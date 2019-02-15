package com.ufoscout.vertk.cron

import com.ufoscout.vertk.launch
import com.ufoscout.vertk.shared.getOrComputeAwait
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.core.shareddata.AsyncMap
import io.vertx.kotlin.core.shareddata.*
import org.slf4j.LoggerFactory
import java.time.Instant

abstract class AbstractBatch(protected val vertx: Vertx) {

    companion object {
        private val BATCH_STATUS_SHARED_MAP_KEY = "BATCH_STATUS_SHARED_MAP_KEY"
        private val JSON_RUNNING = "running"
        private val JSON_LAST_EXECUTION_START_TS = "lastExecutionStartTs"
        private val JSON_LAST_EXECUTION_END_TS = "lastExecutionEndTs"
    }

    protected val logger = LoggerFactory.getLogger(javaClass)

    suspend fun startPeriodicBatch(name: String, enabled: Boolean, execStrategy: ExecutionStrategy, action: suspend () -> Unit) {
        if (enabled) {

            val sharedMapBatchStatusKey = "BATCH_STATUS_${javaClass.name}_${name}"
            val sharedMapBatchStatusCounterKey = "BATCH_STATUS_COUNTER_${javaClass.name}_${name}"

            val map = vertx.sharedData().getAsyncMapAwait<String, JsonObject>(BATCH_STATUS_SHARED_MAP_KEY)

            logger.info("Batch [{}] is enabled. Execution strategy: ", name, execStrategy)

            loop(execStrategy, map, sharedMapBatchStatusKey, sharedMapBatchStatusCounterKey, name, action)

        } else {
            logger.info("Batch [{}] is disabled.", name)
        }
    }


    private fun loop(
            execStrategy: ExecutionStrategy,
            map: AsyncMap<String, JsonObject>,
            sharedMapBatchStatusKey: String,
            sharedMapBatchStatusCounterKey: String,
            name: String,
            action: suspend () -> Unit) {

        var msBeforeNextStart = Math.max(execStrategy.msBeforeNextStart(), 1L)

        vertx.setTimer(msBeforeNextStart) {
            vertx.launch {
                try {
                    execute(map, sharedMapBatchStatusKey, sharedMapBatchStatusCounterKey, name, action)
                } finally {
                    loop(execStrategy, map, sharedMapBatchStatusKey, sharedMapBatchStatusCounterKey, name, action)
                }
            }
        }
    }


    private suspend fun execute(
            map: AsyncMap<String, JsonObject>,
            sharedMapBatchStatusKey: String,
            sharedMapBatchStatusCounterKey: String,
            name: String,
            action: suspend () -> Unit) {
        try {

            logger.debug("Get Counter [{}]", sharedMapBatchStatusCounterKey)
            val counter = vertx.sharedData().getCounterAwait(sharedMapBatchStatusCounterKey)

            var count = counter.getAwait()

            logger.debug("Counter [{}], count is [{}]", sharedMapBatchStatusCounterKey, count)

            if (count > 0L) {
                logger.info("Batch [{}] is still running in this or another node. Skip loop.", name)
                return
            }

            count = counter.incrementAndGetAwait()

            try {

                if (count != 1L) {
                    logger.info("Batch [{}] started concurrently in another node. Skip loop.", name)
                    return
                }

                val now = Instant.now()
                val status = map.getOrComputeAwait(sharedMapBatchStatusKey) {
                    JsonObject()
                            .put(JSON_RUNNING, false)
                            .put(JSON_LAST_EXECUTION_START_TS, Instant.MIN)
                            .put(JSON_LAST_EXECUTION_END_TS, Instant.MIN)
                }

                if (!status.getBoolean(JSON_RUNNING)!!) {

                    logger.debug("Batch [{}] Starting...", name)
                    status.put(JSON_RUNNING, true)
                    map.putAwait(sharedMapBatchStatusKey, status)

                    try {
                        action()
                        logger.debug("Batch [{}] Execution completed successfully", name)
                    } catch (e: RuntimeException) {
                        logger.error("Batch [{}] Execution failed", name, e)
                    } finally {
                        status
                                .put(JSON_RUNNING, false)
                                .put(JSON_LAST_EXECUTION_START_TS, now)
                                .put(JSON_LAST_EXECUTION_END_TS, Instant.now())
                        map.putAwait(sharedMapBatchStatusKey, status)
                    }

                } else {
                    logger.info("Batch [{}] is still running. Skip loop.", name)
                }

            } finally {
                val count = counter.decrementAndGetAwait()
            }
        } catch (e: RuntimeException) {
            logger.error("Batch [{}] Execution failed", name, e)
        }
    }

}

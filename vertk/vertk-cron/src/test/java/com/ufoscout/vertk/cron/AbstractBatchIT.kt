package com.ufoscout.vertk.cron

import com.ufoscout.vertk.BaseTest
import com.ufoscout.vertk.cron.exec.CronExecutionStrategy
import io.vertx.core.Vertx
import io.vertx.kotlin.core.closeAwait
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

internal class AbstractBatchIT: BaseTest() {

    @AfterEach
    fun tearDown() = runBlocking<Unit> {
        vertx.closeAwait()
    }

    @Test
    fun shouldExecuteBatchPeriodicallyWithCron() = runBlocking<Unit> {
        val batch = TestBatch(vertx)
        val name = UUID.randomUUID().toString()
        val enabled = true

        val countDown = CountDownLatch(10)
        val executed = AtomicInteger(0)

        val cronEverySecond = CronExecutionStrategy("0/1 * * * * ?")

        batch.startPeriodicBatch(name, enabled, cronEverySecond) {
            executed.incrementAndGet()
            countDown.countDown()
        }

        countDown.await(3300, TimeUnit.MILLISECONDS)
        assertTrue(executed.get() > 1)
    }

    class TestBatch(val vertx_: Vertx): AbstractBatch(vertx_)

}
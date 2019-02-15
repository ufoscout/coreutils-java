package com.ufoscout.vertk.cron

import com.ufoscout.vertk.BaseTest
import com.ufoscout.vertk.cron.exec.IntervalExecutionStrategy
import io.vertx.core.Vertx
import io.vertx.kotlin.core.closeAwait
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

internal class AbstractBatchTest: BaseTest() {

    @AfterEach
    fun tearDown() = runBlocking<Unit> {
        vertx.closeAwait()
    }

    @Test
    fun shouldNotExecuteDisabledBatch() = runBlocking<Unit> {
        val batch = TestBatch(vertx)
        val name = UUID.randomUUID().toString()
        val enabled = false
        val loopEveryMs = 10L

        val countDown = CountDownLatch(1)
        val executed = AtomicBoolean(false)
        batch.startPeriodicBatch(name, enabled, IntervalExecutionStrategy(loopEveryMs)) {
            executed.set(true)
            countDown.countDown()
        }

        countDown.await(loopEveryMs*2, TimeUnit.MILLISECONDS)
        assert(!executed.get())
    }

    @Test
    fun shouldExecuteBatch() = runBlocking<Unit> {
        val batch = TestBatch(vertx)
        val name = UUID.randomUUID().toString()
        val enabled = true
        val loopEveryMs = 10L

        val countDown = CountDownLatch(1)
        val executed = AtomicBoolean(false)
        batch.startPeriodicBatch(name, enabled, IntervalExecutionStrategy(loopEveryMs)) {
            executed.set(true)
            countDown.countDown()
        }

        countDown.await()
        assert(executed.get())
    }

    @Test
    fun shouldExecuteBatchPeriodically() = runBlocking<Unit> {
        val batch = TestBatch(vertx)
        val name = UUID.randomUUID().toString()
        val enabled = true
        val loopEveryMs = 10L

        val countDown = CountDownLatch(10)
        val executed = AtomicInteger(0)
        batch.startPeriodicBatch(name, enabled, IntervalExecutionStrategy(loopEveryMs)) {
            executed.incrementAndGet()
            countDown.countDown()
        }

        countDown.await(loopEveryMs*100, TimeUnit.MILLISECONDS)
        assertTrue(executed.get() > 1)
    }

    @Test
    fun shouldKeepExecutingBatchEvenInCaseOfException() = runBlocking<Unit> {
        val batch = TestBatch(vertx)
        val name = UUID.randomUUID().toString()
        val enabled = true
        val loopEveryMs = 10L

        val countDown = CountDownLatch(10)
        val executed = AtomicInteger(0)
        batch.startPeriodicBatch(name, enabled, IntervalExecutionStrategy(loopEveryMs, 10L)) {
            executed.incrementAndGet()
            countDown.countDown()
            throw RuntimeException()
        }

        countDown.await(loopEveryMs*100, TimeUnit.MILLISECONDS)
        assertTrue(executed.get() > 1)
    }

    @Test
    fun shouldNotExecuteBatchInParallel() = runBlocking<Unit> {
        val batch = TestBatch(vertx)
        val name = UUID.randomUUID().toString()
        val enabled = true
        val loopEveryMs = 10L

        val countDown = CountDownLatch(10)
        val running = AtomicBoolean(false)
        val foundRunning = AtomicBoolean(false)

        for (i in 0..1000) {
            batch.startPeriodicBatch(name, enabled, IntervalExecutionStrategy(loopEveryMs, 10L)) {
                Thread.sleep(10)
                if (running.get()) {
                    foundRunning.set(true)
                }
                running.set(true)
                Thread.sleep(10)
                running.set(false)
                countDown.countDown()
            }
        }

        countDown.await()
        assert(!foundRunning.get())
    }

    class TestBatch(val vertx_: Vertx): AbstractBatch(vertx_)


}
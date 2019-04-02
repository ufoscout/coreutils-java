package com.ufoscout.vertk.cron.exec

import com.ufoscout.vertk.BaseTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

/**
 * @author Francesco Cina' 04/giu/2010
 */
class IntervalExecutionStrategyTest : BaseTest() {

    @Test
    fun testCount() {
        val interval = 100L
        val delay = 20L
        val executionStrategy = IntervalExecutionStrategy(interval, delay)

        System.out.println(executionStrategy.toString())
        assertTrue(executionStrategy.toString().contains("" + interval))

        assertTrue(executionStrategy.hasOtherExecution())
        var now = Date()
        assertTrue(interval + delay >= executionStrategy.nextExecutionDateAfter(now).time - now.time)

        Thread.sleep(delay)

        now = Date()
        assertTrue(interval >= executionStrategy.nextExecutionDateAfter(now).time - Date().time)

        Thread.sleep(interval)
        assertTrue(executionStrategy.hasOtherExecution())

        System.out.println(executionStrategy.toString())
        assertTrue(executionStrategy.toString().contains("" + interval))
    }

}

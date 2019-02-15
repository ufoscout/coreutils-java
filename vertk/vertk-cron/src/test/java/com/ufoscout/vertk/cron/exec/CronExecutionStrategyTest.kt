package com.ufoscout.vertk.cron.exec

import com.ufoscout.vertk.BaseTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.text.ParseException
import java.util.*

/**
 * @author Francesco Cina' Apr 15, 2012
 */
class CronExecutionStrategyTest : BaseTest() {

    @Test
    @Throws(ParseException::class)
    fun testCron() {
        val cronEverySecond = "0/1 * * * * ?"

        val executionStrategy = CronExecutionStrategy(cronEverySecond)

        assertTrue(executionStrategy.toString().contains(cronEverySecond))

        Thread.sleep(executionStrategy.nextExecutionDateAfter(Date()).time - Date().time)

        assertTrue(executionStrategy.hasOtherExecution())


        val millisToWait = executionStrategy.nextExecutionDateAfter(Date()).time - Date().time
        println("millisToWait = $millisToWait")

        val now = Date()
        Thread.sleep(millisToWait)

        println("Waited for = " + (Date().time - now.time))
        assertTrue(executionStrategy.hasOtherExecution())
    }

}

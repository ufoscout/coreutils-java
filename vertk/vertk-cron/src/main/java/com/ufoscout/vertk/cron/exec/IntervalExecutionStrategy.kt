package com.ufoscout.vertk.cron.exec

import com.ufoscout.vertk.cron.ExecutionStrategy
import java.util.*

/**
 *
 * Execute a job every prefixed interval of time
 *
 * @author Francesco Cina'
 *
 * 27/mar/2010
 */
class IntervalExecutionStrategy(
        private val intervalMilliseconds: Long,
        private val delayMilliseconds: Long = 0L) : ExecutionStrategy {
    private var lastTimeStamp = Date()
    private var firstExecution = true

    override fun toString(): String {
        return "Repeat every  " + this.intervalMilliseconds + " ms"
    }

    override fun hasOtherExecution(): Boolean {
        return true
    }

    override fun newJobExecutionStarted() {
        this.lastTimeStamp = Date()
        firstExecution = false
    }

    override fun nextExecutionDateAfter(date: Date): Date {
        var nextExecution = calculateNextExecutionDate(lastTimeStamp)
        while (nextExecution.before(date)) {
            nextExecution = calculateNextExecutionDate(nextExecution)
        }
        return nextExecution
    }

    private fun calculateNextExecutionDate(previousDate: Date): Date {
        var wait = intervalMilliseconds
        if (firstExecution) {
            wait = intervalMilliseconds + delayMilliseconds
        }
        return Date(previousDate.time + wait)
    }
}

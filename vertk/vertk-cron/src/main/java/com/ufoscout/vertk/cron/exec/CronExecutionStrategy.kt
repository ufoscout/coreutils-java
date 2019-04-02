package com.ufoscout.vertk.cron.exec

import com.ufoscout.vertk.cron.ExecutionStrategy
import com.ufoscout.vertk.cron.util.CronExpression
import java.util.*

/**
 *
 * Execute a job based on a cron expression
 *
 * @author Francesco Cina'
 *
 * Apr 15, 2012
 */
class CronExecutionStrategy(
        cron: String,
        private val startDate: Date = Date()
) : ExecutionStrategy {

    override fun hasOtherExecution(): Boolean {
        return true
    }

    override fun newJobExecutionStarted() {
    }

    val cronExpression = CronExpression(cron)

    override fun toString(): String {
        return "Cron: [" + cronExpression.cronExpression + "]"
    }

    override fun nextExecutionDateAfter(date: Date): Date {
        return if (startDate.after(date)) {
            cronExpression.getTimeAfter(startDate)
        } else cronExpression.getTimeAfter(date)
    }

}

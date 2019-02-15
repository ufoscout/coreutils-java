package com.ufoscout.vertk.cron

import java.util.Date

/**
 *
 * @author Francesco Cina'
 *
 * Apr 4, 2012
 */
interface ExecutionStrategy {

    /**
     * Return milliseconds to wait before next execution
     * @return
     */
    fun msBeforeNextStart(): Long {
        val now = Date()
        val nextExecutionDate = nextExecutionDateAfter(now)
        return nextExecutionDate.time - Date().time
    }

    fun nextExecutionDateAfter(date: Date): Date


    fun hasOtherExecution(): Boolean

    fun newJobExecutionStarted()

}

package com.ufoscout.coreutils.auth

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.slf4j.LoggerFactory
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

abstract class BaseTest {

    private val TIME_FORMAT = DecimalFormat("####,###.###", DecimalFormatSymbols(Locale.US))
    protected val logger = LoggerFactory.getLogger(this.javaClass)
    private var testStartDate: Long = 0

    @BeforeEach
    fun setUpBeforeTest(testInfo: TestInfo) {
        testStartDate = System.currentTimeMillis()
        logger.info("===================================================================")
        logger.info("BEGIN TEST " + testInfo.displayName)
        logger.info("===================================================================")

    }

    @AfterEach
    fun tearDownAfterTest(testInfo: TestInfo) {
        val executionTime = System.currentTimeMillis() - testStartDate
        logger.info("===================================================================")
        logger.info("END TEST " + testInfo.displayName)
        logger.info("execution time: " + TIME_FORMAT.format(executionTime) + " ms")
        logger.info("===================================================================")
    }
}
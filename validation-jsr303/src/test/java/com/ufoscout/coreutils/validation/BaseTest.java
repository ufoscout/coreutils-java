package com.ufoscout.coreutils.validation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public abstract class BaseTest {

    protected static final DecimalFormat TIME_FORMAT = new DecimalFormat("####,###.###",
            new DecimalFormatSymbols(Locale.US));

    private static final String TEMP_DIR = "./target/junit-temp/" + System.currentTimeMillis();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private long testStartDate;

    @BeforeEach
    public final void setUpBeforeTest(TestInfo testInfo) {
        testStartDate = System.currentTimeMillis();
        logger.info("===================================================================");
        logger.info("BEGIN TEST " + testInfo.getDisplayName());
        logger.info("===================================================================");

    }

    @AfterEach
    public final void tearDownAfterTest(TestInfo testInfo) {
        final long executionTime = System.currentTimeMillis() - testStartDate;
        logger.info("===================================================================");
        logger.info("END TEST " + testInfo.getDisplayName());
        logger.info("execution time: " + TIME_FORMAT.format(executionTime) + " ms");
        logger.info("===================================================================");
    }

    protected Logger getLogger() {
        return logger;
    }

    protected String getTempDirectory() {
        new File(TEMP_DIR).mkdirs();
        return TEMP_DIR;
    }

}
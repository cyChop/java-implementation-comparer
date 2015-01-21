package org.keyboardplaying.implcomparer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

/**
 * Test cases for {@link ImplComparisonLogger}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
@RunWith(MockitoJUnitRunner.class)
public class ImplComparisonLoggerTest {

    @Mock
    private Appender mockAppender;
    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

    @Before
    public void setup() {
        // Add the appender to the root logger
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);
    }

    @After
    public void teardown() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(mockAppender);
    }

    @Test
    public void testLog() throws NoSuchMethodException {

        /* Build sample data */
        List<ImplCheckResult> results = new ArrayList<ImplCheckResult>();
        results.add(new ImplCheckResult(ClassWithVariants.class.getMethod("hello", null), ClassWithVariants.hello()));
        results.add(new ImplCheckResult(ClassWithVariants.class.getMethod("hello1", null), ClassWithVariants.hello1()));
        results.add(new ImplCheckResult(ClassWithVariants.class.getMethod("hello2", null), ClassWithVariants.hello2()));
        for (ImplCheckResult result : results) {
            result.addExecutionTime(1337, 42);
        }

        /* Build expectations */
        // Make sure the test is JVM-proof
        String execTime = String.valueOf(1337.0  / 42.0);
        int length = execTime.length();
        String separator = "+--------+-" + new String(new char[length]).replace("\0", "-")
                + "-+--------+";
        String[] expectedLog = { separator, "| Method | Avg time (ms)"
                + new String(new char[length - 13]).replace("\0", " ") + " | Result |",
                separator,
                "| hello  | " + execTime + " |    REF |",
                "| hello1 | " + execTime + " | != REF |",
                "| hello2 | " + execTime + " | == REF |",
                separator };

        /* Execute the code to test. */
        ImplComparisonLogger.log(results);

        /* Check logging matches the expectations. */
        verify(mockAppender, atLeastOnce()).doAppend(captorLoggingEvent.capture());
        List<LoggingEvent> actualLog = captorLoggingEvent.getAllValues();
        assertEquals(expectedLog.length, actualLog.size());

        for (int i = 0; i < expectedLog.length; i++) {
            LoggingEvent log = actualLog.get(i);
            assertEquals(Level.INFO, log.getLevel());
            assertEquals(expectedLog[i], log.getFormattedMessage());
        }
    }
}

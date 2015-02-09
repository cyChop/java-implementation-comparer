/*
 * This file is part of java-implementation-comparer.
 *
 * java-implementation-comparer is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * java-implementation-comparer is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * java-implementation-comparer. If not, see <http://www.gnu.org/licenses/>.
 */
package org.keyboardplaying.comparer.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyboardplaying.comparer.model.ImplCheckResult;
import org.keyboardplaying.comparer.test.ClassWithVariants;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

/**
 * Test cases for {@link ImplComparisonLogger}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
@RunWith(MockitoJUnitRunner.class)
public class ImplComparisonLoggerTest {

    @Mock
    private Appender<ILoggingEvent> mockAppender;
    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

    /** Adds an appender to the root logger in order to capture all the log. */
    @Before
    public void setup() {
        // Add the appender to the root logger
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);
    }

    /** Removes the appender previously added for the test. */
    @After
    public void teardown() {
        // Remove the appender before destroy
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(mockAppender);
    }

    /**
     * Ensures the logging of the result as a comparative array is as expected.
     *
     * @throws NoSuchMethodException
     *             never
     */
    @Test
    public void testLog() throws NoSuchMethodException {

        /* Build sample data */
        List<ImplCheckResult> results = new ArrayList<ImplCheckResult>();
        results.add(new ImplCheckResult(ClassWithVariants.class.getMethod("hello"),
                ClassWithVariants.hello()));
        results.add(new ImplCheckResult(ClassWithVariants.class.getMethod("hello1"),
                ClassWithVariants.hello1()));
        results.add(new ImplCheckResult(ClassWithVariants.class.getMethod("hello2"),
                ClassWithVariants.hello2()));
        for (ImplCheckResult result : results) {
            result.addExecutionTime(1337, 42);
        }

        /* Build expectations */
        // Make sure the test is JVM-proof
        String execTime = String.valueOf(1337.0 / 42.0);
        int length = execTime.length();
        String separator = "+--------+-" + new String(new char[length]).replace("\0", "-")
                + "-+--------+";
        String[] expectedLog = {
                separator,
                "| Method | Avg time (ms)" + new String(new char[length - 13]).replace("\0", " ")
                        + " | Result |", separator, "| hello  | " + execTime + " |    REF |",
                "| hello1 | " + execTime + " | != REF |", "| hello2 | " + execTime + " | == REF |",
                separator };

        /* Execute the code to test. */
        ImplComparisonLogger logger = new ImplComparisonLogger();
        logger.log(results);

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

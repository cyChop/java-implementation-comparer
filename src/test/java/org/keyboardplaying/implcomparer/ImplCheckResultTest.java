package org.keyboardplaying.implcomparer;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Test;

/**
 * Test cases for {@link ImplCheckResult}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class ImplCheckResultTest {

    private static final double DELTA = 0.0000001;
    private static final double EXACT = 0.;

    @Test
    public void testCheckResult() throws NoSuchMethodException {
        // quick and dirty way to have a method
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        Object result = "A random result";

        ImplCheckResult cResult = new ImplCheckResult(method, result);

        assertEquals(method, cResult.getMethod());
        assertEquals(result, cResult.getMethodResult());

        assertEquals(0, cResult.getAverageExecutionTime(), EXACT);
        assertEquals(0, cResult.getNumberOfExecutions());

        cResult.addExecutionTime(42);
        assertEquals(42, cResult.getAverageExecutionTime(), EXACT);
        assertEquals(1, cResult.getNumberOfExecutions());

        cResult.addExecutionTime(1337, 32);
        assertEquals((double) 1379 / 33, cResult.getAverageExecutionTime(), DELTA);
        assertEquals(33, cResult.getNumberOfExecutions());
    }
}

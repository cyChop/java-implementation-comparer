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
package org.keyboardplaying.comparer.model;

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

    /**
     * Tests the getters and setters of an {@link ImplCheckResult}.
     *
     * @throws NoSuchMethodException
     *             never thrown
     */
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

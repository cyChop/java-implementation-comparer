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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.keyboardplaying.comparer.model.ComparisonException;
import org.keyboardplaying.comparer.model.ImplCheckResult;
import org.keyboardplaying.comparer.test.ClassWithVariants;

/**
 * Test cases for {@link ImplComparer}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class ImplComparerTest {

    /** Tests the configuration of the comparer. */
    @Test
    public void testConfiguration() {
        ImplComparer comparer = new ImplComparer();
        comparer.setChecks(42);
        comparer.setIterations(1337);
        assertEquals(42, comparer.getChecks());
        assertEquals(1337, comparer.getIterations());
    }

    /** Tests the calling of the comparison for a method which does not exist. */
    @Test
    public void testNoSuchMethod() {
        try {
            new ImplComparer().compare(new ClassWithVariants(), "notHere", new Class<?>[] { String.class },
                    new Object[] { "A String param" });
            fail("An exception with NoSuchMethodException as cause should have been thrown.");
        } catch (ComparisonException e) {
            assertTrue(e.getCause() instanceof NoSuchMethodException);
        }
    }

    /** Tests the calling of the comparison for a static method which does not exist. */
    @Test
    public void testNoSuchMethodStatic() {
        try {
            new ImplComparer().compareStatic(ClassWithVariants.class, "notHere", new Class<?>[] {}, new Object[] {});
            fail("An exception with NoSuchMethodException as cause should have been thrown.");
        } catch (ComparisonException e) {
            assertTrue(e.getCause() instanceof NoSuchMethodException);
        }
    }

    /**
     * Tests the comparison of non-static methods.
     *
     * @throws ComparisonException
     *             never
     */
    @Test
    public void testCompare() throws ComparisonException {
        ImplComparer comparer = new ImplComparer();

        String prm = "A String param";

        ClassWithVariants target = new ClassWithVariants();
        List<ImplCheckResult> comparison = comparer.compare(target, "string", new Class<?>[] { String.class },
                new Object[] { prm });

        assertEquals("Incorrect number of variants", 3, comparison.size());
        for (ImplCheckResult result : comparison) {
            assertEquals("Incorrect method result", prm, result.getMethodResult());
        }

        // Convenient access to results
        ImplCheckResult string = comparison.get(0);
        ImplCheckResult string2 = comparison.get(2);

        // Ensure these are the correct objects
        assertEquals("string", string.getMethod().getName());
        assertEquals("string2", string2.getMethod().getName());
        // Do test
        assertTrue("Unexpected performances", string2.getAverageExecutionTime() < string.getAverageExecutionTime());
    }

    /**
     * Tests the comparison of static methods.
     *
     * @throws ComparisonException
     *             never
     */
    @Test
    public void testCompareStatic() throws ComparisonException {
        ImplComparer comparer = new ImplComparer();
        comparer.setChecks(5);
        comparer.setIterations(1000);

        List<ImplCheckResult> comparison = comparer.compareStatic(ClassWithVariants.class, "hello", new Class<?>[] {},
                new Object[] {});

        assertEquals("Incorrect number of variants", 3, comparison.size());

        // Convenient access to results
        ImplCheckResult hello = comparison.get(0);
        ImplCheckResult hello1 = comparison.get(1);
        ImplCheckResult hello2 = comparison.get(2);

        // Ensure these are the correct objects
        assertEquals("hello", hello.getMethod().getName());
        assertEquals("hello1", hello1.getMethod().getName());
        assertEquals("hello2", hello2.getMethod().getName());
        // Do test
        assertEquals("Hello, World!", hello.getMethodResult());
        assertNotEquals("Hello, World!", hello1.getMethodResult());
        assertEquals("Hello, World!", hello2.getMethodResult());
        assertTrue("Unexpected performances", hello.getAverageExecutionTime() < hello2.getAverageExecutionTime());
    }

    /**
     * Tests the comparison of methods with void returns.
     *
     * @throws ComparisonException
     *             never
     */
    @Test
    public void testCompareWithVoidReturn() throws ComparisonException {
        ImplComparer comparer = new ImplComparer();
        comparer.setChecks(5);
        comparer.setIterations(1000);

        List<ImplCheckResult> comparison = comparer.compare(new ClassWithVariants(), "returnNothing", new Class<?>[] {},
                new Object[] {});

        assertEquals("Incorrect number of variants", 2, comparison.size());

        // Convenient access to results
        ImplCheckResult nothing = comparison.get(0);
        ImplCheckResult nothing1 = comparison.get(1);

        // Ensure these are the correct objects
        assertEquals("returnNothing", nothing.getMethod().getName());
        assertEquals("returnNothing1", nothing1.getMethod().getName());
        // Do test
        assertNull(nothing.getMethodResult());
        assertNull(nothing1.getMethodResult());
    }

    /**
     * Tests the comparison of methods with null returns.
     *
     * @throws ComparisonException
     *             never
     */
    @Test
    public void testCompareWithNullReturn() throws ComparisonException {
        ImplComparer comparer = new ImplComparer();
        comparer.setChecks(5);
        comparer.setIterations(1000);

        List<ImplCheckResult> comparison = comparer.compare(new ClassWithVariants(), "returnNull", new Class<?>[] {},
                new Object[] {});

        assertEquals("Incorrect number of variants", 2, comparison.size());

        // Convenient access to results
        ImplCheckResult nothing = comparison.get(0);
        ImplCheckResult nothing1 = comparison.get(1);

        // Ensure these are the correct objects
        assertEquals("returnNull", nothing.getMethod().getName());
        assertEquals("returnNull1", nothing1.getMethod().getName());
        // Do test
        assertNull(nothing.getMethodResult());
        assertNull(nothing1.getMethodResult());
    }

    /**
     * Tests the passing of null as erasure and parameters for no-argument methods.
     *
     * @throws ComparisonException
     *             never
     */
    @Test
    public void testNullForNoArg() throws ComparisonException {
        ImplComparer comparer = new ImplComparer();
        comparer.setChecks(1);
        comparer.setIterations(10);

        comparer.compare(new ClassWithVariants(), "noArgMethod", null, null);
        comparer.compareStatic(ClassWithVariants.class, "hello", null, null);
    }

    /**
     * Tests the behavior for methods throwing an exception.
     *
     * @throws ComparisonException
     *             never
     */
    @Test
    public void testExceptionalMethod() throws ComparisonException {
        List<ImplCheckResult> comparison = new ImplComparer().compare(new ClassWithVariants(), "throwException", null,
                null);
        assertEquals(1, comparison.size());
        Object e = comparison.get(0).getMethodResult();
        assertTrue(e instanceof RuntimeException);
        assertEquals("dummy-exception-test-message", ((RuntimeException) e).getMessage());
    }
}

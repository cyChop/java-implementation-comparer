package org.keyboardplaying.comparer.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.keyboardplaying.comparer.model.ImplCheckResult;
import org.keyboardplaying.comparer.test.ClassWithVariants;

/**
 * Test cases for {@link ImplComparer}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class ImplComparerTest {

    @Test(expected = NoSuchMethodException.class)
    public void testNoSuchMethod() throws NoSuchMethodException {
        new ImplComparer().compare(new ClassWithVariants(), "notHere",
                new Class<?>[] { String.class }, new Object[] { "A String param" });
    }

    @Test(expected = NoSuchMethodException.class)
    public void testNoSuchMethodStatic() throws NoSuchMethodException {
        new ImplComparer().compareStatic(ClassWithVariants.class, "notHere", new Class<?>[] {},
                new Object[] {});
    }

    @Test
    public void testCompare() throws NoSuchMethodException {
        ImplComparer comparer = new ImplComparer();

        String prm = "A String param";

        ClassWithVariants target = new ClassWithVariants();
        List<ImplCheckResult> comparison = comparer.compare(target, "string",
                new Class<?>[] { String.class }, new Object[] { prm });

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
        assertTrue("Unexpected performances",
                string2.getAverageExecutionTime() < string.getAverageExecutionTime());
    }

    @Test
    public void testCompareStatic() throws NoSuchMethodException {
        ImplComparer comparer = new ImplComparer();
        comparer.setChecks(5);
        comparer.setIterations(1000);

        List<ImplCheckResult> comparison = comparer.compareStatic(ClassWithVariants.class, "hello",
                new Class<?>[] {}, new Object[] {});

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
        assertTrue("Unexpected performances",
                hello.getAverageExecutionTime() < hello2.getAverageExecutionTime());
    }

    @Test
    public void testNullForNoArg() throws NoSuchMethodException {
        ImplComparer comparer = new ImplComparer();
        comparer.setChecks(1);
        comparer.setIterations(10);

        comparer.compare(new ClassWithVariants(), "noArgMethod", null, null);
        comparer.compareStatic(ClassWithVariants.class, "hello", null, null);
    }

    @Test
    public void testExceptionalMethod() throws NoSuchMethodException {
        List<ImplCheckResult> comparison = new ImplComparer().compare(new ClassWithVariants(),
                "throwException", null, null);
        assertEquals(1, comparison.size());
        Object e = comparison.get(0).getMethodResult();
        assertTrue(e instanceof RuntimeException);
        assertEquals("dummy-exception-test-message", ((RuntimeException) e).getMessage());
    }
}

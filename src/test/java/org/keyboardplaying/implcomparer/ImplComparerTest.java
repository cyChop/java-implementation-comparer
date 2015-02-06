package org.keyboardplaying.implcomparer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

/**
 * Test cases for {@link ImplComparer}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class ImplComparerTest {

    @Test(expected = NoSuchMethodException.class)
    public void testNoSuchMethod() throws NoSuchMethodException {
        ClassWithVariants target = new ClassWithVariants();
        List<ImplCheckResult> comparison =
                ImplComparer.compare(target, "notHere", new Class<?>[] { String.class },
                new Object[] { "A String param" });
    }

    @Test(expected = NoSuchMethodException.class)
    public void testNoSuchMethodStatic() throws NoSuchMethodException {
        List<ImplCheckResult> comparison =
                ImplComparer.compareStatic(ClassWithVariants.class, "notHere",
                new Class<?>[] {}, new Object[] {});
    }

    @Test
    public void testCompare() throws NoSuchMethodException {
        String prm = "A String param";

        ClassWithVariants target = new ClassWithVariants();
        List<ImplCheckResult> comparison =
                ImplComparer.compare(target, "string", new Class<?>[] { String.class },
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
        assertTrue("Unexpected performances", string2.getAverageExecutionTime()
            < string.getAverageExecutionTime());
    }

    @Test
    public void testCompareStatic() throws NoSuchMethodException {
        List<ImplCheckResult> comparison =
                ImplComparer.compareStatic(ClassWithVariants.class, "hello",
                new Class<?>[] {}, new Object[] {}, 5, 1000);

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
        assertTrue("Unexpected performances", hello.getAverageExecutionTime()
            < hello2.getAverageExecutionTime());
    }

    @Test
    public void testNullForNoArg() throws NoSuchMethodException {
        ImplComparer.compare(new ClassWithVariants(), "noArgMethod", null, null, 1, 10);
        ImplComparer.compareStatic(ClassWithVariants.class, "hello", null, null, 1, 10);
    }
    
    @Test
    public void testExceptionalMethod() throws NoSuchMethodException {
        ImplComparer.compare(new ClassWithVariants(), "throwException", null, null);
    }
}

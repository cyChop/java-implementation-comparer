package org.keyboardplaying.comparer.test;

/**
 * A class with variants of methods for implementation comparer testing.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class ClassWithVariants {

    public void noArgMethod() {
        // this does nothing
    }

    public String string(String arg) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arg.length(); i++) {
            sb.append(arg.charAt(i));
        }
        return sb.toString();
    }

    public String string1(String arg) {
        return new String(arg);
    }

    public String string2(String arg) {
        return arg;
    }

    public static String hello() {
        return "Hello, World!";
    }

    public static String hello1() {
        return "Hello, result error!";
    }

    public static String hello2() {
        return new ClassWithVariants().string(hello());
    }

    public void throwException() {
        throw new RuntimeException(
                "This is only to test how the comparer behaves in case of an exception");
    }
}

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
package org.keyboardplaying.comparer.test;

/**
 * A class with variants of methods for implementation comparer testing.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class ClassWithVariants {

    /** A simple method to test comparison on a method without any argument. */
    public void noArgMethod() {
        // this does nothing
    }

    /**
     * Simple method to test method with one {@link String} argument.
     *
     * @param arg
     *            an argument
     * @return a string equals to passed argument
     */
    public String string(String arg) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arg.length(); i++) {
            sb.append(arg.charAt(i));
        }
        return sb.toString();
    }

    /**
     * Simple method to test method with one {@link String} argument.
     *
     * @param arg
     *            an argument
     * @return a string equals to passed argument
     */
    public String string1(String arg) {
        return new String(arg);
    }

    /**
     * Simple method to test method with one {@link String} argument.
     *
     * @param arg
     *            an argument
     * @return a string equals to passed argument
     */
    public String string2(String arg) {
        return arg;
    }

    /**
     * Simple method.
     *
     * @return {@code "Hello, World!"}
     */
    public static String hello() {
        return "Hello, World!";
    }

    /**
     * Simple method to emulate an incorrect variant.
     *
     * @return {@code "Hello, result error!"}
     */
    public static String hello1() {
        return "Hello, result error!";
    }

    /**
     * Simple method to emulate a less performant variant.
     *
     * @return {@code "Hello, World!"}
     */
    public static String hello2() {
        return new ClassWithVariants().string(hello());
    }

    /** Test for a method with void return. */
    public void returnNothing() {
    }

    /** Test for a method with void return. */
    public void returnNothing1() {
    }

    /** Test for a method with null return. */
    public String returnNull() {
        return null;
    }

    /** Test for a method with null return. */
    public String returnNull1() {
        return null;
    }

    /** Method which throws an exception. */
    public void throwException() {
        throw new RuntimeException("dummy-exception-test-message");
    }
}

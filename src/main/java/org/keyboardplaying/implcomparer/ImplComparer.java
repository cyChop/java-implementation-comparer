package org.keyboardplaying.implcomparer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a utility to compare performances of implementations of a method.
 * <p/>
 * This comparer relies on reflection to find the methods to compare and ensure each method
 * implementation proposition returns the same result.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public final class ImplComparer {

    /** The default number of checks per comparison. */
    private static final int DEFAULT_CHECKS = 3;
    /** The default number of iterations per time check. */
    private static final int DEFAULT_ITERATIONS = 10000;

    private static Logger log = LoggerFactory.getLogger(ImplComparer.class);

    /* Utility class should not be instanciated. */
    private ImplComparer() {
    }

    /**
     * Compares several implementations of a static method.
     * <p/>
     * The method variants should be named after the original method but suffixed with a 1-based
     * integer index. The variants should have the same erasure and return type as the original
     * method.
     * <p/>
     * Thus, if you wish to compare implementations of {@code aSimpleMethod(String, int)}, variants
     * should be named {@code aSimpleMethod1(String, int)}, {@code aSimpleMethod2(String, int)},
     * and so on. Variants will be automatically detected using reflectivity.
     * <p/>
     * The default number of checks ({@value #DEFAULT_CHECKS}) and iterations per check
     * ({@value #DEFAULT_ITERATIONS}) will be used.
     *
     * @param target
     *            the instance to run the comparison on
     * @param methodName
     *            the name of the original method
     * @param erasure
     *            the types of the parameters; {@code null} tolerated in case of
     *            a no-arg method
     * @param parameters
     *            the parameters to use for comparison; {@code null} tolerated
     *            in case of a no-arg method
     * @return a list of performance check result
     * @throws NoSuchMethodException
     *             if the requested original method does not exist
     */
    public static List<ImplCheckResult> compare(Object target, String methodName,
            Class<?>[] erasure, Object[] parameters) throws NoSuchMethodException {
        return compare(target, methodName, erasure, parameters, DEFAULT_CHECKS, DEFAULT_ITERATIONS);
    }

    /**
     * Compares several implementations of a static method.
     * <p/>
     * The method variants should be named after the original method but suffixed with a 1-based
     * integer index. The variants should have the same erasure and return type as the original
     * method.
     * <p/>
     * Thus, if you wish to compare implementations of {@code aSimpleMethod(String, int)}, variants
     * should be named {@code aSimpleMethod1(String, int)}, {@code aSimpleMethod2(String, int)},
     * and so on. Variants will be automatically detected using reflectivity.
     * <p/>
     * You can set how many performance checks will be run, and how many iterations each check will
     * represent.
     *
     * @param target
     *            the instance to run the comparison on
     * @param methodName
     *            the name of the original method
     * @param erasure
     *            the types of the parameters; {@code null} tolerated in case of
     *            a no-arg method
     * @param parameters
     *            the parameters to use for comparison; {@code null} tolerated
     *            in case of a no-arg method
     * @param checks
     *            the number of times the performance checks will be run
     * @param iterations
     *            the number of times the method is executed during each check
     * @return a list of performance check result
     * @throws NoSuchMethodException
     *             if the requested original method does not exist
     */
    public static List<ImplCheckResult> compare(Object target, String methodName,
            Class<?>[] erasure, Object[] parameters, int checks, int iterations)
            throws NoSuchMethodException {
        return compare(target, target.getClass(), methodName, erasure, parameters, checks,
                iterations);
    }

    /**
     * Compares several implementations of a static method.
     * <p/>
     * The method variants should be named after the original method but suffixed with a 1-based
     * integer index. The variants should have the same erasure and return type as the original
     * method.
     * <p/>
     * Thus, if you wish to compare implementations of {@code aSimpleMethod(String, int)}, variants
     * should be named {@code aSimpleMethod1(String, int)}, {@code aSimpleMethod2(String, int)},
     * and so on. Variants will be automatically detected using reflectivity.
     * <p/>
     * The default number of checks ({@value #DEFAULT_CHECKS}) and iterations per check
     * ({@value #DEFAULT_ITERATIONS}) will be used.
     *
     * @param klass
     *            the {@link Class} to run the comparison for
     * @param methodName
     *            the name of the original method
     * @param erasure
     *            the types of the parameters; {@code null} tolerated in case of
     *            a no-arg method
     * @param parameters
     *            the parameters to use for comparison; {@code null} tolerated
     *            in case of a no-arg method
     * @return a list of performance check result
     * @throws NoSuchMethodException
     *             if the requested original method does not exist
     */
    public static List<ImplCheckResult> compareStatic(Class<?> klass, String methodName,
            Class<?>[] erasure, Object[] parameters) throws NoSuchMethodException {
        return compareStatic(klass, methodName, erasure, parameters, DEFAULT_CHECKS,
                DEFAULT_ITERATIONS);
    }

    /**
     * Compares several implementations of a static method.
     * <p/>
     * The method variants should be named after the original method but suffixed with a 1-based
     * integer index. The variants should have the same erasure and return type as the original
     * method.
     * <p/>
     * Thus, if you wish to compare implementations of {@code aSimpleMethod(String, int)}, variants
     * should be named {@code aSimpleMethod1(String, int)}, {@code aSimpleMethod2(String, int)},
     * and so on. Variants will be automatically detected using reflectivity.
     * <p/>
     * You can set how many performance checks will be run, and how many iterations each check will
     * represent.
     *
     * @param klass
     *            the {@link Class} to run the comparison for
     * @param methodName
     *            the name of the original method
     * @param erasure
     *            the types of the parameters; {@code null} tolerated in case of
     *            a no-arg method
     * @param parameters
     *            the parameters to use for comparison; {@code null} tolerated
     *            in case of a no-arg method
     * @param checks
     *            the number of times the performance checks will be run
     * @param iterations
     *            the number of times the method is executed during each check
     * @return a list of performance check result
     * @throws NoSuchMethodException
     *             if the requested original method does not exist
     */
    public static List<ImplCheckResult> compareStatic(Class<?> klass, String methodName,
            Class<?>[] erasure, Object[] parameters, int checks, int iterations)
            throws NoSuchMethodException {
        return compare(null, klass, methodName, erasure, parameters, checks, iterations);
    }

    /**
     * Compares several implementations of a static method.
     * <p/>
     * The method variants should be named after the original method but suffixed with a 1-based
     * integer index. The variants should have the same erasure and return type as the original
     * method.
     * <p/>
     * Thus, if you wish to compare implementations of {@code aSimpleMethod(String, int)}, variants
     * should be named {@code aSimpleMethod1(String, int)}, {@code aSimpleMethod2(String, int)},
     * and so on. Variants will be automatically detected using reflectivity.
     *
     * @param target
     *            the instance to run the comparison on
     * @param klass
     *            the {@link Class} to run the comparison for
     * @param methodName
     *            the name of the original method
     * @param erasure
     *            the types of the parameters; {@code null} tolerated in case of
     *            a no-arg method
     * @param parameters
     *            the parameters to use for comparison; {@code null} tolerated
     *            in case of a no-arg method
     * @param checks
     *            the number of times the performance checks will be run
     * @param iterations
     *            the number of times the method is executed during each check
     * @return a list of performance check result
     * @throws NoSuchMethodException
     *             if the requested original method does not exist
     */
    private static List<ImplCheckResult> compare(Object target, Class<?> klass, String methodName,
            Class<?>[] erasure, Object[] parameters, int checks, int iterations)
            throws NoSuchMethodException {
        log.info("Beginning performance comparison for method <{}>, ({} check(s), {} iteration(s) per check",
                methodName, checks, iterations);
        List<Method> methods = loadMethods(klass, methodName, erasure);

        log.debug("{} variants found (including original).", methods.size());
        List<ImplCheckResult> results =  initCheckResultList(methods, target, parameters);
        for (int c = 0; c < checks; c++) {
            log.debug("Beginning time check #{}", c);
            performTimeChecks(results, target, parameters, iterations);
        }
        return results;
    }

    /**
     * Returns a list containing the original method and its variants.
     * <p/>
     * The method variants should be named after the original method but suffixed with a 1-based
     * integer index. The variants should have the same erasure and return type as the original
     * method.
     * <p/>
     * The discovery is made using reflectivity.
     *
     * @param klass
     *            the {@link Class} defining the methods
     * @param methodName
     *            the name of the original method
     * @param erasure
     *            the types of the parameters; {@code null} tolerated in case of
     *            a no-arg method
     * @return a {@code List} beginning with the original {@link Method} and the
     *         followed with its suffixed variants
     * @throws NoSuchMethodException
     *             when the original method could not be found
     */
    private static List<Method> loadMethods(Class<?> klass, String methodName, Class<?>[] erasure)
            throws NoSuchMethodException {
        // Result list
        List<Method> methods = new ArrayList<Method>();

        // Add base method
        methods.add(klass.getMethod(methodName, erasure));

        // Add all variants to compare
        int i = 1;
        boolean found = true;
        do {
            try {
                methods.add(klass.getMethod(methodName + i++, erasure));
            } catch (NoSuchMethodException e) {
                found = false;
            }
        } while (found);

        return methods;
    }

    /**
     * Initializes a list of {@link ImplCheckResult} from a list of {@link Method}, the
     * object to call the method on and the parameters for the call.
     * <p/>
     * The elements of the result will not contain performance-related information, only the
     * {@link Method} and return value.
     *
     * @param methods
     *            the method and its variants
     * @param target
     *            the instance to call the method on; {@code null} tolerated for static methods
     * @param parameters
     *            the parameters to use when calling the method; {@code null} tolerated
     *            in case of a no-arg method
     * @return a list of {@link ImplCheckResult} instances, initialized with the
     *      {@link Method} and return value
     */
    private static List<ImplCheckResult> initCheckResultList(List<Method> methods, Object target,
            Object[] parameters) {
        List<ImplCheckResult> results = new ArrayList<ImplCheckResult>();
        for (Method method : methods) {
            results.add(new ImplCheckResult(method,
                    invokeMethod(target, method, parameters)));
        }
        return results;
    }

    /**
     * Perform a time check for each {@link ImplCheckResult} supplied in parameters.
     *
     * @param results
     *            the {@link ImplCheckResult} instances to enrich with performance
     *            information
     * @param target
     *            the instance to call the method on; {@code null} tolerated for static methods
     * @param parameters
     *            the parameters to use when calling the method; {@code null} tolerated
     *            in case of a no-arg method
     */
    private static void performTimeChecks(List<ImplCheckResult> results, Object target,
            Object[] parameters, int iterations) {
        for (ImplCheckResult result : results) {
            Method method = result.getMethod();
            log.debug("Beginning new time check for <{}>", method.getName());

            long startTime = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                invokeMethod(target, method, parameters);
            }
            long executionTime = (System.nanoTime() - startTime) / 1000;
            result.addExecutionTime(executionTime, iterations);
            log.debug("Time check for {} done (iterations: {}; total time: {} ms)",
                    method.getName(), executionTime, iterations);
        }
    }

    /**
     * Exception-safely invokes a method.
     * <p/>
     * If the method throws an exception, it will be returned instead of the method result. This was
     * retained as a better way to fully compare methods' behavior.
     *
     * @param target
     *            the instance to call the method on; {@code null} tolerated for static methods
     * @param method
     *            the method to invoke
     * @param parameters
     *            the parameters to use when calling the method; {@code null} tolerated
     *            in case of a no-arg method
     * @return the method's result or the thrown exception if any
     */
    private static Object invokeMethod(Object target, Method method, Object[] parameters) {
        Object result;
        try {
            result = method.invoke(target, parameters);
        } catch (Exception e) {
            result = e;
        }
        return result;
    }
}

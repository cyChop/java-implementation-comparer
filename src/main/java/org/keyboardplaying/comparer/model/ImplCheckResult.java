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

import java.lang.reflect.Method;

/**
 * An object to contain the results of a performance check.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public class ImplCheckResult {

    private Method method;
    private long totalExecutionTime = 0L;
    private int totalExecutions = 0;
    private Object methodResult;

    /**
     * Creates a new instance.
     *
     * @param method
     *            the method
     * @param methodResult
     *            the result of the method
     */
    public ImplCheckResult(Method method, Object methodResult) {
        this.method = method;
        this.methodResult = methodResult;
    }

    /**
     * Adds the execution time for one execution to this result and increments the number of executions.
     *
     * @param executionTime
     *            the execution time
     */
    public void addExecutionTime(long executionTime) {
        addExecutionTime(executionTime, 1);
    }

    /**
     * Adds the execution time for the a supplied number of executions and increments the number of executions
     * accordingly.
     *
     * @param executionTime
     *            the execution time
     * @param nbExecutions
     *            the number of executions
     */
    public void addExecutionTime(long executionTime, int nbExecutions) {
        this.totalExecutionTime += executionTime;
        this.totalExecutions += nbExecutions;
    }

    /**
     * Returns the method of the method this result corresponds to.
     *
     * @return the method of the method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Returns the result of the method.
     * <p/>
     * In case the method returns a random result, no guarantee on the value is made.
     *
     * @return the result of the method
     */
    public Object getMethodResult() {
        return methodResult;
    }

    /**
     * Calculates the average execution time of this method.
     *
     * @return the average execution time
     */
    public double getAverageExecutionTime() {
        return totalExecutions == 0 ? 0. : (double) totalExecutionTime / totalExecutions;
    }

    /**
     * Returns the number of time this method was executed to obtain this result.
     *
     * @return the number of executions
     */
    public int getNumberOfExecutions() {
        return totalExecutions;
    }
}

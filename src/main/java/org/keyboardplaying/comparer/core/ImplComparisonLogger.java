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

import java.util.List;
import java.util.Objects;

import org.alcibiade.asciiart.raster.CharacterRaster;
import org.alcibiade.asciiart.raster.ExtensibleCharacterRaster;
import org.alcibiade.asciiart.raster.RasterContext;
import org.alcibiade.asciiart.widget.TableWidget;
import org.alcibiade.asciiart.widget.TextPanel;
import org.alcibiade.asciiart.widget.model.AbstractTableModel;
import org.keyboardplaying.comparer.model.ImplCheckResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility class to easily print the result of an implementation comparison as table to the logs.
 * <p/>
 * The table will contain three columns:
 * <ul>
 * <li>the name of the method being tested;</li>
 * <li>the average execution time;</li>
 * <li>whether the invocation result equals the reference result or not.</li>
 * </ul>
 * <p/>
 * The logging is made with the INFO level.
 * <p/>
 * The logging is based on logback and SLF4J. Default logging is to the console, but you can change the logging channel
 * with a logback configuration file or supplying your own SLF4J {@link Logger}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public final class ImplComparisonLogger {

    private static final Logger LOG = LoggerFactory.getLogger(ImplComparisonLogger.class);

    /**
     * Logs the results as a table.
     *
     * @param results
     *            the results to LOG
     */
    public void log(List<ImplCheckResult> results) {
        TableWidget widget = new TableWidget(new ImplComparisonTable(results));
        if (LOG.isInfoEnabled()) {
            TextPanel textPanel = new TextPanel();
            textPanel.add(widget);

            CharacterRaster raster = new ExtensibleCharacterRaster(' ');
            textPanel.render(new RasterContext(raster));

            for (String line : raster) {
                LOG.info(line);
            }
        }
    }

    /**
     * A table model for logging the results of a comparison.
     *
     * @author Cyrille Chopelet (http://keyboardplaying.org)
     */
    private static class ImplComparisonTable extends AbstractTableModel {

        private final ImplComparisonColumn[] columns = { new MethodComparisonColumn(), new AvgTimeComparisonColumn(),
                new ResultComparisonColumn() };

        private final List<ImplCheckResult> results;

        public ImplComparisonTable(List<ImplCheckResult> results) {
            this.results = results;
        }

        @Override
        public int getWidth() {
            return columns.length;
        }

        @Override
        public int getHeight() {
            return results.size();
        }

        @Override
        public String getCellContent(int x, int y) {
            return columns[x].getValue(results.get(y));
        }

        @Override
        public String getColumnTitle(int x) {
            return columns[x].getTitle();
        }
    }

    /**
     * A column model for the logging.
     *
     * @author Cyrille Chopelet (http://keyboardplaying.org)
     */
    private interface ImplComparisonColumn {

        /**
         * Returns the title of the column.
         *
         * @return the title
         */
        String getTitle();

        /**
         * Returns the formatted value for this column.
         *
         * @param result
         *            the line being displayed
         * @return the formatted value
         */
        String getValue(ImplCheckResult result);
    }

    /**
     * A column to display the method the row corresponds to.
     *
     * @author Cyrille Chopelet (http://keyboardplaying.org)
     */
    private static class MethodComparisonColumn implements ImplComparisonColumn {

        /*
         * (non-Javadoc)
         *
         * @see org.keyboardplaying.comparer.core.ImplComparisonLogger.ImplComparisonColumn#getTitle()
         */
        @Override
        public String getTitle() {
            return "Method";
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.keyboardplaying.comparer.core.ImplComparisonLogger.ImplComparisonColumn#getValue(org.keyboardplaying.
         * comparer.model.ImplCheckResult)
         */
        @Override
        public String getValue(ImplCheckResult result) {
            return result.getMethod().getName();
        }
    }

    /**
     * A column to display the average execution time for a method.
     *
     * @author Cyrille Chopelet (http://keyboardplaying.org)
     */
    private static class AvgTimeComparisonColumn implements ImplComparisonColumn {

        /*
         * (non-Javadoc)
         *
         * @see org.keyboardplaying.comparer.core.ImplComparisonLogger.ImplComparisonColumn#getTitle()
         */
        @Override
        public String getTitle() {
            return "Avg time (ms)";
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.keyboardplaying.comparer.core.ImplComparisonLogger.ImplComparisonColumn#getValue(org.keyboardplaying.
         * comparer.model.ImplCheckResult)
         */
        @Override
        public String getValue(ImplCheckResult result) {
            return String.valueOf(result.getAverageExecutionTime());
        }
    }

    /**
     * A column to display equality with the reference result.
     * <p/>
     * Will display:
     * <ul>
     * <li>{@code REF} if this result is the same object as the reference method;</li>
     * <li>{@code == REF} if this result equals the reference method;</li>
     * <li>{@code != REF} otherwise.</li>
     * </ul>
     * <p/>
     * <strong>This object should only be used once per table.</strong>
     *
     * @author Cyrille Chopelet (http://keyboardplaying.org)
     */
    private static class ResultComparisonColumn implements ImplComparisonColumn {

        private static final String NAME_REF = "REF";

        private Object referenceResult;
        private boolean referenceSet = false;

        /*
         * (non-Javadoc)
         *
         * @see org.keyboardplaying.comparer.core.ImplComparisonLogger.ImplComparisonColumn#getTitle()
         */
        @Override
        public String getTitle() {
            return "Result";
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.keyboardplaying.comparer.core.ImplComparisonLogger.ImplComparisonColumn#getValue(org.keyboardplaying.
         * comparer.model.ImplCheckResult)
         */
        @Override
        public String getValue(ImplCheckResult result) {
            String content;
            if (referenceSet) {
                if (referenceResult == result.getMethodResult()) {
                    content = "   " + NAME_REF;
                } else {
                    content = (Objects.equals(referenceResult, result.getMethodResult()) ? "== " : "!= ") + NAME_REF;
                }
            } else {
                referenceResult = result.getMethodResult();
                referenceSet = true;
                content = "   " + NAME_REF;
            }
            return content;
        }
    }
}

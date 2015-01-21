package org.keyboardplaying.implcomparer;

import java.util.List;
import java.util.Objects;

import org.alcibiade.asciiart.raster.CharacterRaster;
import org.alcibiade.asciiart.raster.ExtensibleCharacterRaster;
import org.alcibiade.asciiart.raster.RasterContext;
import org.alcibiade.asciiart.slf4j.AsciiArtLogger;
import org.alcibiade.asciiart.widget.model.AbstractTableModel;
import org.alcibiade.asciiart.widget.TableWidget;
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
 * The logging is based on logback and SLF4J. Default logging is to the console, but you can change
 * the logging channel with a logback configuration file or supplying your own SLF4J {@link Logger}.
 *
 * @author Cyrille Chopelet (http://keyboardplaying.org)
 */
public final class ImplComparisonLogger {

    private static Logger log = LoggerFactory.getLogger(ImplComparisonLogger.class);

    /** Utility class should not be instanciated. */
    private ImplComparisonLogger() {
    }

    /**
     * Logs the results as a table.
     *
     * @param results
     *            the results to log
     */
    public static void log(List<ImplCheckResult> results) {
        log(results, log);
    }

    /**
     * Logs the results as a table.
     *
     * @param results
     *            the results to log
     * @param logger
     *            the {@link Logger} to log to
     */
    public static void log(List<ImplCheckResult> results, Logger logger) {
        TableWidget tableWidget = new TableWidget(new ImplComparisonTable(results));
        new AsciiArtLogger(logger).info(tableWidget);
    }

    /**
     * A table model for logging the results of a comparison.
     *
     * @author Cyrille Chopelet (http://keyboardplaying.org)
     */
    private static class ImplComparisonTable extends AbstractTableModel {

        private static final String NAME_REF = "REF";

        private Object referenceResult;
        private boolean referenceSet = false;

        private final ImplComparisonColumn[] columns = {
            new ImplComparisonColumn() {
                @Override
                public String getTitle() {
                    return "Method";
                }

                @Override
                public String getValue(ImplCheckResult result) {
                    return result.getMethod().getName();
                }
            },
            new ImplComparisonColumn() {
                @Override
                public String getTitle() {
                    return "Avg time (ms)";
                }

                @Override
                public String getValue(ImplCheckResult result) {
                    return String.valueOf(result.getAverageExecutionTime());
                }
            },
            new ImplComparisonColumn() {
                @Override
                public String getTitle() {
                    return "Result";
                }

                @Override
                public String getValue(ImplCheckResult result) {
                    String content;
                    if (referenceSet) {
                        if (referenceResult == result.getMethodResult()) {
                            content = "   " + NAME_REF;
                        } else {
                            content = (Objects.equals(referenceResult, result.getMethodResult()) ?
                                    "== " : "!= ") + NAME_REF;
                        }
                    } else {
                        referenceResult = result.getMethodResult();
                        referenceSet = true;
                        content = "   " + NAME_REF;
                    }
                    return content;
                }
            }
        };

        private List<ImplCheckResult> results;

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
    private static interface ImplComparisonColumn {

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
}

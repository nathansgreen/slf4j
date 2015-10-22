/**
 * Copyright (c) 2015 QOS.ch
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package org.slf4j;

import org.junit.Test;
import org.slf4j.helpers.BasicMarkerFactory;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.slf4j.SupplierTest.State.*;

/**
 * Test the various uses of {@link Supplier} (and {@link ArraySupplier}) to
 * ensure that the default methods in {@link Logger} are implemented without
 * any bugs.
 */
public class SupplierTest {
    enum State {
        DEFAULT, //@formatter:off
        TRACE_1_ARG, TRACE_2_ARG, TRACE_N_ARG, MTRACE_1_ARG, MTRACE_2_ARG, MTRACE_N_ARG,
        DEBUG_1_ARG, DEBUG_2_ARG, DEBUG_N_ARG, MDEBUG_1_ARG, MDEBUG_2_ARG, MDEBUG_N_ARG,
        INFO_1_ARG, INFO_2_ARG, INFO_N_ARG, MINFO_1_ARG, MINFO_2_ARG, MINFO_N_ARG,
        WARN_1_ARG, WARN_2_ARG, WARN_N_ARG, MWARN_1_ARG, MWARN_2_ARG, MWARN_N_ARG,
        ERROR_1_ARG, ERROR_2_ARG, ERROR_N_ARG, MERROR_1_ARG, MERROR_2_ARG, MERROR_N_ARG,
    } //@formatter:on

    @SuppressWarnings("all")
    static <T> T[] of(T... arr) {
        return arr;
    }

    LoggerImpl logger = new LoggerImpl();
    Marker marker = new BasicMarkerFactory().getDetachedMarker("SupplierTest");
    int diff = new Random().nextInt(10000);

    void assrt(State expected, Runnable invoke) {
        invoke.run();
        assertEquals(expected, logger.state());
    }

    @Test
    public void testTrace() {
        logger.marker = false;
        assrt(TRACE_1_ARG, () -> logger.trace("{}    ", () -> diff));
        assrt(TRACE_2_ARG, () -> logger.trace("{}{}  ", () -> diff, diff));
        assrt(TRACE_2_ARG, () -> logger.trace("{}{}  ", diff, () -> diff));
        assrt(TRACE_2_ARG, () -> logger.trace("{}{}  ", () -> diff, () -> diff));
        assrt(TRACE_N_ARG, () -> logger.trace("{}{}{}", () -> diff, () -> diff, () -> diff));
        assrt(TRACE_N_ARG, () -> logger.trace("{}{}{}", () -> of(diff)));
    }

    @Test
    public void testMarkerTrace() {
        logger.marker = true;
        assrt(MTRACE_1_ARG, () -> logger.trace(marker, "{}    ", () -> diff));
        assrt(MTRACE_2_ARG, () -> logger.trace(marker, "{}{}  ", () -> diff, diff));
        assrt(MTRACE_2_ARG, () -> logger.trace(marker, "{}{}  ", diff, () -> diff));
        assrt(MTRACE_2_ARG, () -> logger.trace(marker, "{}{}  ", () -> diff, () -> diff));
        assrt(MTRACE_N_ARG, () -> logger.trace(marker, "{}{}{}", () -> diff, () -> diff, () -> diff));
        assrt(MTRACE_N_ARG, () -> logger.trace(marker, "{}{}{}", () -> of(diff)));
    }

    @Test
    public void testDebug() {
        logger.marker = false;
        assrt(DEBUG_1_ARG, () -> logger.debug("{}    ", () -> diff));
        assrt(DEBUG_2_ARG, () -> logger.debug("{}{}  ", () -> diff, diff));
        assrt(DEBUG_2_ARG, () -> logger.debug("{}{}  ", diff, () -> diff));
        assrt(DEBUG_2_ARG, () -> logger.debug("{}{}  ", () -> diff, () -> diff));
        assrt(DEBUG_N_ARG, () -> logger.debug("{}{}{}", () -> diff, () -> diff, () -> diff));
        assrt(DEBUG_N_ARG, () -> logger.debug("{}{}{}", () -> of(diff)));
    }

    @Test
    public void testMarkerDebug() {
        logger.marker = true;
        assrt(MDEBUG_1_ARG, () -> logger.debug(marker, "{}    ", () -> diff));
        assrt(MDEBUG_2_ARG, () -> logger.debug(marker, "{}{}  ", () -> diff, diff));
        assrt(MDEBUG_2_ARG, () -> logger.debug(marker, "{}{}  ", diff, () -> diff));
        assrt(MDEBUG_2_ARG, () -> logger.debug(marker, "{}{}  ", () -> diff, () -> diff));
        assrt(MDEBUG_N_ARG, () -> logger.debug(marker, "{}{}{}", () -> diff, () -> diff, () -> diff));
        assrt(MDEBUG_N_ARG, () -> logger.debug(marker, "{}{}{}", () -> of(diff)));
    }

    @Test
    public void testInfo() {
        logger.marker = false;
        assrt(INFO_1_ARG, () -> logger.info("{}    ", () -> diff));
        assrt(INFO_2_ARG, () -> logger.info("{}{}  ", () -> diff, diff));
        assrt(INFO_2_ARG, () -> logger.info("{}{}  ", diff, () -> diff));
        assrt(INFO_2_ARG, () -> logger.info("{}{}  ", () -> diff, () -> diff));
        assrt(INFO_N_ARG, () -> logger.info("{}{}{}", () -> diff, () -> diff, () -> diff));
        assrt(INFO_N_ARG, () -> logger.info("{}{}{}", () -> of(diff)));
    }

    @Test
    public void testMarkerInfo() {
        logger.marker = true;
        assrt(MINFO_1_ARG, () -> logger.info(marker, "{}    ", () -> diff));
        assrt(MINFO_2_ARG, () -> logger.info(marker, "{}{}  ", () -> diff, diff));
        assrt(MINFO_2_ARG, () -> logger.info(marker, "{}{}  ", diff, () -> diff));
        assrt(MINFO_2_ARG, () -> logger.info(marker, "{}{}  ", () -> diff, () -> diff));
        assrt(MINFO_N_ARG, () -> logger.info(marker, "{}{}{}", () -> diff, () -> diff, () -> diff));
        assrt(MINFO_N_ARG, () -> logger.info(marker, "{}{}{}", () -> of(diff)));
    }

    @Test
    public void testWarn() {
        logger.marker = false;
        assrt(WARN_1_ARG, () -> logger.warn("{}    ", () -> diff));
        assrt(WARN_2_ARG, () -> logger.warn("{}{}  ", () -> diff, diff));
        assrt(WARN_2_ARG, () -> logger.warn("{}{}  ", diff, () -> diff));
        assrt(WARN_2_ARG, () -> logger.warn("{}{}  ", () -> diff, () -> diff));
        assrt(WARN_N_ARG, () -> logger.warn("{}{}{}", () -> diff, () -> diff, () -> diff));
        assrt(WARN_N_ARG, () -> logger.warn("{}{}{}", () -> of(diff)));
    }

    @Test
    public void testMarkerWarn() {
        logger.marker = true;
        assrt(MWARN_1_ARG, () -> logger.warn(marker, "{}    ", () -> diff));
        assrt(MWARN_2_ARG, () -> logger.warn(marker, "{}{}  ", () -> diff, diff));
        assrt(MWARN_2_ARG, () -> logger.warn(marker, "{}{}  ", diff, () -> diff));
        assrt(MWARN_2_ARG, () -> logger.warn(marker, "{}{}  ", () -> diff, () -> diff));
        assrt(MWARN_N_ARG, () -> logger.warn(marker, "{}{}{}", () -> diff, () -> diff, () -> diff));
        assrt(MWARN_N_ARG, () -> logger.warn(marker, "{}{}{}", () -> of(diff)));
    }

    @Test
    public void testError() {
        logger.marker = false;
        assrt(ERROR_1_ARG, () -> logger.error("{}    ", () -> diff));
        assrt(ERROR_2_ARG, () -> logger.error("{}{}  ", () -> diff, diff));
        assrt(ERROR_2_ARG, () -> logger.error("{}{}  ", diff, () -> diff));
        assrt(ERROR_2_ARG, () -> logger.error("{}{}  ", () -> diff, () -> diff));
        assrt(ERROR_N_ARG, () -> logger.error("{}{}{}", () -> diff, () -> diff, () -> diff));
        assrt(ERROR_N_ARG, () -> logger.error("{}{}{}", () -> of(diff)));
    }

    @Test
    public void testMarkerError() {
        logger.marker = true;
        assrt(MERROR_1_ARG, () -> logger.error(marker, "{}    ", () -> diff));
        assrt(MERROR_2_ARG, () -> logger.error(marker, "{}{}  ", () -> diff, diff));
        assrt(MERROR_2_ARG, () -> logger.error(marker, "{}{}  ", diff, () -> diff));
        assrt(MERROR_2_ARG, () -> logger.error(marker, "{}{}  ", () -> diff, () -> diff));
        assrt(MERROR_N_ARG, () -> logger.error(marker, "{}{}{}", () -> diff, () -> diff, () -> diff));
        assrt(MERROR_N_ARG, () -> logger.error(marker, "{}{}{}", () -> of(diff)));
    }

    static class LoggerImpl implements Logger {
        private State state = DEFAULT;
        Boolean marker = null;

        public State state() {
            State r = state;
            state = DEFAULT;
            return r;
        }

        public String getName() {
            return getClass().getSimpleName();
        }

        public boolean isTraceEnabled() {
            return !this.marker;
        }

        public void trace(String msg) {}

        public void trace(String format, Object arg) {
            state = TRACE_1_ARG;
        }

        public void trace(String format, Object arg1, Object arg2) {
            state = TRACE_2_ARG;
        }

        public void trace(String format, Object... arguments) {
            state = TRACE_N_ARG;
        }

        public void trace(String msg, Throwable t) {}

        public boolean isTraceEnabled(Marker marker) {
            return this.marker;
        }

        public void trace(Marker marker, String msg) {}

        public void trace(Marker marker, String format, Object arg) {
            state = MTRACE_1_ARG;
        }

        public void trace(Marker marker, String format, Object arg1, Object arg2) {
            state = MTRACE_2_ARG;
        }

        public void trace(Marker marker, String format, Object... argArray) {
            state = MTRACE_N_ARG;
        }

        public void trace(Marker marker, String msg, Throwable t) {}

        public boolean isDebugEnabled() {
            return !this.marker;
        }

        public void debug(String msg) {}

        public void debug(String format, Object arg) {
            state = DEBUG_1_ARG;
        }

        public void debug(String format, Object arg1, Object arg2) {
            state = DEBUG_2_ARG;
        }

        public void debug(String format, Object... arguments) {
            state = DEBUG_N_ARG;
        }

        public void debug(String msg, Throwable t) {
            state = DEBUG_N_ARG;
        }

        public boolean isDebugEnabled(Marker marker) {
            return this.marker;
        }

        public void debug(Marker marker, String msg) {}

        public void debug(Marker marker, String format, Object arg) {
            state = MDEBUG_1_ARG;
        }

        public void debug(Marker marker, String format, Object arg1, Object arg2) {
            state = MDEBUG_2_ARG;
        }

        public void debug(Marker marker, String format, Object... arguments) {
            state = MDEBUG_N_ARG;
        }

        public void debug(Marker marker, String msg, Throwable t) {}

        public boolean isInfoEnabled() {
            return !this.marker;
        }

        public void info(String msg) {}

        public void info(String format, Object arg) {
            state = INFO_1_ARG;
        }

        public void info(String format, Object arg1, Object arg2) {
            state = INFO_2_ARG;
        }

        public void info(String format, Object... arguments) {
            state = INFO_N_ARG;
        }

        public void info(String msg, Throwable t) {}

        public boolean isInfoEnabled(Marker marker) {
            return this.marker;
        }

        public void info(Marker marker, String msg) {}

        public void info(Marker marker, String format, Object arg) {
            state = MINFO_1_ARG;
        }

        public void info(Marker marker, String format, Object arg1, Object arg2) {
            state = MINFO_2_ARG;
        }

        public void info(Marker marker, String format, Object... arguments) {
            state = MINFO_N_ARG;
        }

        public void info(Marker marker, String msg, Throwable t) {}

        public boolean isWarnEnabled() {
            return !this.marker;
        }

        public void warn(String msg) {}

        public void warn(String format, Object arg) {
            state = WARN_1_ARG;
        }

        public void warn(String format, Object... arguments) {
            state = WARN_N_ARG;
        }

        public void warn(String format, Object arg1, Object arg2) {
            state = WARN_2_ARG;
        }

        public void warn(String msg, Throwable t) {}

        public boolean isWarnEnabled(Marker marker) {
            return this.marker;
        }

        public void warn(Marker marker, String msg) {}

        public void warn(Marker marker, String format, Object arg) {
            state = MWARN_1_ARG;
        }

        public void warn(Marker marker, String format, Object arg1, Object arg2) {
            state = MWARN_2_ARG;
        }

        public void warn(Marker marker, String format, Object... arguments) {
            state = MWARN_N_ARG;
        }

        public void warn(Marker marker, String msg, Throwable t) {}

        public boolean isErrorEnabled() {
            return !this.marker;
        }

        public void error(String msg) {}

        public void error(String format, Object arg) {
            state = ERROR_1_ARG;
        }

        public void error(String format, Object arg1, Object arg2) {
            state = ERROR_2_ARG;
        }

        public void error(String format, Object... arguments) {
            state = ERROR_N_ARG;
        }

        public void error(String msg, Throwable t) {}

        public boolean isErrorEnabled(Marker marker) {
            return this.marker;
        }

        public void error(Marker marker, String msg) {}

        public void error(Marker marker, String format, Object arg) {
            state = MERROR_1_ARG;
        }

        public void error(Marker marker, String format, Object arg1, Object arg2) {
            state = MERROR_2_ARG;
        }

        public void error(Marker marker, String format, Object... arguments) {
            state = MERROR_N_ARG;
        }

        public void error(Marker marker, String msg, Throwable t) {}
    }
}

/**
 * Copyright (c) 2004-2015 QOS.ch
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
package org.slf4j.helpers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import junit.framework.TestCase;
import org.slf4j.Logger;

/**
 * @author Chetan Mehrotra
 */
public class SubstitutableLoggerTest extends TestCase {
    private static final Set<String> EXCLUDED_METHODS = Collections.singleton("getName");

    public void testDelegate() throws Exception {
        SubstituteLogger log = new SubstituteLogger("foo");
        assertTrue(log.delegate() instanceof NOPLogger);

        Set<String> expectedMethodSignatures = determineMethodSignatures(Logger.class);
        LoggerInvocationHandler ih = new LoggerInvocationHandler();
        Logger proxyLogger = (Logger) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { Logger.class }, ih);
        log.setDelegate(proxyLogger);

        invokeMethods(log);

        // Assert that all methods are delegated
        expectedMethodSignatures.removeAll(ih.getInvokedMethodSignatures());
        if (!expectedMethodSignatures.isEmpty()) {
            fail("Following methods are not delegated " + expectedMethodSignatures.toString());
        }
    }

    private void invokeMethods(Logger proxyLogger) throws InvocationTargetException, IllegalAccessException {
        Arrays.asList(Logger.class.getDeclaredMethods()).stream()
            .filter(m -> !EXCLUDED_METHODS.contains(m.getName()))
            .filter(m -> !m.isDefault())
            .forEach(m -> {
                try {
                    m.invoke(proxyLogger, new Object[m.getParameterTypes().length]);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
        });
    }

    private class LoggerInvocationHandler implements InvocationHandler {
        private final Set<String> invokedMethodSignatures = new HashSet<>();

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            invokedMethodSignatures.add(getMethodSignature(method));
            if (method.getName().startsWith("is")) {
                return true;
            }
            return null;
        }

        public Set<String> getInvokedMethodSignatures() {
            return invokedMethodSignatures;
        }
    }

    private static Set<String> determineMethodSignatures(Class<Logger> loggerClass) {
        return Arrays.asList(loggerClass.getDeclaredMethods()).stream()
            .filter(m -> !EXCLUDED_METHODS.contains(m.getName()))
            .filter(m -> !m.isDefault())
            .map(m -> getMethodSignature(m))
            .collect(Collectors.toSet());
    }

    private static String getMethodSignature(Method m) {
        List<String> result = new ArrayList<>();
        result.add(m.getName());
        for (Class<?> clazz : m.getParameterTypes()) {
            result.add(clazz.getSimpleName());
        }
        return result.toString();
    }
}

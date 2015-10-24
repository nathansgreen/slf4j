/**
 * Copyright (c) 2015 QOS.ch
 * All rights reserved.
 * <p/>
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 * <p/>
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 * <p/>
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.slf4j;

/**
 * Code needed by the default methods of {@code Logger},
 */
class Util {
    private Util() {}

    private static final Object[] EMPTY_ARRAY = new Object[0];

    static Object get(Supplier<?> arg) {
        return safeGet(arg);
    }

    static Object[] getAll(Supplier<?>[] arguments) {
        return safeGetAll(arguments);
    }

    static Object[] get(ArraySupplier<?> arg) {
        return safeGet(arg);
    }

    static Object unsafeGet(Supplier<?> arg) {
        return arg == null ? null : arg.get();
    }

    static Object safeGet(Supplier<?> arg) {
        try {
            return arg == null ? null : arg.get();
        } catch (Throwable t) {
            System.err.println("SLF4J: Failed invocation of [Supplier<?>.get()]");
            t.printStackTrace();
            return null;
        }
    }

    static Object[] unsafeGet(ArraySupplier<?> arg) {
        return arg == null ? EMPTY_ARRAY : arg.get();
    }

    static Object[] safeGet(ArraySupplier<?> arg) {
        try {
            return arg == null ? EMPTY_ARRAY : arg.get();
        } catch (Throwable t) {
            System.err.println("SLF4J: Failed invocation of [ArraySupplier<?>.get()]");
            t.printStackTrace();
            return null;
        }
    }

    /**
     * Implementation detail for default methods
     *
     * @param arguments one or more suppliers
     * @return results of evaluating all suppliers
     */
    static Object[] unsafeGetAll(Supplier<?>[] arguments) {
        if(arguments == null || arguments.length < 1) {
            return EMPTY_ARRAY;
        }
        Object[] args = new Object[arguments.length];
        for(int i = 0; i < arguments.length; i++) {
            args[i] = unsafeGet(arguments[i]);
        }
        return args;
    }

    static Object[] safeGetAll(Supplier<?>[] arguments) {
        if(arguments == null || arguments.length < 1) {
            return EMPTY_ARRAY;
        }
        Object[] args = new Object[arguments.length];
        for(int i = 0; i < arguments.length; i++) {
            args[i] = safeGet(arguments[i]);
        }
        return args;
    }

}

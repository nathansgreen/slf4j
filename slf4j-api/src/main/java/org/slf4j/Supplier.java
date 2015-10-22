package org.slf4j;

/**
 * Supplies results.
 *
 * @param <T> the type of result supplied
 * @see java.util.function.Supplier
 */
@FunctionalInterface
public interface Supplier<T> {

    /**
     * Get a result.
     *
     * @return a result
     */
    T get();

}

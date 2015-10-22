package org.slf4j;

/**
 * Supplies an array of results.
 *
 * @param <T> the type of result supplied
 * @see java.util.function.Supplier
 */
@FunctionalInterface
public interface ArraySupplier<T> {

    /**
     * Get a result.
     *
     * @return a result
     */
    T[] get();

}

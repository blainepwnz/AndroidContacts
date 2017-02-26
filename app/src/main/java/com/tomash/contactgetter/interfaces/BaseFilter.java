package com.tomash.contactgetter.interfaces;

/**
 * <p>
 *     Base class for all filters
 * </p>
 * @param <T> Type of container where from can get filterable data
 * @param <V> Type of filterable data
 */

public abstract class BaseFilter<T,V> implements Filterable {
    /**
     * <p>
     *     This pattern will be used for all future filtering.
     * </p>
     * @return pattern for filtering
     */
    protected abstract V getFilterPattern();

    /**
     * <p>
     *     Used for getting target that should be compared with patter to be filtered or not.
     * </p>
     * @param data container where from data could be obtained
     * @return target fro comparing with pattern
     */
    protected abstract V getFilterData(T data);

    /**
     * <p>
     *     Should return condition that will filter data
     * </p>
     * @param data target data
     * @param pattern pattern to compare with
     * @return
     */
    protected abstract boolean getFilterCondition(V data, V pattern);
}

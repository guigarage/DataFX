package org.datafx.util;

import java.io.Serializable;

/**
 * A base interface for all entites that have a unique id
 * @param <T> type of the id
 */
public interface EntityWithId<T> extends Serializable {

    /**
     * Returns the id
     * @return the id
     */
    T getId();
}


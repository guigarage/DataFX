package org.datafx.provider;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;

/**
 *
 * @author johan
 */
public interface DataProvider<T> {
    /**
     * A ObservableValue that wraps the provided Data. Maybe at the creation
     * of this class the provided data is not loaded. So you can add a
     * ChangeLister to the ObservableValue. The listener will be invoked once the
     * data is loaded.
     *
     * @return the data obtained by this DataProvider.
     */
    ObservableValue<T> getData();

    /**
     * Starts to retrieve the data in a background thread and returns the Worker for
     * monitoring. The Worker has the generic type T. So you can access the loaded data by the
     * Worker once it's finished.
     *
     * @return Worker that retrieves the data
     * 
     */
    public Worker<T> retrieve();
}

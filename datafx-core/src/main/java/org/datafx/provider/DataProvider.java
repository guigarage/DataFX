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
     * @return
     */
    ObservableValue<T> getData();

    /**
     * Starts to retrieve the data in Background and returns the worker for some
     * monitoring. I don't return a Service here like in ObjectDataSource. A
     * Service has methods like start(), restart(), getExecutor() that should
     * not called from outside because the ObjectReceiver should manage all the
     * threading. With a Worker you can only check the state of the process. The
     * Worker has the generic type T. So you can access the loaded Data by the
     * Worker once it's finished.
     *
     * @return Worker that retrieves the data
     * 
     */
    public Worker<T> retrieve();
}

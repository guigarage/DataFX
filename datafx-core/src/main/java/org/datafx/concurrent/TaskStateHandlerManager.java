package org.datafx.concurrent;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * A helper class that holds weak references of {@link TaskStateHandler} instances. The class is used to provide the functionality of the {@link TaskStateHandler} class for runnable and callable types.
 *
 * @see TaskStateHandler
 * @see DataFxCallable
 * @see DataFxRunnable
 *
 * @author Hendrik Ebbers
 *
 */
public class TaskStateHandlerManager {

    private static Map<WeakReference, TaskStateHandler> weakMap;

    private TaskStateHandlerManager() {}

    /**
     *  adds a {@link TaskStateHandler} for the given task to the internal weak map.
     * @param task the task for that the {@link TaskStateHandler} instance should be registered
     * @param stateHandler the {@link TaskStateHandler} instance
     */
    public static synchronized void add(Object task, TaskStateHandler stateHandler) {
        if (weakMap == null) {
            weakMap = new HashMap<>();
        }
        weakMap.put(new WeakReference(task), stateHandler);
    }

    /**
     * Returns the registered  {@link TaskStateHandler} instance for the given task
     * @param task the task
     * @return the {@link TaskStateHandler} instance
     */
    public static synchronized TaskStateHandler get(Object task) {
        for (WeakReference ref : weakMap.keySet()) {
            if (ref.get() == null) {
                //TODO: remove
            } else if (ref.get().equals(task)) {
                return weakMap.get(ref);
            }
        }
        return null;
    }
}

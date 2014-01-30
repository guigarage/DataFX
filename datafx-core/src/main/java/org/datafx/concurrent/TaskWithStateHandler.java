package org.datafx.concurrent;

public interface TaskWithStateHandler {
    /**
     * This method will be called by DataFX to inject a
     * <code>TaskStateHandler</code> in this callable. The handler can be used
     * to provide some more feedback of this callable.
     *
     * @param stateHandler the injected TaskStateHandler
     */
    default void injectStateHandler(TaskStateHandler stateHandler) {
        TaskStateHandlerManager.add(this, stateHandler);
    }

    default TaskStateHandler getStateHandler(TaskStateHandler stateHandler) {
        return TaskStateHandlerManager.get(this);
    }
}

package org.datafx.concurrent;

/**
 * A interface that adds support for {@link TaskStateHandler}
 *
 * @author Hendrik Ebbers
 * @see TaskStateHandler
 */
public interface TaskWithStateHandler {
    /**
     * This method will be called by DataFX to inject a
     * {@link TaskStateHandler} in this callable. The handler can be used
     * to provide some more feedback of this callable.
     *
     * @param stateHandler the injected TaskStateHandler
     */
    default void injectStateHandler(TaskStateHandler stateHandler) {
        TaskStateHandlerManager.add(this, stateHandler);
    }

    /**
     * Returns the {@link TaskStateHandler} for this instance
     * @return the {@link TaskStateHandler} for this instance
     */
    default TaskStateHandler getStateHandler() {
        return TaskStateHandlerManager.get(this);
    }

    public default void updateTaskTitle(String title) {
        getStateHandler().updateTaskTitle(title);
    }

    default void updateTaskMessage(String message){
        getStateHandler().updateTaskMessage(message);
    }

    default void updateTaskProgress(double workDone, double max){
        getStateHandler().updateTaskProgress(workDone, max);
    }

    default void updateTaskProgress(long workDone, long max){
        getStateHandler().updateTaskProgress(workDone, max);
    }

    default void setCancelable(boolean cancelable){
        getStateHandler().setCancelable(cancelable);
    }
}

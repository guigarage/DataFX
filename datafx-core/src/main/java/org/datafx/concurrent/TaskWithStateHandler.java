package org.datafx.concurrent;

/**
 * A interface that adds support for <tt>TaskStateHandler</tt>
 *
 * @author Hendrik Ebbers
 * @see TaskStateHandler
 */
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

    /**
     * Returns the <tt>TaskStateHandler</tt> for this instance
     * @return the <tt>TaskStateHandler</tt> for this instance
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

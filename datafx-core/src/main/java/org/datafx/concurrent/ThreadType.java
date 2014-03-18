package org.datafx.concurrent;

/**
 * Defines the type of a thread. This supports the JavaFX Application thread and any other thread
 *
 * @author Hendrik Ebbers
 */
public enum ThreadType {
    /**
     * The JavaFX Application Thread
     */
    PLATFORM,

    /**
     * A background thread that is NOT the JavaFX Application Thread
     */
    EXECUTOR;
}

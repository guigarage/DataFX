package org.datafx.concurrent;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class TaskStateHandlerManager {

    private static Map<WeakReference, TaskStateHandler> weakMap;

    public static synchronized void add(Object task, TaskStateHandler stateHandler) {
        if (weakMap == null) {
            weakMap = new HashMap<>();
        }
        weakMap.put(new WeakReference(task), stateHandler);
    }

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

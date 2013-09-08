package org.datafx.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;

public class ConcurrentUtils {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void runAndWait(Runnable runnable)
			throws InterruptedException, ExecutionException {
		FutureTask<?> future = new FutureTask(runnable, null);
		Platform.runLater(future);
		future.get();
	}

	public static <T> T runAndWait(Callable<T> callable)
			throws InterruptedException, ExecutionException {
		FutureTask<T> future = new FutureTask<T>(callable);
		Platform.runLater(future);
		return future.get();
	}
}

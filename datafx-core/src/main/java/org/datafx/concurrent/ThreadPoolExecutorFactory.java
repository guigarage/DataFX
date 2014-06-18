package org.datafx.concurrent;

import org.datafx.DataFXConfiguration;
import org.datafx.util.ExceptionHandler;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory to create a default thread pool for all background tasks. A developer can use the
 * {@link #getThreadPoolExecutor()} method if a {@link ExecutorService} is needed. If a {@link Executor} can be used a
 * developer should use the {@link ObservableExecutor} instead.
 * @see ObservableExecutor
 */
public class ThreadPoolExecutorFactory {

    private static final Logger LOGGER = Logger.getLogger(ThreadPoolExecutorFactory.class.getName());

    private static ThreadPoolExecutor defaultExecutor;

    private static void onUncaughtException(Thread thread, Throwable throwable) {
        if (!ExceptionHandler.isLogException()) {
            LOGGER.log(Level.SEVERE, "Uncaught throwable in " + thread.getName(), throwable);
        }
        ExceptionHandler.getDefaultInstance().setException(throwable);
    }

    /**
     * Returns the {@code ThreadPoolExecutor} that can be used for all background tasks. the returned
     * {@link ExecutorService} is a singleton that will be used by all background operations of DataFX, too.
     * The size of the thread pool can be configured by using the {@link DataFXConfiguration} class before this method
     * will be called the first time.
     * @return the executor service singleton
     */
    public static synchronized ThreadPoolExecutor getThreadPoolExecutor() {
        if(defaultExecutor == null) {

            BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>() {
                @Override
                public boolean offer(Runnable runnable) {
                    if (defaultExecutor.getPoolSize() < DataFXConfiguration.getInstance().getDefaultThreadMaxSize()) {
                        return false;
                    }
                    return super.offer(runnable);
                }
            };

            ThreadFactory threadFactory = new ThreadFactory() {
                @Override
                public Thread newThread(final Runnable run) {
                    ThreadGroup threadGroup = AccessController.doPrivileged(new PrivilegedAction<ThreadGroup>() {
                        @Override
                        public ThreadGroup run() {
                            return new ThreadGroup(DataFXConfiguration.getInstance().getThreadGroupName());
                        }
                    });

                    // Addition of doPrivileged added due to RT-19580
                    return AccessController.doPrivileged(new PrivilegedAction<Thread>() {
                        @Override
                        public Thread run() {
                            final Thread th = new Thread(threadGroup, run);
                            th.setUncaughtExceptionHandler((t, e) -> onUncaughtException(t, e));
                            th.setPriority(Thread.MIN_PRIORITY);
                            th.setDaemon(true);
                            return th;
                        }
                    });
                }
            };

            defaultExecutor = new ThreadPoolExecutor(
                    DataFXConfiguration.getInstance().getDefaultThreadPoolStartSize(), DataFXConfiguration.getInstance().getDefaultThreadMaxSize(),
                    DataFXConfiguration.getInstance().getDefaultThreadTimeout(), TimeUnit.MILLISECONDS,
                    queue, threadFactory, new ThreadPoolExecutor.AbortPolicy());
            defaultExecutor.allowCoreThreadTimeOut(true);
        }
        return defaultExecutor;
    }
}

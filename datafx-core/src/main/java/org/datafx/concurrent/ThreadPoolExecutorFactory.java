package org.datafx.concurrent;

import org.datafx.DataFXConfiguration;
import org.datafx.util.ExceptionHandler;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadPoolExecutorFactory {

    private static final Logger LOGGER = Logger.getLogger(ThreadPoolExecutorFactory.class.getName());

    private static ThreadPoolExecutor defaultExecutor;

    private static void onUncaughtException(Thread thread, Throwable throwable) {
        if (!ExceptionHandler.isLogException()) {
            LOGGER.log(Level.SEVERE, "Uncaught throwable in " + thread.getName(), throwable);
        }
        ExceptionHandler.getDefaultInstance().setException(throwable);
    }

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

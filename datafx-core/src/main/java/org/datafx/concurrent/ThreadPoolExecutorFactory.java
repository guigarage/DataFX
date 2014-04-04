package org.datafx.concurrent;

import org.datafx.util.ExceptionHandler;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadPoolExecutorFactory {

    private static final Logger LOGGER = Logger.getLogger(ThreadPoolExecutorFactory.class.getName());
    private static final int THREAD_POOL_SIZE = 32;
    private static final long THREAD_TIME_OUT = 1000;
    private static final BlockingQueue<Runnable> IO_QUEUE = new LinkedBlockingQueue<Runnable>() {
        @Override
        public boolean offer(Runnable runnable) {
            if (EXECUTOR.getPoolSize() < THREAD_POOL_SIZE) {
                return false;
            }
            return super.offer(runnable);
        }
    };

    private static final ThreadGroup DATAFX_THREAD_GROUP = AccessController.doPrivileged(new PrivilegedAction<ThreadGroup>() {
        @Override
        public ThreadGroup run() {
            return new ThreadGroup("DataFX thread pool");
        }
    });

    private static final ThreadFactory DATAFX_THREAD_FACTORY = new ThreadFactory() {
        @Override
        public Thread newThread(final Runnable run) {
            // Addition of doPrivileged added due to RT-19580
            return AccessController.doPrivileged(new PrivilegedAction<Thread>() {
                @Override
                public Thread run() {
                    final Thread th = new Thread(DATAFX_THREAD_GROUP, run);
                    th.setUncaughtExceptionHandler((t, e) -> onUncaughtException(t, e));
                    th.setPriority(Thread.MIN_PRIORITY);
                    th.setDaemon(true);
                    return th;
                }
            });
        }
    };
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
            2, THREAD_POOL_SIZE,
            THREAD_TIME_OUT, TimeUnit.MILLISECONDS,
            IO_QUEUE, DATAFX_THREAD_FACTORY, new ThreadPoolExecutor.AbortPolicy());

    static {
        EXECUTOR.allowCoreThreadTimeOut(true);
    }

    private static void onUncaughtException(Thread thread, Throwable throwable) {
        if (!ExceptionHandler.isLogException()) {
            LOGGER.log(Level.SEVERE, "Uncaught throwable in " + thread.getName(), throwable);
        }
        ExceptionHandler.getDefaultInstance().setException(throwable);
    }

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        return EXECUTOR;
    }
}

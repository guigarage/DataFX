/**
 * Copyright (c) 2011, 2014, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL DataFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.core.concurrent;

//import org.datafx.DataFXConfiguration;
import io.datafx.core.Assert;
import io.datafx.core.DataFXConfiguration;
import io.datafx.core.ExceptionHandler;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadPoolExecutorFactory {

    private static final Logger LOGGER = Logger.getLogger(ThreadPoolExecutorFactory.class.getName());

    private static ThreadPoolExecutor defaultExecutor;

    private static void onUncaughtException(final Thread thread, final Throwable throwable) {
        Assert.requireNonNull(thread, "thread");
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

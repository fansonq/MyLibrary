package com.example.fansonlib.http.ThreadPool;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by：fanson
 * Created on：2016/11/17 13:32
 * Describe：ThreadPool Manager
 * Use：ThreadPoolManager.getThreadPoolProxy().execute(runnable);
 */
public class ThreadPoolManager {
    private static final String TAG = ThreadPoolManager.class.getSimpleName();

    private static ThreadPoolProxy mThreadPoolProxy;

    public static ThreadPoolProxy getThreadPoolProxy() {
        if (mThreadPoolProxy == null) {
            synchronized (ThreadPoolManager.class) {
                if (mThreadPoolProxy == null) {
                    int proccessorCount = Runtime.getRuntime().availableProcessors();
                    int maxAvailable = Math.max(proccessorCount * 5, 10);

                    mThreadPoolProxy = new ThreadPoolProxy(proccessorCount, maxAvailable, 15 * 1000);
                }
            }
        }
        return mThreadPoolProxy;
    }


    public static class ThreadPoolProxy {

        private ThreadPoolExecutor mThreadPoolExecutor;

//        private ExecutorService mExecutorService;

        //线程池中核心线程数
        private int corePoolSize;
        //线程池中最大并发数，若并发数高于该数，后面的任务则会等待
        private int maximumPoolSize;
        //超出核心线程数的线程在执行完后保持alive的时长
        private int keepAliveTime;

        public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, int keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        public void  initThreadPoolExecutor(){
            if (mThreadPoolExecutor == null || mThreadPoolExecutor.isShutdown()||mThreadPoolExecutor.isTerminated()) {
                synchronized (ThreadPoolManager.class) {
                    if (mThreadPoolExecutor == null || mThreadPoolExecutor.isShutdown()) {
                        mThreadPoolExecutor = createExecutor();
                        mThreadPoolExecutor.allowCoreThreadTimeOut(false);  //核心线程始终不会消失
                    }
                }
            }
        }


        /**
         * 提交任务
         */
        public Future<?> submit(Runnable runnable){
            initThreadPoolExecutor();
            Future<?> submitResult = mThreadPoolExecutor.submit(runnable);
            return submitResult;
        }


        /**
         * 执行任务
         */
        public void execute(Runnable runnable) {
            if (runnable == null) {
                return;
            } else {
                initThreadPoolExecutor();
                mThreadPoolExecutor.execute(runnable);
            }
        }

        /**
         * 移除任务
         */
        public void remove(Runnable runnable) {
            initThreadPoolExecutor();
            mThreadPoolExecutor.remove(runnable);
        }


        public ThreadPoolExecutor createExecutor() {
            return new ThreadPoolExecutor(corePoolSize,
                    maximumPoolSize, keepAliveTime,
                    TimeUnit.MILLISECONDS, //单位毫秒
                    new LinkedBlockingQueue<Runnable>(), //工作队列
                    new DefaultThreadFactory(Thread.NORM_PRIORITY, "fanson-pool-"),
                    new ThreadPoolExecutor.AbortPolicy());
        }

        /**
         * 创建线程的工厂，设置线程的优先级，group，以及命名
         */
        private static class DefaultThreadFactory implements ThreadFactory {

            private static final AtomicInteger poolNumber = new AtomicInteger(1); //线程池的计数

            private static AtomicInteger threadNumber = new AtomicInteger(1); //线程的计数

            private final ThreadGroup group;
            private final String namePrefix;
            private final int threadPriority;


            public DefaultThreadFactory(int threadPriority, String threadNamePrefix) {
                this.threadPriority = threadPriority;
                this.group = Thread.currentThread().getThreadGroup();
                this.namePrefix = threadNamePrefix + poolNumber.getAndIncrement() + "-thread-";

            }

            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(group, runnable, namePrefix + threadNumber.getAndIncrement(), 0);
                if (thread.isDaemon()) {
                    thread.setDaemon(false);
                }
                thread.setPriority(threadPriority);
                return thread;
            }
        }
    }
}

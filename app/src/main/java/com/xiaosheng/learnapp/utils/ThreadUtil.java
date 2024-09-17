package com.xiaosheng.learnapp.utils;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadUtil {

    private static final String TAG = "ThreadUtil";

    // Singleton instance of ThreadPoolExecutor
    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            5, // core pool size
            10, // maximum pool size
            60, // idle time for excess threads
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(20), // task queue size
            new ThreadPoolExecutor.CallerRunsPolicy() // rejection policy
    );

    // Submits a task with a task name that returns a result
    public static Future<?> submitTask(Runnable r, String taskName) {
        Log.i(TAG, "Submitting task: " + taskName);
        return threadPoolExecutor.submit(wrapTaskWithLogging(r, taskName));
    }

    // Executes a task with a task name without expecting a result
    public static void executeTask(Runnable r, String taskName) {
        Log.i(TAG, "Executing task: " + taskName);
        threadPoolExecutor.execute(wrapTaskWithLogging(r, taskName));
    }

    // Wraps the task with logging functionality
    private static Runnable wrapTaskWithLogging(final Runnable r, final String taskName) {
        return new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Task started: " + taskName);
                try {
                    r.run();
                } finally {
                    Log.i(TAG, "Task completed: " + taskName);
                }
            }
        };
    }

    // Shut down the thread pool gracefully
    public static void shutdownThreadPool() {
        if (!threadPoolExecutor.isShutdown()) {
            Log.i(TAG, "Shutting down thread pool");
            threadPoolExecutor.shutdown();
        }
    }

    // Forcefully shut down the thread pool
    public static void shutdownNow() {
        if (!threadPoolExecutor.isShutdown()) {
            Log.i(TAG, "Forcefully shutting down thread pool");
            threadPoolExecutor.shutdownNow();
        }
    }

    // Check if the thread pool is shut down
    public static boolean isShutdown() {
        return threadPoolExecutor.isShutdown();
    }

    // Get the active thread count
    public static int getActiveCount() {
        return threadPoolExecutor.getActiveCount();
    }

    // Get the task queue size
    public static int getQueueSize() {
        return threadPoolExecutor.getQueue().size();
    }

    // Check if the thread pool is terminated
    public static boolean isTerminated() {
        return threadPoolExecutor.isTerminated();
    }

    // Wait for the thread pool to terminate
    public static void awaitTermination() throws InterruptedException {
        threadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS);
    }
}

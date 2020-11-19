package com.dangbinh.dinter.utils;

import android.app.Activity;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaskRunner {
    public static <T> T run(Callable<T> task) {
        T result = null;
        try {
            ExecutorService service = Executors.newCachedThreadPool();
            Future<T> future = service.submit(task);
            result = future.get();
            service.shutdown();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <T> List<T> parallel(Callable<T>... tasks) throws InterruptedException {
        ExecutorService EXEC = Executors.newCachedThreadPool();
        List<Callable<T>> taskList = Arrays.asList(tasks);

        List<Future<T>> result = EXEC.invokeAll(taskList);
        Function<Future<T>, T> transform = tFuture -> {
            try {
                return tFuture.get();
            } catch (ExecutionException | InterruptedException e) {
                return null;
            }
        };
        return result.stream().map(transform).collect(Collectors.toList());
    }

    public static void runOnUiThread(Activity activity, Runnable task) {
        activity.runOnUiThread(task);
    }

    public static void setTimeOut(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

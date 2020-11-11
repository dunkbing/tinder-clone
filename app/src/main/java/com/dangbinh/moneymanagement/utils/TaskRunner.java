package com.dangbinh.moneymanagement.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskRunner<T> {
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
}

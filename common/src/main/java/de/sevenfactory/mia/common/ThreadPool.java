package de.sevenfactory.mia.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by man0004t on 20/01/16.
 */
public class ThreadPool {

    private static class Holder {
        static final ExecutorService INSTANCE = Executors.newCachedThreadPool();
    }

    public static ExecutorService executor() {
        return Holder.INSTANCE;
    }
}
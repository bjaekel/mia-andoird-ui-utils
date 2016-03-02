package de.sevenfactory.mia.common;

import android.support.annotation.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public abstract class Interactor<T> {
    private Scheduler     mScheduler;
    protected FutureTask<T> mTask;

    public Interactor(Scheduler scheduler) {
        mScheduler = scheduler;
    }
    
    protected final void execute(Callable<T> call, Observable<T> observable) {
        if (isRunning()) {
            mTask.cancel(true);
        }
        mTask = new FutureTask<>(call);
        mScheduler.execute(mTask, observable, getResolution());
    }

    protected final boolean isRunning() {
        return mTask != null && !mTask.isDone();
    }

    /**
     * Implement the resolution when the call fails
     */
    @Nullable
    protected Resolution getResolution() {
        return null;
    }
}

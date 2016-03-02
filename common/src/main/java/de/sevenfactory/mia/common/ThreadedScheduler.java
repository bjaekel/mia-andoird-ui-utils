package de.sevenfactory.mia.common;

import android.os.Handler;
import android.support.annotation.UiThread;

import java.util.concurrent.FutureTask;

public class ThreadedScheduler extends SimpleScheduler {
    private Handler mHandler;
    
    @UiThread
    public ThreadedScheduler() {
        super();
        mHandler = new Handler();
    }
    
    @Override
    public <T> void execute(final FutureTask<T> task, final Observable<T> observable, final Resolution resolution) {
        ThreadPool.executor().execute(new Runnable() {
            @Override
            public void run() {
                ThreadedScheduler.super.execute(task, observable, resolution);
            }
        });
    }


    @Override
    protected <T> void onLoading(final Observable<T> observable) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ThreadedScheduler.super.onLoading(observable);
            }
        });
    }

    @Override
    protected <T> void onCompleted(final Observable<T> observable, final T model) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ThreadedScheduler.super.onCompleted(observable, model);
            }
        });
    }

    @Override
    protected <T> void onFail(final Observable<T> observable, final Failure failure) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ThreadedScheduler.super.onFail(observable, failure);
            }
        });
    }

    @Override
    protected <T> void onCancelled(final Observable<T> observable) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ThreadedScheduler.super.onCancelled(observable);
            }
        });
    }
}

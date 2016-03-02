package de.sevenfactory.mia.modules.scheduler;

import java.util.concurrent.FutureTask;

import de.sevenfactory.mia.common.Failure;
import de.sevenfactory.mia.common.Observable;
import de.sevenfactory.mia.common.Resolution;
import de.sevenfactory.mia.common.SimpleScheduler;
import de.sevenfactory.mia.common.ThreadPool;

public class MockThreadedScheduler extends SimpleScheduler {

    public MockThreadedScheduler() {
        super();
    }
    
    @Override
    public <T> void execute(final FutureTask<T> task, final Observable<T> observable, final Resolution resolution) {
        onLoading(observable);
        ThreadPool.executor().execute(new Runnable() {
            @Override
            public void run() {
                MockThreadedScheduler.super.execute(task, observable, resolution);
            }
        });
    }
    
    @Override
    protected <T> void onLoading(final Observable<T> observable) {

        MockThreadedScheduler.super.onLoading(observable);
    }
    
    @Override
    protected <T> void onCompleted(final Observable<T> observable, final T model) {
        MockThreadedScheduler.super.onCompleted(observable, model);
    }
    
    @Override
    protected <T> void onFail(final Observable<T> observable, final Failure failure) {
        MockThreadedScheduler.super.onFail(observable, failure);
    }
}

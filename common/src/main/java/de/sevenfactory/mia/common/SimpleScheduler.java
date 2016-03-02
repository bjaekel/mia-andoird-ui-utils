package de.sevenfactory.mia.common;

import java.util.concurrent.CancellationException;
import java.util.concurrent.FutureTask;

public class SimpleScheduler implements Scheduler {

    public <T> void execute(FutureTask<T> task, Observable<T> observable, Resolution resolution) {
        onLoading(observable);
        try {
            ThreadPool.executor().execute(task);
            T model = task.get();
            onCompleted(observable, model);
        } catch (InterruptedException e) {
            onFail(observable, new Failure(e, resolution));
        } catch (CancellationException e) {
            onCancelled(observable);
        } catch (Exception e) {
            onFail(observable, new Failure(e, resolution));
        }
    }

    protected <T> void onCancelled(Observable<T> observable) {
        observable.notifyCancelled();
    }
    
    protected <T> void onLoading(Observable<T> observable) {
        observable.notifyLoading();
    }
    
    protected <T> void onCompleted(Observable<T> observable, T model) {
        observable.notifyCompleted(model);
    }
    
    protected <T> void onFail(Observable<T> observable, Failure failure) {
        observable.notifyFail(failure);
    }
}

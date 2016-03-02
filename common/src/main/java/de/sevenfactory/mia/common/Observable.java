package de.sevenfactory.mia.common;

import java.util.LinkedList;
import java.util.List;

public class Observable<T> {
    private List<Observer<T>> mObservers;
    
    public synchronized void addObserver(Observer<T> observer) {
        if (mObservers == null) mObservers = new LinkedList<>();

        mObservers.add(observer);
    }

    public synchronized void removeObserver(Observer<T> observer) {
        if (mObservers == null) return;
        
        mObservers.remove(observer);
    }
    
    public synchronized void notifyLoading() {
        if (mObservers == null) return;
        
        for (Observer<T> observer : mObservers) {
            observer.onLoading();
        }
    }

    public synchronized void notifyCompleted(T model) {
        if (mObservers == null) return;

        for (Observer<T> observer : mObservers) {
            observer.onCompleted(model);
        }
    }

    public synchronized void notifyFail(Failure failure) {
        if (mObservers == null) return;

        for (Observer<T> observer : mObservers) {
            observer.onFail(failure);
        }
    }

    public synchronized void notifyCancelled() {
        if (mObservers == null) return;

        for (Observer<T> observer : mObservers) {
            observer.onCancelled();
        }
    }
    
    public interface Observer<T> {
        void onLoading();

        void onCompleted(T model);

        void onFail(Failure failure);

        void onCancelled();
    }
}

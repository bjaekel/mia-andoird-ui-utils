package de.sevenfactory.mia.common;

public abstract class SimpleObserver<T> implements Observable.Observer<T> {
    @Override
    public void onLoading() {
        
    }
    
    @Override
    public void onCompleted(T model) {
        
    }
    
    @Override
    public void onFail(Failure failure) {
        
    }

    @Override
    public void onCancelled() {

    }
}

package de.sevenfactory.mia.modules.loading;

import de.sevenfactory.mia.common.Failure;
import de.sevenfactory.mia.common.Observable;
import de.sevenfactory.mia.common.Presenter;

@SuppressWarnings({"unchecked", "rawtypes"})
        // This presenter expects observer of different types
class LoadingPresenter implements Presenter, Observable.Observer {
    
    private Observable[] mObservables;
    private int          mCounter;
    private LoadingUi    mUi;
    
    LoadingPresenter(Observable... observables) {
        mObservables = observables;
    }
    
    void setUi(LoadingUi ui) {
        mUi = ui;
    }

    /* Presenter */
    @Override
    public void register() {
        for (Observable observable : mObservables) {
            observable.addObserver(this);
        }
    }
    
    @Override
    public void unregister() {
        for (Observable observable : mObservables) {
            observable.removeObserver(this);
        }
    }
    
    @Override
    public void update() {
        mCounter = 0;
    }

    /* Observable */
    @Override
    public void onLoading() {
        observableStarted();
    }
    
    @Override
    public void onCompleted(Object model) {
        observableFinished();
    }
    
    @Override
    public void onFail(Failure failure) {
        observableFinished();
    }

    @Override
    public void onCancelled() {
        observableFinished();
    }

    private void observableStarted() {
        mCounter++;
        if (mUi != null) {
            mUi.setLoading(true);
        }
    }
    
    private void observableFinished() {
        mCounter--;
        if (mUi != null) {
            mUi.setLoading(mCounter != 0);
        }
    }
}

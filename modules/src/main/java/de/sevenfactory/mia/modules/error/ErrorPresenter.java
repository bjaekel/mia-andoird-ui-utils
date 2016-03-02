package de.sevenfactory.mia.modules.error;

import de.sevenfactory.mia.common.Failure;
import de.sevenfactory.mia.common.Observable;
import de.sevenfactory.mia.common.Presenter;
import de.sevenfactory.mia.common.SimpleObserver;

@SuppressWarnings({"unchecked", "rawtypes"}) // This presenter must be able to handle observables of several types.
class ErrorPresenter extends SimpleObserver implements Presenter {
    private Observable[] mObservables;
    private ErrorUi           mUi;

    ErrorPresenter(Observable... observables) {
        mObservables = observables;
    }

    void setUi(ErrorUi ui) {
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

        mUi.dismissFailure();
    }

    @Override
    public void update() {
    }

    /* Observer */

    @Override
    public void onFail(Failure failure) {
        mUi.showFailure(failure);
    }
}
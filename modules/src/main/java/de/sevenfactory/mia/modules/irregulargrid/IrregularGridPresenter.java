package de.sevenfactory.mia.modules.irregulargrid;

import java.util.List;

import de.sevenfactory.mia.common.Observable;
import de.sevenfactory.mia.common.Presenter;
import de.sevenfactory.mia.common.SimpleObserver;

public class IrregularGridPresenter<T> extends SimpleObserver<List<T>> implements Presenter {

    private final IrregularGridInteractor<T>              mInteractor;
    private final Observable<List<T>> mObservable;
    private final IrregularGridUi<T>                      mUi;

    public IrregularGridPresenter(IrregularGridInteractor<T> interactor, Observable<List<T>> observable, IrregularGridUi<T> ui) {
        mInteractor = interactor;
        mObservable = observable;
        mUi = ui;
    }
    /* Presenter */

    @Override
    public void register() {
        mObservable.addObserver(this);
    }

    @Override
    public void unregister() {
        mObservable.removeObserver(this);
    }

    @Override
    public void update() {
        mInteractor.fetchItems();
    }

    /* Observable */

    @Override
    public void onCompleted(List<T> items) {
        mUi.setItems(items);
    }
}

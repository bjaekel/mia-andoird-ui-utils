package de.sevenfactory.mia.modules.irregulargrid;

import java.util.LinkedList;
import java.util.List;

import de.sevenfactory.mia.common.Interactor;
import de.sevenfactory.mia.common.Observable;
import de.sevenfactory.mia.common.Resolution;
import de.sevenfactory.mia.common.Scheduler;

import java.util.concurrent.Callable;

import de.sevenfactory.mia.modules.paging.PageModel;

public abstract class IrregularGridInteractor<T> extends Interactor<List<T>> {

    public static final int FEED_ITEMS = 7; // 6 items plus one to know whether there is "more"

    private Observable<List<T>> mObservable;
    private Retry                                mRetry;

    public IrregularGridInteractor(Scheduler scheduler, Observable<List<T>> observable) {
        super(scheduler);
        mObservable = observable;
    }

    public void fetchItems() {
        Callable<List<T>> call = new FetchItemsCall();
        mRetry = new Retry(call);
        execute(call, mObservable);
    }

    @Override
    protected Resolution getResolution() {
        return mRetry;
    }

    /* FetchItemsTask */

    private class FetchItemsCall implements Callable<List<T>> {
        @Override
        public List<T> call() throws Exception {
            PageModel<? extends T> page;
            page = fetchPage(FEED_ITEMS);

            return new LinkedList<>(page.getItems());
        }
    }

    protected abstract PageModel<? extends T> fetchPage(int limit) throws Exception;

    /* Retry */

    private class Retry implements Resolution {

        private final Callable<List<T>> mCall;

        public Retry(Callable<List<T>> call) {
            mCall = call;
        }

        @Override
        public void resolve() {
            retry(mCall);
        }

        @Override
        public int getType() {
            return Resolution.RETRY;
        }
    }

    private void retry(Callable<List<T>> call) {
        execute(call, mObservable);
    }
}

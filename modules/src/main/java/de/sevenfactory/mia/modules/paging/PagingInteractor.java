package de.sevenfactory.mia.modules.paging;

import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import de.sevenfactory.mia.common.Failure;
import de.sevenfactory.mia.common.Interactor;
import de.sevenfactory.mia.common.Observable;
import de.sevenfactory.mia.common.Resolution;
import de.sevenfactory.mia.common.Scheduler;

import de.sevenfactory.mia.common.ThreadedScheduler;

public abstract class PagingInteractor<T> extends Interactor<List<T>> implements Observable.Observer<List<T>> {
    private Observable<List<T>> mObservable;
    
    private List<T> mEntries;
    private int     mTotalCount;
    private int     mLimit;
    private Retry   mRetry;
    
    public PagingInteractor(Observable<List<T>> observable, int limit) {
        this(new ThreadedScheduler(), observable, limit);
    }

    @VisibleForTesting
    protected PagingInteractor(Scheduler scheduler, Observable<List<T>> observable, int limit) {
        super(scheduler);
        
        mObservable = observable;
        observable.addObserver(this);
        
        mLimit = limit;
        
        mEntries = new ArrayList<>();
        mTotalCount = 1; // Assume at least one item
    }
    
    /* Interaction */
    public void fetchFirstPage() {
        FetchPageCall call = new FetchPageCall(0, mEntries);
        mRetry = new Retry(call);
        execute(call, mObservable);
    }
    
    public void fetchNextPage() {
        if (!isRunning() && mEntries.size() < mTotalCount) {
            FetchPageCall call = new FetchPageCall(mEntries.size(), mEntries);
            mRetry = new Retry(call);
            execute(call, mObservable);
        }
    }

    private void retry(Callable<List<T>> call) {
        execute(call, mObservable);
    }
    
    @Override
    protected Resolution getResolution() {
        return mRetry;
    }
    
    /* Observer */
    
    @Override
    public void onLoading() {
    }
    
    @Override
    public void onCompleted(List<T> list) {
        mEntries = list;
    }
    
    @Override
    public void onFail(Failure failure) {
    }

    @Override
    public void onCancelled() {
    }

    /* Task */
    
    private class FetchPageCall implements Callable<List<T>> {
        private final int     mOffset;
        private final List<T> mEntries;

        public FetchPageCall(int offset, List<T> entries) {
            mOffset = offset;
            mEntries = new ArrayList<>(entries.size());
            mEntries.addAll(entries);
        }


        @Override
        public List<T> call() throws Exception {
            PageModel<? extends T> page = fetchPage(mOffset, mLimit);
            if (mOffset == 0) {
                mEntries.clear();
            }
            mEntries.addAll(page.getItems());
            mTotalCount = page.getTotalCount();
            return mEntries;
        }
    }
    
    protected abstract PageModel<? extends T> fetchPage(int skipItems, int limit) throws Exception;
    
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
}
package de.sevenfactory.mia.modules.paging;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import de.sevenfactory.mia.common.Failure;
import de.sevenfactory.mia.common.Observable;
import de.sevenfactory.mia.common.Scheduler;
import de.sevenfactory.mia.common.SimpleObserver;
import de.sevenfactory.mia.common.SimpleScheduler;
import de.sevenfactory.mia.modules.scheduler.MockThreadedScheduler;

public class TestPagingInteractor {
    private static final int PAGE_SIZE   = 5;
    private static final int MAX_TIMEOUT = 10000;

    private Observable<List<String>> mObservable;
    private PagingInteractor<String> mPagingInteractor;

    @Before
    public void setUp() {
        mObservable = new Observable<>();
    }

    /**
     * This test will try to fetch the first page 3 times (20ms wait in between fetch calls). There
     * is an artificial delay of 500 ms when calling {@link de.prosiebensat1digital.modules.paging.TestPagingInteractor.TestablePagingInteractor#fetchPage(int, int)}
     * so all 3 fetch attempts will be started before the first call returns.
     * <p/>
     * The intended behaviour is for the first two tasks to be cancelled and only the last one to go through.
     */
    @Test
    public synchronized void testRepeatedFetch() {
        // ThreadedScheduler must be used to test concurrency behaviour
        mPagingInteractor = new TestablePagingInteractor(new MockThreadedScheduler(), mObservable, PAGE_SIZE, 500);

        TestObserver observer = new TestObserver();

        mObservable.addObserver(observer);

        mPagingInteractor.fetchFirstPage();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            // This is expected
        }
        mPagingInteractor.fetchFirstPage();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            // This is expected
        }
        mPagingInteractor.fetchFirstPage();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            // This is expected
        }

        try {
            wait(MAX_TIMEOUT);
        } catch (InterruptedException e) {
            // First fetch went through without an exception
        }

        // Make sure only the last fetch went through and the others were cancelled
        Assert.assertEquals(1, observer.getCounter());
    }

    private synchronized void resumeTests() {
        notify();
    }

    @Test
    public void testPaging() {
        mPagingInteractor = new TestablePagingInteractor(new SimpleScheduler(), mObservable, PAGE_SIZE, 0);

        mPagingInteractor.fetchFirstPage();
        mPagingInteractor.fetchNextPage();

        mObservable.addObserver(new Observable.Observer<List<String>>() {
            @Override
            public void onLoading() {
                // do nothing
            }

            @Override
            public void onCompleted(List<String> model) {
                Assert.assertEquals(3 * PAGE_SIZE, model.size());
            }

            @Override
            public void onFail(Failure failure) {
                throw new RuntimeException(failure.getException());
            }

            @Override
            public void onCancelled() {

            }
        });
        mPagingInteractor.fetchNextPage();
    }

    private static class TestablePagingInteractor extends PagingInteractor<String> {
        private static final int SIZE = 20;
        private int mDelay;

        private List<String> mData; // For testing purposes, no repository is required here

        public TestablePagingInteractor(Scheduler scheduler, Observable<List<String>> observable, int limit, int delay) {
            super(scheduler, observable, limit);
            mDelay = delay;
            mData = new ArrayList<>(SIZE);
            for (int i = 0; i < SIZE; i++) {
                mData.add(Integer.toString(i));
            }
        }

        @Override
        protected PageModel<? extends String> fetchPage(int skipItems, int limit) throws Exception {
            List<String> subList = mData.subList(skipItems, skipItems + limit);
            if (mDelay > 0) {
                try {
                    Thread.sleep(mDelay);
                } catch (InterruptedException e) {
                    // Just continue if interrupted
                }
            }
            return new MockPage<>(mData.size(), subList);
        }
    }

    private class TestObserver extends SimpleObserver<List<String>> {
        private int mCounter = 0;

        @Override
        public void onLoading() {

        }

        @Override
        public void onCompleted(List<String> model) {
            mCounter++;
            // The next assertion will not fail the test as this code is running on another
            // thread but the console output might be helpful when debugging
            Assert.assertEquals(PAGE_SIZE, model.size());

            resumeTests();
        }

        @Override
        public void onFail(Failure failure) {
            throw new RuntimeException(failure.getException());
        }

        public int getCounter() {
            return mCounter;
        }

    }
}

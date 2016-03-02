package de.sevenfactory.mia.common;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import de.sevenfactory.mia.common.Failure;
import de.sevenfactory.mia.common.Observable;

public class TestObservable {

    private static final String EXCEPTION_MESSAGE = "exception";

    private Observable<Object> mObservable;

    @Before
    public void setUp() {
        mObservable = new Observable<Object>();
    }

    @Test
    public void testObservableLoading() {
        mObservable.addObserver(new Observable.Observer<Object>() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onCompleted(Object model) {
                Assert.fail();
            }

            @Override
            public void onFail(Failure failure) {
                Assert.fail();
            }

            @Override
            public void onCancelled() {

            }
        });

        mObservable.notifyLoading();
    }

    @Test
    public void testObservableCompleted() {
        mObservable.addObserver(new Observable.Observer<Object>() {
            @Override
            public void onLoading() {
                Assert.fail();
            }

            @Override
            public void onCompleted(Object model) {
                Assert.assertNotNull(model);
            }

            @Override
            public void onFail(Failure failure) {
                Assert.fail();
            }

            @Override
            public void onCancelled() {

            }
        });

        mObservable.notifyCompleted(new Object());
    }

    @Test
    public void testObservableFail() {
        mObservable.addObserver(new Observable.Observer<Object>() {
            @Override
            public void onLoading() {
                Assert.fail();
            }

            @Override
            public void onCompleted(Object model) {
                Assert.fail();
            }

            @Override
            public void onFail(Failure failure) {
                Assert.assertEquals(EXCEPTION_MESSAGE, failure.getException().getMessage());
            }

            @Override
            public void onCancelled() {

            }
        });

        mObservable.notifyFail(new Failure(new Exception(EXCEPTION_MESSAGE)));
    }
}

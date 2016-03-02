package de.sevenfactory.mia.modules.loading;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import de.sevenfactory.mia.common.Observable;

public class TestLoadingPresenter {

    private LoadingPresenter   mLoadingPresenter;
    private Observable<Object> mObservable;

    // Loading counter
    @Before
    public void setUp() {
        mObservable = new Observable<>();
        mLoadingPresenter = new LoadingPresenter(mObservable);

        mLoadingPresenter.register();
    }

    @Test
    public void testShowLoading() {
        mLoadingPresenter.setUi(new LoadingUi() {
            @Override
            public void setLoading(boolean isLoading) {
                Assert.assertTrue(isLoading);
            }
        });
        mObservable.notifyLoading();
    }

    @Test
    public void testShowLoadingWithMultipleNotifications() {
        mObservable.notifyLoading();
        mObservable.notifyLoading();
        mObservable.notifyLoading();
        mObservable.notifyLoading();
        mObservable.notifyLoading();
        mObservable.notifyCompleted(null);
        mObservable.notifyCompleted(null);
        mObservable.notifyCompleted(null);
        mObservable.notifyCompleted(null);
        mObservable.notifyCompleted(null);

        mLoadingPresenter.setUi(new LoadingUi() {
            @Override
            public void setLoading(boolean isLoading) {
                Assert.assertTrue(isLoading);
            }
        });
        mObservable.notifyLoading();
    }

    @Test
    public void testHideLoading() {
        mObservable.notifyLoading();
        mLoadingPresenter.setUi(new LoadingUi() {
            @Override
            public void setLoading(boolean isLoading) {
                Assert.assertFalse(isLoading);
            }
        });
        mObservable.notifyCompleted(null);
    }

    @Test
    public void testHideLoadingWithMultipleNotifications() {
        mObservable.notifyLoading();
        mObservable.notifyLoading();
        mObservable.notifyLoading();
        mObservable.notifyLoading();
        mObservable.notifyLoading();
        mObservable.notifyCompleted(null);
        mObservable.notifyCompleted(null);
        mObservable.notifyCompleted(null);
        mObservable.notifyCompleted(null);

        mLoadingPresenter.setUi(new LoadingUi() {
            @Override
            public void setLoading(boolean isLoading) {
                Assert.assertFalse(isLoading);
            }
        });
        mObservable.notifyCompleted(null);
    }
}

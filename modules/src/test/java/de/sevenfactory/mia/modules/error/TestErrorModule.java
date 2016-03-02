package de.sevenfactory.mia.modules.error;

import android.test.mock.MockContext;
import android.view.View;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import de.sevenfactory.mia.common.Observable;
import de.sevenfactory.mia.common.Failure;
import de.sevenfactory.mia.common.Resolution;

public class TestErrorModule {

    private ErrorModule mErrorModule;

    @Before
    public void setUp() {
        View             view       = new View(new MockContext());
        Observable<Void> observable = new Observable<>();
        mErrorModule = new ErrorModule(view, observable);
        mErrorModule.onStart();
    }

    @Test
    public void testShouldRetryWithResolution() {
        mErrorModule.addFailure(new Failure(new Exception()) {
            @Override
            public boolean hasResolution() {
                return true;
            }

            @Override
            public int getResolutionType() {
                return Resolution.RETRY;
            }
        });
        Assert.assertTrue(mErrorModule.shouldRetry());
    }

    @Test
    public void testShouldRetryWithoutResolution() {
        mErrorModule.addFailure(new Failure(new Exception()));
        Assert.assertFalse(mErrorModule.shouldRetry());
    }
}

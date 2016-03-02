package de.sevenfactory.mia.common;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import de.sevenfactory.mia.common.Failure;
import de.sevenfactory.mia.common.Observable;
import de.sevenfactory.mia.common.SimpleObserver;
import de.sevenfactory.mia.common.SimpleScheduler;

public class TestSimpleScheduler {

    public static final String MESSAGE = "Error";
    private SimpleScheduler  mScheduler;
    private FutureTask<Void> mSuccessfulTask;
    private FutureTask<Void> mFailingTask;
    private Observable<Void> mObservable;
    private Observable<Void> mFailingObservable;

    @Before
    public void setUp() {
        mScheduler = new SimpleScheduler();

        /* Successful task */

        Callable<Void> call = new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                Thread.sleep(1000);

                return null;
            }
        };

        mObservable = new Observable<>();
        mObservable.addObserver(new SimpleObserver<Void>() {
            @Override
            public void onFail(Failure failure) {
                Assert.fail();
            }
        });
        mSuccessfulTask = new FutureTask<>(call);

        /* Failing successful task */

        call = new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                Thread.sleep(1000);
                throw new Exception(MESSAGE);
            }
        };
        mFailingTask = new FutureTask<>(call);

        mFailingObservable = new Observable<>();
        mFailingObservable.addObserver(new SimpleObserver<Void>() {
            @Override
            public void onCompleted(Void model) {
                Assert.fail();
            }

            @Override
            public void onFail(Failure failure) {
                Assert.assertEquals(failure.getException().getCause().getMessage(), MESSAGE);
            }
        });
    }

    @Test
    public void testExecute() {
        mScheduler.execute(mSuccessfulTask, mObservable, null);
    }

    @Test
    public void testExecuteFail() {
        mScheduler.execute(mFailingTask, mFailingObservable, null);
    }
}

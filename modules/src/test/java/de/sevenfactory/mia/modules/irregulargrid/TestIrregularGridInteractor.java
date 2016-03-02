package de.prosiebensat1digital.modules.irregulargrid;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import de.sevenfactory.mia.common.Failure;
import de.sevenfactory.mia.common.Observable;
import de.sevenfactory.mia.common.SimpleScheduler;
import de.sevenfactory.mia.modules.paging.PageModel;

public class TestIrregularGridInteractor implements Observable.Observer<List<IrregularGridModel>> {

    private IrregularGridInteractor mInteractor;

    @Before
    public void setUp() {
        Observable<List<IrregularGridModel>> observable = new Observable<>();
        observable.addObserver(this);

        final MockIrregularGridRepository mockIrregularGridRepository = new MockIrregularGridRepository();
        mInteractor = new IrregularGridInteractor(new SimpleScheduler(), observable) {
            @Override
            protected PageModel<? extends IrregularGridModel> fetchPage(int limit) {
                return mockIrregularGridRepository.fetchFeedItems(limit);
            }
        };
    }

    @Test
    public void testGetFeed() {
        mInteractor.fetchItems();
    }

    @Override
    public void onLoading() {
    }

    @Override
    public void onCompleted(List<IrregularGridModel> model) {
        Assert.assertNotNull(model);
        Assert.assertFalse(model.isEmpty());
    }

    @Override
    public void onFail(Failure failure) {
        Assert.assertTrue(failure.hasResolution());
        failure.resolve();
    }

    @Override
    public void onCancelled() {

    }
}

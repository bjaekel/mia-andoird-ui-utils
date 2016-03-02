package de.sevenfactory.mia.common;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import de.sevenfactory.mia.common.Failure;
import de.sevenfactory.mia.common.Resolution;

public class TestFailure {

    @Before
    public void setUp() {
    }

    @Test
    public void testFailure() {
        Failure failure = new Failure(new Exception());

        Assert.assertFalse(failure.hasResolution());
    }


    @Test
    public void testFailureWithRetry() {
        Failure failure = new Failure(new Exception(), new Resolution() {
            @Override
            public void resolve() {
            }

            @Override
            public int getType() {
                return Resolution.RETRY;
            }
        });

        Assert.assertTrue(failure.hasResolution());
        Assert.assertEquals(failure.getResolutionType(), Resolution.RETRY);

        failure.resolve();
    }


}

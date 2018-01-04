package com.spotapps.beans;

import static junit.framework.Assert.fail;

/**
 * Created by tty on 15/12/2016.
 */

public class SpotMergeOperationTest extends junit.framework.TestCase {

    public void testFailure() throws Exception {
        fail();
    }

    public void testExecute(){
        testSingleExecute(6, 10, 6, 10, 2, 2, 2, 2, 6, 10);
        testSingleExecute(6, 10, 6, 10, 12, 12, 6, 10, 12, 12);
        testSingleExecute(6, 10, 6, 10, 8, 8, 6, 8, 8, 10);
        testSingleExecute(6, 14, 8, 16, 7, 12, 6, 12, 8, 16);
    }

    private void testSingleExecute(double minLat, double minLong,
                                   double maxLat, double maxLong,
                                   double updatedLat, double updatedLong,
                                   double resMinLat, double resMinLong,
                                   double resMaxLat, double resMaxLong) {

        SpotLocation loc = new SpotLocation(minLat, minLong, maxLat, maxLong);
        SpotMergeOperation op = new SpotMergeOperation(loc, updatedLat, updatedLong);
        SpotLocation updatedLoc = op.execute();
        SpotLocation resultLoc = new SpotLocation( resMinLat, resMinLong, resMaxLat, resMaxLong);
        assertTrue(updatedLoc.equals(resultLoc));
    }
}

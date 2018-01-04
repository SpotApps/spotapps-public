package com.spotapps.beans;

import android.os.Bundle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tty on 9/9/2015.
 */
public class SpotBundleUtils {
    private static final String SPOT = "SPOT";
    private static final String SINGLE_SPOT = "SINGLE_SPOT";

    public static SpotBundle loadSpotsFromBundle(Bundle bundle){
        return (SpotBundle)bundle.getSerializable(SPOT);
    }

    public static void storeSpotsInBundle(Bundle bundle, SpotBundle spots){
        bundle.putSerializable(SPOT, spots);
    }


//    public static Spot loadSingleSpotFromBundle(Bundle bundle){
//        return (Spot)bundle.getSerializable(SINGLE_SPOT);
//    }
//    public static void storeSingleSpotInBundle(Bundle bundle, Spot spot) {
//        bundle.putSerializable(SINGLE_SPOT, spot);
//    }
}

package com.spotapps.beans;

import android.os.Bundle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tty on 9/9/2015.
 */
public class SpotBundleUtils {
    private static final String SPOT = "SPOT";
    public static List<Spot> loadSpotsFromBundle(Bundle bundle){
        return (List<Spot>)bundle.getSerializable(SPOT);
    }

    public static void storeSpotsInBundle(Bundle bundle, List<Spot> spots){
        bundle.putSerializable(SPOT, (Serializable)spots);
    }

}

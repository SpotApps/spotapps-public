package com.spotapps.server;

import android.support.annotation.NonNull;

import com.spotapps.beans.Spot;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by tty on 5/31/2016.
 */
public class SpotRepository {
    Set repo = new TreeSet<Spot>();

    public void addSpot(Spot spot) {
        repo.add(spot);
    }
}

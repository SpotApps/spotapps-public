package com.spotapps.beans;

import com.spotapps.sensors.SensorsFacade;

/**
 * Created by tty on 4/6/2015.
 */
public abstract class SpotFactory {



    public static Spot createSpot(String name, SpotLocation location) {
        return new DefaultSpot(name, location);
    }

    public static SpotKey createSpotKey(){
        return new DefaultSpotKey(SensorsFacade.getCurrentLocation());
    }
}

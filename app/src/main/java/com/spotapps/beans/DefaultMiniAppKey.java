package com.spotapps.beans;

/**
 * Created by tty on 7/20/2016.
 */
public class DefaultMiniAppKey implements MiniAppKey{
    Spot spot;

    public DefaultMiniAppKey(Spot spot) {
        this.spot = spot;
    }

    @Override
    public Spot getSpot() {

        return spot;
    }
}

package com.spotapps.beans;

import java.util.List;

/**
 * Created by tty on 28/12/2016.
 */

public class DefaultFullSpot extends DefaultSpot implements FullSpot {

    public DefaultFullSpot(Spot spot, List<MiniApp> miniApps){
        this(spot.getId(), spot.getName(), spot.getLocation(), spot.getType(), miniApps);
    }
    public DefaultFullSpot(String id, String name, SpotLocation location, String type, List<MiniApp> miniApps){
        super(id, name, location, type);
        this.miniApps = miniApps;
    }
    private List<MiniApp> miniApps;

    @Override
    public List<MiniApp> getMiniApps() {
        return this.miniApps;
    }
}

package com.spotapps.beans;

import android.location.Location;

import java.util.List;

/**
 * Created by tty on 4/6/2015.
 */
public abstract class SpotFactory {



    public static Spot createSpot(String id, String name, SpotLocation spotLocation, String type) {
        return new DefaultSpot(id, name, spotLocation, type);
    }

    public static Spot createNewSpot(String id, String name, double currentLatitude, double currentLongitude, String type) {
        SpotLocation spotLocation = new SpotLocation(currentLatitude, currentLongitude, currentLatitude, currentLongitude);
        return createSpot(id, name, spotLocation, type);
    }

    public static FullSpot createNewFullSpot(Spot spot, List<MiniApp> miniApps){
        return new DefaultFullSpot(spot, miniApps);
    }

    public static SpotKey createSpotKey(Location location){
        return new DefaultSpotKey(location);
    }

    public static SpotKey createSpotKey(double latitude, double longitude){
        return new MockSpotKey(latitude, longitude);
    }

    // TODO TALYAC - you should also update the old location to fix it.
    // TODO TALYAC - should not be here.Not relevant to a factory
    public static Spot updateSpotLocation(Spot spot, double updatedLatitude, double updatedLongitude) {

        SpotLocation currentLocation = spot.getLocation();
        SpotMergeOperation op = new SpotMergeOperation(currentLocation, updatedLatitude, updatedLongitude);
        SpotLocation location = op.execute();

        return new DefaultSpot(spot.getId(), spot.getName(), location, spot.getType());

    }

    public static boolean isValidSpot(Spot spot) {
        if (spot.getName() == null || spot.getName().isEmpty() )
            return false;
        if (spot.getType() == null || spot.getType().isEmpty())
            return false;
        return true;
    }


    private static class MockSpotKey implements SpotKey {
        private double latitude;
        private double longitude;

        public MockSpotKey(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        public double getCurrentLatitude() {
            return latitude;
        }

        @Override
        public double getCurrentLongitude() {
            return longitude;
        }
    }
}

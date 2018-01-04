package com.spotapps.server;

import android.content.Context;
import android.support.annotation.NonNull;

import com.spotapps.beans.MiniApp;
import com.spotapps.beans.MiniAppFactory;
import com.spotapps.beans.Spot;
import com.spotapps.beans.SpotLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tty on 4/7/2015.
 */
public class ServerFacade {



    // TODO TALYAC this should eventually work with paging
    public static List<MiniApp> loadMiniApps(Spot spot) {
        // If you reach here there is a spot that is not null
        List<MiniApp> list = getMockMiniApps(null, spot);

        return list;
    }

    @NonNull
    private static List<MiniApp> getMockMiniApps(Context context, Spot spot) {
        List<MiniApp> list = new ArrayList<MiniApp>();
        list.add(0, MiniAppFactory.createMiniApp(context, "01", "View Room Service Menu", "description", "web_link.png", "openUrl", "www.hotel.com", spot, 0, 0));
//
//        list.add(1, MiniAppFactory.createUrlMiniApp("web_link.png", "Call Front Desk", "openPhoneApp", "972542202500", spot));
//        list.add(2, MiniAppFactory.createUrlMiniApp("web_link.png", "Car Rental Coupons", "openUrl", "www.getTaxi.com", spot));
//        list.add(3, MiniAppFactory.createUrlMiniApp("web_link.png", "Book Spa Treatment", "openUrl", "www.hotel.com/spa", spot));
//        for(int i=4; i<5 ; i++) {
//            String icon = "web_link.png";
//            String description = "describes miniApp " + i;
//            String operation = "openUrl";
//            String params = "www.spotappsmobile.com";
//            list.add(i, MiniAppFactory.createUrlMiniApp(icon, description, operation, params, spot))
//        }
//      Very well on 25. D
        return list;
    }

    public static List<Spot> loadSpots(SpotLocation spotLocation) {

        List<Spot> spots = new ArrayList<Spot>();
//        String spotName0 = "Bet Dagan Grocery";
//        SpotLocation spotLocation0 = SensorsFacade.getCurrentLocation();
//        spots.add(0, SpotFactory.createSpot("00", spotName0, spotLocation0));
//
//        String spotName1 = "Gourmet Restaurant Kfar Habad";
//        SpotLocation spotLocation1 = SensorsFacade.getCurrentLocation();
//        spots.add(1, SpotFactory.createSpot("10",spotName1, spotLocation1));
//
//        String spotName2 = "Toys R Us Bet Dagan Mall";
//        SpotLocation spotLocation2 = SensorsFacade.getCurrentLocation();
//        spots.add(2, SpotFactory.createSpot("20",spotName2, spotLocation2));

        return spots;
    }
}

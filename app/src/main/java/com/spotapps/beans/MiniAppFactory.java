package com.spotapps.beans;

/**
 * Created by tty on 4/6/2015.
 */
public abstract class MiniAppFactory {



    public static MiniApp createUrlMiniApp(String icon, String description, String operation, String params, Spot spot) {
        return new DefaultMiniApp(icon, description, operation, params, spot);
    }
}

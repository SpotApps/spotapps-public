package com.spotapps.beans;

/**
 * Created by tty on 24/11/2016.
 */

public enum MiniAppTypes {
    // general info, faq, media (video\images), schedule, map, customer feedback, poll,
    OPEN_URL("openUrl"),
    OPEN_APP("openApp"),
    DIAL("dial");
    // chat app
    // map
    // openFacebook
    // calendar
    // poll

    public static MiniAppTypes lookup(String miniAppType){
        for(MiniAppTypes type : values()){
            if( type.miniAppType.equals(miniAppType)){
                return type;
            }
        }
        return null;
    }
    private final String miniAppType;

    MiniAppTypes(String miniAppType) {
        this.miniAppType = miniAppType;

    }

    // TODO TALYAC must be a cleaner way to do this
    public static MiniAppTypes lookupByText(String typeAsString) {
        switch (typeAsString){
            case "Dial a phone number" : return DIAL;
            case "Open an application" : return OPEN_APP;
            default : return OPEN_URL;

        }
    }

    @Override
    public String toString() {
        return miniAppType;
    }

}

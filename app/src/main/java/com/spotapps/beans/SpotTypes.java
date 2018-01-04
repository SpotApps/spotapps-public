package com.spotapps.beans;

/**
 * Created by tty on 24/11/2016.
 */

public enum SpotTypes {

    OTHER("Other"),
    RESTAURANT("Restaurant"),
    PRIVATE_HOME("Private Home"),
    PARK("Park"),
    SPORS_VENUE("Sports Venue"),
    BANK("Bank"),
    UNIVERSITY("University"),
    KINDERGARTEN("Kindergarten"),
    SCHOOL("School"),
    SHOPPING_CENTER("Shopping Center"),
    CINEMA("Cinema"),
    SUPERMARKET("Supermarket");


    public static SpotTypes lookup(String spotType){
        for(SpotTypes type : values()){
            if( type.spotType.equals(spotType)){
                return type;
            }
        }
        return null;
    }
    private final String spotType;

    SpotTypes(String spotType) {
        this.spotType = spotType;

    }
}

package com.spotapps.beans;

import java.io.Serializable;

/**
 * Created by tty on 4/5/2015.
 */
public interface Spot extends Serializable {

    String getName();

    SpotLocation getLocation();
}

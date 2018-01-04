package com.spotapps.logic;

import com.spotapps.beans.FullSpot;

/**
 * Created by tty on 28/12/2016.
 */
public class UpdateSpotResult {

        private FullSpot spot;
        private boolean wasSuccessful;

        public UpdateSpotResult(FullSpot spot, boolean wasSuccessful) {
            this.spot = spot;
            this.wasSuccessful = wasSuccessful;
        }

        public FullSpot getSpot() {
            return spot;
        }

        //public void setSpot(FullSpot spot) {
//            this.spot = spot;
//        }
}

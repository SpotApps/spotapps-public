package com.spotapps.server.miniappgenerator;

import com.spotapps.beans.SpotTypes;


/**
 * Created by tty on 27/04/2017.
 */

public class MiniAppGeneratorFactory {
    public static MiniAppGenerator createMiniAppGenerator(String type){
        SpotTypes spotType = SpotTypes.lookup(type);
        switch (spotType) {
            case BANK: return new BankMiniAppGenerator();
            case SCHOOL: return new SchoolMiniAppGenerator();
            default: return new DefaultMiniAppGenerator();
        }
    }
}

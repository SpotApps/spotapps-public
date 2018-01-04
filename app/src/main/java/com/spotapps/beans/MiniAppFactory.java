package com.spotapps.beans;

import android.content.Context;

import com.spotapps.R;

/**
 * Created by tty on 4/6/2015.
 */
public abstract class MiniAppFactory {



    public static MiniApp createMiniApp(Context context, String id, String name, String description, String icon, String operation, String params, Spot spot, Integer upVotes, Integer downVotes) {
        return new DefaultMiniApp(id, name, description, icon, operation, params, spot, upVotes, downVotes);
    }

    public static MiniApp createMiniApp(Context context, String id, String name, String icon, String operation, String params, Spot spot, Integer upVotes, Integer downVotes) {
        String description = generateUserDescription(context, name, operation, params);
        return new DefaultMiniApp(id, name, description, icon, operation, params, spot, upVotes, downVotes);
    }

    private static String generateUserDescription(Context context, String name, String operation, String params) {
        MiniAppTypes miniAppTypes = MiniAppTypes.lookup(operation);
        switch (miniAppTypes) {
            case DIAL: return context.getText(R.string.op_call).toString() + params;
            case OPEN_APP: return context.getText(R.string.op_open_app).toString() + params;
            default: return context.getText(R.string.op_open_url).toString() + params;
        }
    }

    public static MiniAppKey createMiniAppKey(Spot spot){
        return new DefaultMiniAppKey(spot);
    }
}

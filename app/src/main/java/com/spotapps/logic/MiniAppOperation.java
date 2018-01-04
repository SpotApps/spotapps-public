package com.spotapps.logic;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.spotapps.R;
import com.spotapps.beans.MiniApp;
import com.spotapps.beans.MiniAppTypes;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tty on 02/04/2017.
 */

public class MiniAppOperation {

    private MiniApp miniApp;
    private PackageManager packageManager;

    public MiniAppOperation(MiniApp miniApp, Activity contextActivity) {

        this.miniApp = miniApp;
        this.packageManager = contextActivity.getPackageManager();
    }

    public Intent createIntentByOperation() {
        String operation = miniApp.getOperation();
        MiniAppTypes miniAppTypes = MiniAppTypes.lookup(operation);
        if (miniAppTypes != null) {
            switch (miniAppTypes) {
                case OPEN_URL:
                    String url = miniApp.getParams();
                    // TODO TALYAC the uri must always have http://
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toLowerCase()));
                    return browserIntent;

                case DIAL:
                    String number = miniApp.getParams();

                    Uri uri = Uri.parse("tel:" + number);
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, uri);
                    return callIntent;

                case OPEN_APP:
                    String appName = miniApp.getParams();
                    Intent intentMatchingAppName = findIntentMatchingAppName(appName); // find returns null if none
                    if (intentMatchingAppName != null) {
                        return intentMatchingAppName;
                    } else {
                        // app store link
                        // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                        return new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appName));
                    }

                //            // check that the app exists
                //            PackageManager packageManager = getPackageManager();
                //            List activities = packageManager.queryIntentActivities(appIntent,
                //                    PackageManager.MATCH_DEFAULT_ONLY);
                //            boolean isIntentSafe = activities.size() > 0;
                //            if (isIntentSafe){
                //                startActivity(appIntent);
                //            }
            }
        }
        return null;
    }

    private Intent findIntentMatchingAppName(String appName) {

        ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
        List<PackageInfo> packs = packageManager.getInstalledPackages(0);
        for (PackageInfo pi : packs) {
            if (pi.packageName.toString().toLowerCase().contains(appName.toLowerCase())) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("appName", pi.applicationInfo.loadLabel(packageManager));
                map.put("packageName", pi.packageName);
                items.add(map);
            }
        }
        if (items.size() >= 1) {
            String packageName = (String) items.get(0).get("packageName");

            Intent appIntent = packageManager.getLaunchIntentForPackage(packageName);
            if (appIntent != null) {
                List activities = packageManager.queryIntentActivities(appIntent, PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    return appIntent;
                }
            }
        }
        return null;
    }

    public void modifyIconByOperation(ImageView iv) {


        switch(MiniAppTypes.lookup(miniApp.getOperation())) {
            case DIAL:
                iv.setImageResource(R.drawable.call);

                break;
            case OPEN_APP:
                String appName = miniApp.getParams();
                Intent intentMatchingAppName = findIntentMatchingAppName(appName);
                if (intentMatchingAppName != null) {
                    try {
                        Drawable activityIcon = packageManager.getActivityIcon(intentMatchingAppName);
                        iv.setImageDrawable(activityIcon);
                    } catch (PackageManager.NameNotFoundException e) {
                        // do nothing. Default icon will be used.
                        // p.s we will validated earlier that it exists so shouldn't happen.
                        // TODO TALYAC log
                        iv.setImageResource(R.drawable.app_link);
                    }
                } else {
                    iv.setImageResource(R.drawable.app_link);
                }

                break;
            default:
                iv.setImageResource(R.drawable.web_link);
                break;
        }
    }

    public boolean validateIntentByOperation() {
        String operation = miniApp.getOperation();
        MiniAppTypes miniAppTypes = MiniAppTypes.lookup(operation);
        if (miniAppTypes != null) {
            switch (miniAppTypes) {
                case OPEN_URL:
                    String url = miniApp.getParams();
                    String fixedUrl = URLUtil.guessUrl(url);
                    return URLUtil.isValidUrl(fixedUrl);

                case DIAL:
                    String number = miniApp.getParams();
                    return android.util.Patterns.PHONE.matcher(number).matches();
                //android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches();

                case OPEN_APP:
                    return true;
            }
        }
        return false;
    }
}

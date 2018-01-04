package com.spotapps.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.spotapps.beans.MiniApp;
import com.spotapps.beans.MiniAppKey;
import com.spotapps.beans.Spot;
import com.spotapps.beans.SpotKey;
import com.spotapps.beans.SpotLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tty on 4/7/2016.
 * TODO TALYAC this should use GetTask and the url should use UriBuilder (https://www.udacity.com/course/viewer#!/c-ud853/l-1469948762/e-1630778633/m-1630778636)
 */
public class GetMiniAppsTask extends AsyncTask<MiniAppKey, Integer, List<MiniApp>> {


    public GetMiniAppsTask(Context context) {
        this.context = context;
    }

    private Context context;

    protected List<MiniApp> doInBackground(MiniAppKey... keys) {
        /*
            To ensure that a task is cancelled as quickly as possible,
            you should always check the return value of isCancelled() periodically from doInBackground(Object[]),
            if possible (inside a loop for instance.)
            if possible (inside a loop for instance.)
         */
        // TODO extract location data from key
        MiniAppKey key = keys[0];
        if (key == null) {
            throw new RuntimeException("AHHHHHHHH!!!!!");
        }
        Spot spot = key.getSpot();
        AWSAdapter awsAdapter = new AWSAdapter();
        List<MiniApp> result = awsAdapter.loadMiniApps(this.context, spot);
        // publishProgress(50);
        // TODO TALYAC here we can call publishProgress()

        return result;
    }

    // TODO TALYAC an integer is returned to keep track of the progress. Is this the best way to do this?
    protected void onProgressUpdate(Integer... progress) {
//        if (progress != null && progress.length > 0){
//            setLoadingCounter(progress[0]);
//        }
    }

    protected void onPostExecute(List<Spot> result) {
        // TODO TALYAC update the adapter with the new data
//            if (result!= null && result.size() > 0){
//                setLoadedSpot(result.get(0));
//            }
    }
}

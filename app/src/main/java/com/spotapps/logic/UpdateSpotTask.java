package com.spotapps.logic;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.spotapps.ViewNearbySpotsActivity;
import com.spotapps.beans.FullSpot;
import com.spotapps.beans.MiniApp;
import com.spotapps.beans.Spot;
import com.spotapps.beans.SpotFactory;
import com.spotapps.server.AWSAdapter;

import java.util.List;

/**
 * Created by tty on 28/12/2016.
 */

public class UpdateSpotTask extends AsyncTask<Spot, Integer, UpdateSpotResult> {

    public UpdateSpotTask(Context context) {
        this.context = context;
    }

    private Context context;

    protected UpdateSpotResult doInBackground(Spot... spots) {
        /*
            To ensure that a task is cancelled as quickly as possible,
            you should always check the return value of isCancelled() periodically from doInBackground(Object[]),
            if possible (inside a loop for instance.)
            if possible (inside a loop for instance.)
         */
        // TODO extract spot data from key
        Spot spot = spots[0];
        if (spot == null) {
            throw new RuntimeException("AHHHHHHHH!!!!!");
        }
        AWSAdapter awsAdapter = new AWSAdapter();

        boolean resultCode = awsAdapter.updateSpotLocation(this.context, spot);


        List<MiniApp> listMiniApp = awsAdapter.loadMiniApps(this.context, spot);
        FullSpot firstSpot = SpotFactory.createNewFullSpot(spot, listMiniApp);
        UpdateSpotResult updateSpotResult = new UpdateSpotResult(firstSpot, resultCode);

        // publishProgress(50);
        // TODO TALYAC here we can call publishProgress()

        return updateSpotResult;
    }

    // TODO TALYAC an integer is returned to keep track of the progress. Is this the best way to do this?
    protected void onProgressUpdate(Integer... progress) {
//        if (progress != null && progress.length > 0){
//            setLoadingCounter(progress[0]);
//        }
    }

    protected void onPostExecute(UpdateSpotResult updateSpotResult) {
        // TODO TALYAC update the adapter with the new data
//          populateMiniApps(result);
//          registerOnClickCallback();

    }
}
/////////////////

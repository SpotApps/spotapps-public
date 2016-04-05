package com.spotapps;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.spotapps.beans.Spot;
import com.spotapps.beans.SpotBundleUtils;
import com.spotapps.beans.SpotFactory;
import com.spotapps.beans.SpotKey;
import com.spotapps.server.ServerFacade;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * SplashActivity is the first activity in the application - it is responsible for opening a "loading..." splash screen.
 * Then in the background it loads the current spot and launches the relevant page in the app.
 * Members of the activity are those that remain last
 */
public class SplashActivity extends Activity {

//    private Spot loadedSpot;
    private LoadSpotsTask aSyncTask;

//    public synchronized void setLoadedSpot(Spot loadedSpot) {
//        this.loadedSpot = loadedSpot;
//    }
//
//    public synchronized Spot getLoadedSpot() {
//        return loadedSpot;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        final TextView tv = (TextView) findViewById(R.id.splashtextid);
        final ImageView iv = (ImageView) findViewById(R.id.splashimage);
        final AnimationSet an = (AnimationSet)AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotation);
        final Animation anFade = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);
        iv.startAnimation(an);
        for (Animation animation : an.getAnimations()) {

            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    startLoadingSpots();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    if (hasLoadingEnded()) {

                        Bundle bundle = new Bundle();

                        try {
                            SpotBundleUtils.storeSpotsInBundle(bundle, loadSpotsForCurrentLocation());
                        } catch (ExecutionException e) {
                            logMessage(e);
                            return; // TODO end application?
                        } catch (InterruptedException e) {
                            logMessage(e);
                            return;// TODO end application?
                        }
                        iv.startAnimation(anFade);
                        finish();
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                }
            });
        }
    }

    private void logMessage(Exception e) {
        // TODO: 12/30/2015
    }

    private List<Spot> loadSpotsForCurrentLocation() throws ExecutionException, InterruptedException {

        return aSyncTask.get();
    }

    public void setLoadingCounter(int loadingCounter) {
//        this.loadingCounter = loadingCounter;
    }

//    int loadingCounter = 0;
    //int maxLoaded = 5;
    private void startLoadingSpots() {
        // TODO: 12/30/2015 create key based on location data
        SpotKey spotKey = SpotFactory.createSpotKey();
        aSyncTask = new LoadSpotsTask();
        aSyncTask.execute(spotKey);
    }

    private boolean hasLoadingEnded() {
        //return aSyncTask.getLoadedSpot() != null;
        AsyncTask.Status status = aSyncTask.getStatus();
        return AsyncTask.Status.FINISHED.equals(status);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO TALYAC this should use GetTask
    private class LoadSpotsTask extends AsyncTask<SpotKey, Integer, List<Spot>> {
        protected List<Spot> doInBackground(SpotKey... keys) {
            Spot spot = ServerFacade.loadSpot();
            List<Spot> result = new ArrayList<>();
            // here we can call publishProgress() while loading the spots
            result.add(spot);
            simulate5SecondsWaitTime();
            return result;
        }

        // TODO TALYAC an integer is returned to keep track of the progress. Is this the best way to do this?
        protected void onProgressUpdate(Integer... progress) {
            if (progress != null && progress.length > 0){
                setLoadingCounter(progress[0]);
            }
        }

        protected void onPostExecute(List<Spot> result) {
//            if (result!= null && result.size() > 0){
//                setLoadedSpot(result.get(0));
//            }
        }

        private void simulate5SecondsWaitTime() {
            int count = 5;
            ///// load spots
            for (int i = 0; i < count; i++) {
                try {
                    synchronized (this){
                        wait(1000);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
                // TODO TALYAC result.add;
                publishProgress((int) ((i / (float) count) * 100));
                // Escape early if cancel() is called
                if (isCancelled()) break;
            }
        }
        //

    }
}

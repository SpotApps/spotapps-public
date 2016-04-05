package com.spotapps;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.spotapps.R;

public class ToDeleteMainActivity extends Activity {
    private static final int PROGRESS = 0x1;

    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    private Handler mHandler = new Handler();


    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        //setContentView(R.layout.activity_main);

//        mProgress = (ProgressBar) findViewById(R.id.splashprogressid);
//
//        // Start lengthy operation in a background thread
//        new Thread(new Runnable() {
//            public void run() {
//                while (mProgressStatus < 100) {
//                    mProgressStatus = doWork();
//
//                    // Update the progress bar
//                    mHandler.post(new Runnable() {
//                        public void run() {
//                            mProgress.setProgress(mProgressStatus);
//                        }
//                    });
//                }
//            }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//        }).start();
//    }
//    private static int i;
//    private int doWork() {
//
//        return i++;
    }
}
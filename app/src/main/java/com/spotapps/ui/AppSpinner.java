package com.spotapps.ui;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;


/**
 * Created by tty on 05/04/2017.
 */

public class AppSpinner extends android.support.v7.widget.AppCompatSpinner {

    public AppSpinner(Context context) {
        super(context);
    }

    public AppSpinner(Context context, int mode) {
        super(context, mode);
    }

    public AppSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AppSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public AppSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
    }
}

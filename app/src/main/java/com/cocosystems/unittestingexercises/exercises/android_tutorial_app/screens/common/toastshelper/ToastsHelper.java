package com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.toastshelper;

import android.content.Context;
import android.widget.Toast;

import com.cocosystems.unittestingexercises.R;


public class ToastsHelper {

    private final Context mContext;

    public ToastsHelper(Context context) {
        mContext = context;
    }

    public void showUseCaseError() {
        Toast.makeText(mContext, R.string.error_network_call_failed, Toast.LENGTH_SHORT).show();
    }
}

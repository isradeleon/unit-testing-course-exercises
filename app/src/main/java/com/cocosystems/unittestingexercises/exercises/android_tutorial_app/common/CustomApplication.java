package com.cocosystems.unittestingexercises.exercises.android_tutorial_app.common;

import android.app.Application;

import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.common.dependencyinjection.CompositionRoot;

public class CustomApplication extends Application {

    private CompositionRoot mCompositionRoot;

    @Override
    public void onCreate() {
        super.onCreate();
        mCompositionRoot = new CompositionRoot();
    }

    public CompositionRoot getCompositionRoot() {
        return mCompositionRoot;
    }
}

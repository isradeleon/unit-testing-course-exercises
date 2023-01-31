package com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.controllers;

import androidx.appcompat.app.AppCompatActivity;

import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.common.CustomApplication;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.common.dependencyinjection.ControllerCompositionRoot;

public class BaseActivity extends AppCompatActivity {

    private ControllerCompositionRoot mControllerCompositionRoot;

    protected ControllerCompositionRoot getCompositionRoot() {
        if (mControllerCompositionRoot == null) {
            mControllerCompositionRoot = new ControllerCompositionRoot(
                    ((CustomApplication) getApplication()).getCompositionRoot(),
                    this
            );
        }
        return mControllerCompositionRoot;
    }

}

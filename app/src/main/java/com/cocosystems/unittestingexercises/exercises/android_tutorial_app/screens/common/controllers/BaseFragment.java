package com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.controllers;

import androidx.fragment.app.Fragment;

import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.common.CustomApplication;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.common.dependencyinjection.ControllerCompositionRoot;

public class BaseFragment extends Fragment {

    private ControllerCompositionRoot mControllerCompositionRoot;

    protected ControllerCompositionRoot getCompositionRoot() {
        if (mControllerCompositionRoot == null) {
            mControllerCompositionRoot = new ControllerCompositionRoot(
                    ((CustomApplication) requireActivity().getApplication()).getCompositionRoot(),
                    requireActivity()
            );
        }
        return mControllerCompositionRoot;
    }
}

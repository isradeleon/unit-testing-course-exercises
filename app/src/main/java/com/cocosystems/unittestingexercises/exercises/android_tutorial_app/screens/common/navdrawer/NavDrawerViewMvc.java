package com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.navdrawer;

import android.widget.FrameLayout;

import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.views.ObservableViewMvc;

public interface NavDrawerViewMvc extends ObservableViewMvc<NavDrawerViewMvc.Listener> {

    interface Listener {

        void onQuestionsListClicked();
    }

    FrameLayout getFragmentFrame();

    boolean isDrawerOpen();
    void openDrawer();
    void closeDrawer();

}

package com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.controllers;

public interface BackPressDispatcher {
    void registerListener(BackPressedListener listener);
    void unregisterListener(BackPressedListener listener);
}

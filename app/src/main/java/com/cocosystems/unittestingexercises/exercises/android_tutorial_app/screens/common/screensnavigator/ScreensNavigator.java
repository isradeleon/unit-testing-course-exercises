package com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.screensnavigator;

import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.fragmentframehelper.FragmentFrameHelper;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.questiondetails.QuestionDetailsFragment;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.questionslist.QuestionsListFragment;

public class ScreensNavigator {

    private FragmentFrameHelper mFragmentFrameHelper;

    public ScreensNavigator(FragmentFrameHelper fragmentFrameHelper) {
        mFragmentFrameHelper = fragmentFrameHelper;
    }

    public void toQuestionDetails(String questionId) {
        mFragmentFrameHelper.replaceFragment(QuestionDetailsFragment.newInstance(questionId));
    }

    public void toQuestionsList() {
        mFragmentFrameHelper.replaceFragmentAndClearBackstack(QuestionsListFragment.newInstance());
    }

    public void navigateUp() {
        mFragmentFrameHelper.navigateUp();
    }
}

package com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common;

import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.navdrawer.NavDrawerHelper;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.navdrawer.NavDrawerViewMvc;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.navdrawer.NavDrawerViewMvcImpl;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.toolbar.ToolbarViewMvc;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.questiondetails.QuestionDetailsViewMvc;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.questiondetails.QuestionDetailsViewMvcImpl;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.questionslist.QuestionsListViewMvc;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.questionslist.QuestionsListViewMvcImpl;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.questionslist.questionslistitem.QuestionsListItemViewMvc;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.questionslist.questionslistitem.QuestionsListItemViewMvcImpl;

public class ViewMvcFactory {

    private final LayoutInflater mLayoutInflater;
    private final NavDrawerHelper mNavDrawerHelper;

    public ViewMvcFactory(LayoutInflater layoutInflater, NavDrawerHelper navDrawerHelper) {
        mLayoutInflater = layoutInflater;
        mNavDrawerHelper = navDrawerHelper;
    }

    public QuestionsListViewMvc getQuestionsListViewMvc(@Nullable ViewGroup parent) {
        return new QuestionsListViewMvcImpl(mLayoutInflater, parent, mNavDrawerHelper, this);
    }

    public QuestionsListItemViewMvc getQuestionsListItemViewMvc(@Nullable ViewGroup parent) {
        return new QuestionsListItemViewMvcImpl(mLayoutInflater, parent);
    }

    public QuestionDetailsViewMvc getQuestionDetailsViewMvc(@Nullable ViewGroup parent) {
        return new QuestionDetailsViewMvcImpl(mLayoutInflater, parent, this);
    }

    public ToolbarViewMvc getToolbarViewMvc(@Nullable ViewGroup parent) {
        return new ToolbarViewMvc(mLayoutInflater, parent);
    }

    public NavDrawerViewMvc getNavDrawerViewMvc(@Nullable ViewGroup parent) {
        return new NavDrawerViewMvcImpl(mLayoutInflater, parent);
    }
}

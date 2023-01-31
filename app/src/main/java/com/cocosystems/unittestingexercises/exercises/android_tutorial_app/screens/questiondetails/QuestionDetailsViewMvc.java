package com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.questiondetails;

import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.questions.QuestionDetails;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.views.ObservableViewMvc;

public interface QuestionDetailsViewMvc extends ObservableViewMvc<QuestionDetailsViewMvc.Listener> {

    public interface Listener {
        void onNavigateUpClicked();
    }

    void bindQuestion(QuestionDetails question);

    void showProgressIndication();

    void hideProgressIndication();
}

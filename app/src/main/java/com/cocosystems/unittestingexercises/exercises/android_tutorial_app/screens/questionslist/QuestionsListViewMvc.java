package com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.questionslist;

import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.questions.Question;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.views.ObservableViewMvc;

import java.util.List;

public interface QuestionsListViewMvc extends ObservableViewMvc<QuestionsListViewMvc.Listener> {

    public interface Listener {
        void onQuestionClicked(Question question);
    }

    void bindQuestions(List<Question> questions);

    void showProgressIndication();

    void hideProgressIndication();

}

package com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.questionslist.questionslistitem;

import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.questions.Question;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.views.ObservableViewMvc;

public interface QuestionsListItemViewMvc extends ObservableViewMvc<QuestionsListItemViewMvc.Listener> {

    public interface Listener {
        void onQuestionClicked(Question question);
    }

    void bindQuestion(Question question);
}

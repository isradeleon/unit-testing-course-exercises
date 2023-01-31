package com.cocosystems.unittestingexercises.exercises.android_tutorial_app.networking;

import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.common.Constants;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.networking.questions.QuestionDetailsResponseSchema;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.networking.questions.QuestionsListResponseSchema;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StackoverflowApi {

    @GET("/questions?key=" + Constants.STACKOVERFLOW_API_KEY + "&sort=activity&order=desc&site=stackoverflow&filter=withbody")
    Call<QuestionsListResponseSchema> fetchLastActiveQuestions(@Query("pagesize") Integer pageSize);

    @GET("/questions/{questionId}?key=" + Constants.STACKOVERFLOW_API_KEY + "&site=stackoverflow&filter=withbody")
    Call<QuestionDetailsResponseSchema> fetchQuestionDetails(@Path("questionId") String questionId);
}

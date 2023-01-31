package com.cocosystems.unittestingexercises.exercises.android_tutorial_app.common.dependencyinjection;

import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.common.Constants;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.common.time.TimeProvider;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.networking.StackoverflowApi;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.networking.questions.FetchQuestionDetailsEndpoint;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.questions.FetchQuestionDetailsUseCase;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CompositionRoot {

    private Retrofit mRetrofit;
    private FetchQuestionDetailsUseCase mFetchQuestionDetailsUseCase;

    private Retrofit getRetrofit() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    public StackoverflowApi getStackoverflowApi() {
        return getRetrofit().create(StackoverflowApi.class);
    }

    public TimeProvider getTimeProvider() {
        return new TimeProvider();
    }

    private FetchQuestionDetailsEndpoint getFetchQuestionDetailsEndpoint() {
        return new FetchQuestionDetailsEndpoint(getStackoverflowApi());
    }

    public FetchQuestionDetailsUseCase getFetchQuestionDetailsUseCase() {
        if (mFetchQuestionDetailsUseCase == null) {
            mFetchQuestionDetailsUseCase = new FetchQuestionDetailsUseCase(getFetchQuestionDetailsEndpoint(), getTimeProvider());
        }
        return mFetchQuestionDetailsUseCase;
    }
}

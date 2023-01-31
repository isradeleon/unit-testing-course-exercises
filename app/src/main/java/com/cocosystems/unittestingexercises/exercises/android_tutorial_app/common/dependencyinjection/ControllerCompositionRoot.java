package com.cocosystems.unittestingexercises.exercises.android_tutorial_app.common.dependencyinjection;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;

import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.common.time.TimeProvider;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.networking.StackoverflowApi;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.networking.questions.FetchLastActiveQuestionsEndpoint;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.questions.FetchLastActiveQuestionsUseCase;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.questions.FetchQuestionDetailsUseCase;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.ViewMvcFactory;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.controllers.BackPressDispatcher;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.fragmentframehelper.FragmentFrameHelper;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.fragmentframehelper.FragmentFrameWrapper;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.navdrawer.NavDrawerHelper;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.screensnavigator.ScreensNavigator;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.toastshelper.ToastsHelper;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.questiondetails.QuestionDetailsController;
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.questionslist.QuestionsListController;

public class ControllerCompositionRoot {

    private final CompositionRoot mCompositionRoot;
    private final FragmentActivity mActivity;

    public ControllerCompositionRoot(CompositionRoot compositionRoot, FragmentActivity activity) {
        mCompositionRoot = compositionRoot;
        mActivity = activity;
    }

    private FragmentActivity getActivity() {
        return mActivity;
    }

    private Context getContext() {
        return mActivity;
    }

    private FragmentManager getFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    private StackoverflowApi getStackoverflowApi() {
        return mCompositionRoot.getStackoverflowApi();
    }

    private FetchLastActiveQuestionsEndpoint getFetchLastActiveQuestionsEndpoint() {
        return new FetchLastActiveQuestionsEndpoint(getStackoverflowApi());
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(getContext());
    }

    public ViewMvcFactory getViewMvcFactory() {
        return new ViewMvcFactory(getLayoutInflater(), getNavDrawerHelper());
    }

    private NavDrawerHelper getNavDrawerHelper() {
        return (NavDrawerHelper) getActivity();
    }

    public FetchQuestionDetailsUseCase getFetchQuestionDetailsUseCase() {
        return mCompositionRoot.getFetchQuestionDetailsUseCase();
    }

    public FetchLastActiveQuestionsUseCase getFetchLastActiveQuestionsUseCase() {
        return new FetchLastActiveQuestionsUseCase(getFetchLastActiveQuestionsEndpoint());
    }

    public TimeProvider getTimeProvider() {
        return mCompositionRoot.getTimeProvider();
    }

    public QuestionsListController getQuestionsListController() {
        return new QuestionsListController(
                getFetchLastActiveQuestionsUseCase(),
                getScreensNavigator(),
                getToastsHelper(),
                getTimeProvider());
    }

    public ToastsHelper getToastsHelper() {
        return new ToastsHelper(getContext());
    }

    public ScreensNavigator getScreensNavigator() {
        return new ScreensNavigator(getFragmentFrameHelper());
    }

    private FragmentFrameHelper getFragmentFrameHelper() {
        return new FragmentFrameHelper(getActivity(), getFragmentFrameWrapper(), getFragmentManager());
    }

    private FragmentFrameWrapper getFragmentFrameWrapper() {
        return (FragmentFrameWrapper) getActivity();
    }

    public BackPressDispatcher getBackPressDispatcher() {
        return (BackPressDispatcher) getActivity();
    }

    public QuestionDetailsController getQuestionDetailsController() {
        return new QuestionDetailsController(getFetchQuestionDetailsUseCase(), getScreensNavigator(), getToastsHelper());
    }
}

package com.cocosystems.unittestingexercises.android_tutorial_app

import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.questions.FetchQuestionDetailsUseCase
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.questions.QuestionDetails
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.screensnavigator.ScreensNavigator
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.common.toastshelper.ToastsHelper
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.questiondetails.QuestionDetailsController
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.screens.questiondetails.QuestionDetailsViewMvc
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class QuestionDetailsControllerTest {
    /**
     * System Under Test
     * */
    private lateinit var systemUnderTest: QuestionDetailsController

    /**
     * SUT dependencies
     * */
    private lateinit var useCaseTD: UseCaseTD

    @Mock
    private lateinit var questionDetailsViewMvcMock: QuestionDetailsViewMvc

    @Mock
    private lateinit var screensNavigatorMock: ScreensNavigator

    @Mock
    private lateinit var toastsHelperMock: ToastsHelper

    /**
     * Helper constants & methods
     * */
    companion object {
        const val QUESTION_ID = "13"
        private const val QUESTION_TITLE = "Question title"
        private const val QUESTION_BODY = "Question body"

        fun getQuestionDetails(): QuestionDetails {
            return QuestionDetails(
                QUESTION_ID,
                QUESTION_TITLE,
                QUESTION_BODY
            )
        }
    }

    @Before
    fun setup() {
        useCaseTD = UseCaseTD()
        systemUnderTest = QuestionDetailsController(
            useCaseTD,
            screensNavigatorMock,
            toastsHelperMock
        )
        systemUnderTest.bindView(questionDetailsViewMvcMock)
        systemUnderTest.bindQuestionId(QUESTION_ID)
    }

    @Test
    fun onStart_progressIndicationShown() {
        systemUnderTest.onStart()
        verify(questionDetailsViewMvcMock).showProgressIndication()
    }

    @Test
    fun onStart_useCaseSuccess_progressIndicationHidden() {
        systemUnderTest.onStart()
        verify(questionDetailsViewMvcMock).hideProgressIndication()
    }

    @Test
    fun onStart_useCaseSuccess_detailsBoundToView() {
        systemUnderTest.onStart()
        assert(useCaseTD.fetchCalled > 0)
        verify(questionDetailsViewMvcMock).bindQuestion(getQuestionDetails())
    }

    @Test
    fun onStart_useCaseFailed_errorToastShown() {
        useCaseTD.failure = true

        systemUnderTest.onStart()
        verify(toastsHelperMock).showUseCaseError()
    }

    @Test
    fun onStart_useCaseFailed_progressIndicationHidden() {
        useCaseTD.failure = true

        systemUnderTest.onStart()
        verify(questionDetailsViewMvcMock).hideProgressIndication()
    }

    @Test
    fun onStart_listenersRegistered() {
        systemUnderTest.onStart()
        verify(questionDetailsViewMvcMock).registerListener(systemUnderTest)
        assert(useCaseTD.isRegisteredInListeners(systemUnderTest))
    }

    @Test
    fun onStop_listenersUnregistered() {
        systemUnderTest.onStart()
        systemUnderTest.onStop()
        verify(questionDetailsViewMvcMock).unregisterListener(systemUnderTest)
        assert(!useCaseTD.isRegisteredInListeners(systemUnderTest))
    }

    @Test
    fun onNavigateUpClicked_screenNavigatorNavigateUpCalled() {
        systemUnderTest.onNavigateUpClicked()
        verify(screensNavigatorMock).navigateUp()
    }

    /**
     * Helper fake implementation for FetchQuestionDetailsUseCase
     * */
    class UseCaseTD : FetchQuestionDetailsUseCase(null, null) {
        var failure = false
        var fetchCalled = 0

        override fun fetchQuestionDetailsAndNotify(questionId: String) {
            fetchCalled++
            listeners.forEach { listener ->
                if (failure)
                    listener.onQuestionDetailsFetchFailed()
                else
                    listener.onQuestionDetailsFetched(getQuestionDetails())
            }
        }

        fun isRegisteredInListeners(candidate: Listener) = listeners.contains(candidate)
    }
}
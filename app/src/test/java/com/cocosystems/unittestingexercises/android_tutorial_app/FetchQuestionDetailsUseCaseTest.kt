package com.cocosystems.unittestingexercises.android_tutorial_app

import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.common.time.TimeProvider
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.networking.questions.FetchQuestionDetailsEndpoint
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.networking.questions.QuestionSchema
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.questions.FetchQuestionDetailsUseCase
import com.cocosystems.unittestingexercises.exercises.android_tutorial_app.questions.QuestionDetails
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Matchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FetchQuestionDetailsUseCaseTest {
    /**
     * System Under Test
     * */
    private lateinit var systemUnderTest: FetchQuestionDetailsUseCase

    /**
     * SUT dependencies
     * */
    @Mock
    private lateinit var fetchQuestionDetailsEndpointMock: FetchQuestionDetailsEndpoint

    @Mock
    private lateinit var timeProviderMock: TimeProvider

    @Mock
    private lateinit var listener1: FetchQuestionDetailsUseCase.Listener

    @Mock
    private lateinit var listener2: FetchQuestionDetailsUseCase.Listener

    /**
     * Mockito captors
     * */
    @Captor lateinit var questionDetailsCaptor: ArgumentCaptor<QuestionDetails>

    /**
     * Constants
     * */
    companion object {
        const val QUESTION_ID = "13"
        const val QUESTION_TITLE = "Question title"
        const val QUESTION_BODY = "Question body"
    }

    @Before
    fun setup() {
        systemUnderTest = FetchQuestionDetailsUseCase(
            fetchQuestionDetailsEndpointMock,
            timeProviderMock
        )
        endpointSuccessScenario()
    }

    @Test
    fun fetchDetails_correctIDPassedToEndpoint() {
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID)
        verify(
            fetchQuestionDetailsEndpointMock,
            times(1)
        ).fetchQuestionDetails(eq(QUESTION_ID), any())
    }

    @Test
    fun fetchDetails_success_listenersNotifiedWithData() {
        systemUnderTest.registerListener(listener1)
        systemUnderTest.registerListener(listener2)
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID)

        verify(listener1).onQuestionDetailsFetched(questionDetailsCaptor.capture())
        verify(listener2).onQuestionDetailsFetched(questionDetailsCaptor.capture())

        assert(questionDetailsCaptor.allValues[0] == getQuestionDetails())
        assert(questionDetailsCaptor.allValues[1] == getQuestionDetails())
    }

    @Test
    fun fetchDetails_success_unregisteredListenersNotNotified() {
        systemUnderTest.registerListener(listener1)
        systemUnderTest.registerListener(listener2)
        systemUnderTest.unregisterListener(listener1)
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID)

        verifyNoMoreInteractions(listener1)
    }

    @Test
    fun fetchDetails_failure_listenersNotifiedOfFailure() {
        endpointFailureScenario()

        systemUnderTest.registerListener(listener1)
        systemUnderTest.registerListener(listener2)
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID)

        verify(listener1).onQuestionDetailsFetchFailed()
        verify(listener2).onQuestionDetailsFetchFailed()
    }

    @Test
    fun fetchDetails_failure_unregisteredListenersNotNotified() {
        endpointFailureScenario()

        systemUnderTest.registerListener(listener1)
        systemUnderTest.registerListener(listener2)
        systemUnderTest.unregisterListener(listener1)
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID)

        verifyNoMoreInteractions(listener1)
    }

    /**
     * I'm not sure if this is the right way to test this hidden cache functionality
     * */

    // fetched once before - no more interactions with endpoint
    @Test
    fun fetchDetailsOnce_success_endpointNotCalledASecondTime() {
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID)
        verify(fetchQuestionDetailsEndpointMock).fetchQuestionDetails(eq(QUESTION_ID), any())
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID)
        verifyNoMoreInteractions(fetchQuestionDetailsEndpointMock)
    }

    // fetched once before - listeners notified from cache
    @Test
    fun fetchDetailsOnce_success_listenersNotifiedFromCache() {
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID)
        verify(fetchQuestionDetailsEndpointMock).fetchQuestionDetails(eq(QUESTION_ID), any())

        systemUnderTest.registerListener(listener1)
        systemUnderTest.fetchQuestionDetailsAndNotify(QUESTION_ID)

        verify(listener1).onQuestionDetailsFetched(questionDetailsCaptor.capture())
        assert(questionDetailsCaptor.allValues[0] == getQuestionDetails())
    }

    /**
     * Helper scenario methods
     * */
    private fun endpointSuccessScenario() {
        doAnswer { invocation ->
            val callback =
                invocation.getArgumentAt(1, FetchQuestionDetailsEndpoint.Listener::class.java)

            callback.onQuestionDetailsFetched(getQuestionSchema())
            null
        }.`when`(fetchQuestionDetailsEndpointMock).fetchQuestionDetails(any(), any())
    }

    private fun endpointFailureScenario() {
        doAnswer { invocation ->
            val callback =
                invocation.getArgumentAt(1, FetchQuestionDetailsEndpoint.Listener::class.java)

            callback.onQuestionDetailsFetchFailed()
            null
        }.`when`(fetchQuestionDetailsEndpointMock).fetchQuestionDetails(any(), any())
    }

    private fun getQuestionSchema(): QuestionSchema {
        return QuestionSchema(
            QUESTION_TITLE, QUESTION_ID, QUESTION_BODY
        )
    }

    private fun getQuestionDetails(): QuestionDetails {
        return QuestionDetails(
            QUESTION_ID, QUESTION_TITLE, QUESTION_BODY
        )
    }
}
package com.cocosystems.unittestingexercises.exercise5

import com.cocosystems.unittestingexercises.exercises.exercise5.UpdateUsernameUseCaseSync
import com.cocosystems.unittestingexercises.exercises.exercise5.eventbus.EventBusPoster
import com.cocosystems.unittestingexercises.exercises.exercise5.eventbus.UserDetailsChangedEvent
import com.cocosystems.unittestingexercises.exercises.exercise5.networking.NetworkErrorException
import com.cocosystems.unittestingexercises.exercises.exercise5.networking.UpdateUsernameHttpEndpointSync
import com.cocosystems.unittestingexercises.exercises.exercise5.users.User
import com.cocosystems.unittestingexercises.exercises.exercise5.users.UsersCache
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UpdateUsernameUseCaseSyncTestMockAnnotated {
    lateinit var systemUnderTest: UpdateUsernameUseCaseSync

    // SUT dependencies
    @Mock
    private lateinit var updateUsernameHttpEndpointSyncMock: UpdateUsernameHttpEndpointSync
    @Mock
    private lateinit var usersCacheMock: UsersCache
    @Mock
    private lateinit var eventBusPosterMock: EventBusPoster

    companion object {
        const val USER_ID = "13"
        const val USERNAME = "john_wick"
    }

    @Before
    fun setup() {
        systemUnderTest = UpdateUsernameUseCaseSync(
            updateUsernameHttpEndpointSyncMock,
            usersCacheMock,
            eventBusPosterMock
        )
        success()
    }

    // Check use case passes the userId and username to the endpoint
    @Test
    fun update_userIdAndUsername_passedToEndpoint() {
        val captor = ArgumentCaptor.forClass(String::class.java)
        systemUnderTest.updateUsernameSync(
            USER_ID, USERNAME
        )

        // Mockito verify method
        verify(
            updateUsernameHttpEndpointSyncMock,
            times(1)
        ).updateUsername(captor.capture(), captor.capture())

        val captures = captor.allValues
        assert(captures[0] == USER_ID)
        assert(captures[1] == USERNAME)
    }

    // Check user data is being cached when update succeeds
    @Test
    fun update_success_newUserDataCached() {
        val captor = ArgumentCaptor.forClass(User::class.java)
        systemUnderTest.updateUsernameSync(
            USER_ID, USERNAME
        )

        verify(usersCacheMock).cacheUser(captor.capture())
        val capturedUser = captor.value

        assert(capturedUser.userId == USER_ID)
        assert(capturedUser.username == USERNAME)
    }

    // Check user data is not cached when general error
    @Test
    fun update_generalError_newUserDataNotCached() {
        generalError()
        systemUnderTest.updateUsernameSync(
            USER_ID, USERNAME
        )
        verifyNoMoreInteractions(usersCacheMock)
    }

    // Check user data is not cached when auth error
    @Test
    fun update_authError_newUserDataNotCached() {
        authError()
        systemUnderTest.updateUsernameSync(
            USER_ID, USERNAME
        )
        verifyNoMoreInteractions(usersCacheMock)
    }

    // Check user data is not cached when server error
    @Test
    fun update_serverError_newUserDataNotCached() {
        serverError()
        systemUnderTest.updateUsernameSync(
            USER_ID, USERNAME
        )
        verifyNoMoreInteractions(usersCacheMock)
    }

    // Check user data is not cached when network error
    @Test
    fun update_networkError_newUserDataNotCached() {
        networkError()
        systemUnderTest.updateUsernameSync(
            USER_ID, USERNAME
        )
        verifyNoMoreInteractions(usersCacheMock)
    }

    // Check event bus is being called when success
    @Test
    fun update_success_userDetailsChangedEventPosted() {
        val captor = ArgumentCaptor.forClass(Any::class.java)
        systemUnderTest.updateUsernameSync(
            USER_ID, USERNAME
        )
        verify(eventBusPosterMock).postEvent(captor.capture())
        assert(captor.value is UserDetailsChangedEvent)
    }

    // Check no interactions with event bus when general error
    @Test
    fun update_generalError_noEventBusInteractions() {
        generalError()
        systemUnderTest.updateUsernameSync(
            USER_ID, USERNAME
        )
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    // Check no interactions with event bus when auth error
    @Test
    fun update_authError_noEventBusInteractions() {
        authError()
        systemUnderTest.updateUsernameSync(
            USER_ID, USERNAME
        )
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    // Check no interactions with event bus when server error
    @Test
    fun update_serverError_noEventBusInteractions() {
        serverError()
        systemUnderTest.updateUsernameSync(
            USER_ID, USERNAME
        )
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    // Check no interactions with event bus when network error
    @Test
    fun update_networkError_noEventBusInteractions() {
        networkError()
        systemUnderTest.updateUsernameSync(
            USER_ID, USERNAME
        )
        verifyNoMoreInteractions(eventBusPosterMock)
    }

    // Check success returned when update succeeds
    @Test
    fun update_success_successReturned() {
        val result = systemUnderTest.updateUsernameSync(
            USER_ID, USERNAME
        )
        assert(result == UpdateUsernameUseCaseSync.UseCaseResult.SUCCESS)
    }

    // Check failure returned when general error
    @Test
    fun update_generalError_failureReturned() {
        generalError()
        val result = systemUnderTest.updateUsernameSync(
            USER_ID, USERNAME
        )
        assert(result == UpdateUsernameUseCaseSync.UseCaseResult.FAILURE)
    }

    // Check failure returned when auth error
    @Test
    fun update_authError_failureReturned() {
        authError()
        val result = systemUnderTest.updateUsernameSync(
            USER_ID, USERNAME
        )
        assert(result == UpdateUsernameUseCaseSync.UseCaseResult.FAILURE)
    }

    // Check failure returned when server error
    @Test
    fun update_serverError_failureReturned() {
        serverError()
        val result = systemUnderTest.updateUsernameSync(
            USER_ID, USERNAME
        )
        assert(result == UpdateUsernameUseCaseSync.UseCaseResult.FAILURE)
    }

    // Check network error returned when network error
    @Test
    fun update_networkError_failureReturned() {
        networkError()
        val result = systemUnderTest.updateUsernameSync(
            USER_ID, USERNAME
        )
        assert(result == UpdateUsernameUseCaseSync.UseCaseResult.NETWORK_ERROR)
    }

    // Endpoint results forced with mockito
    private fun success() {
        `when`(updateUsernameHttpEndpointSyncMock.updateUsername(
            any(), any()
        )).thenReturn(
            UpdateUsernameHttpEndpointSync.EndpointResult(
                UpdateUsernameHttpEndpointSync.EndpointResultStatus.SUCCESS,
                USER_ID,
                USERNAME
            )
        )
    }

    private fun serverError() {
        `when`(updateUsernameHttpEndpointSyncMock.updateUsername(
            any(), any()
        )).thenReturn(
            UpdateUsernameHttpEndpointSync.EndpointResult(
                UpdateUsernameHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,
                "", ""
            )
        )
    }

    private fun generalError() {
        `when`(updateUsernameHttpEndpointSyncMock.updateUsername(
            any(), any()
        )).thenReturn(
            UpdateUsernameHttpEndpointSync.EndpointResult(
                UpdateUsernameHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,
                "", ""
            )
        )
    }

    private fun authError() {
        `when`(updateUsernameHttpEndpointSyncMock.updateUsername(
            any(), any()
        )).thenReturn(
            UpdateUsernameHttpEndpointSync.EndpointResult(
                UpdateUsernameHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,
                "", ""
            )
        )
    }

    private fun networkError() {
        `when`(updateUsernameHttpEndpointSyncMock.updateUsername(
            any(), any()
        )).thenThrow(
            NetworkErrorException()
        )
    }
}
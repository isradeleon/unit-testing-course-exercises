package com.cocosystems.unittestingexercises.exercise6

import com.cocosystems.unittestingexercises.exercises.exercise6.FetchUserUseCaseSync
import com.cocosystems.unittestingexercises.exercises.exercise6.FetchUserUseCaseSyncImpl
import com.cocosystems.unittestingexercises.exercises.exercise6.networking.FetchUserHttpEndpointSync
import com.cocosystems.unittestingexercises.exercises.exercise6.networking.FetchUserHttpEndpointSync.*
import com.cocosystems.unittestingexercises.exercises.exercise6.networking.NetworkErrorException
import com.cocosystems.unittestingexercises.exercises.exercise6.users.User
import com.cocosystems.unittestingexercises.exercises.exercise6.users.UsersCache
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FetchUserUseCaseSyncTest {

    companion object {
        const val USER_ID = "13"
        const val USERNAME = "John Wick"
    }

    private lateinit var systemUnderTest: FetchUserUseCaseSync

    // Dependencies
    @Mock private lateinit var fetchUserHttpEndpointSyncMock: FetchUserHttpEndpointSync
    @Mock private lateinit var usersCacheMock: UsersCache

    @Before
    fun setup() {
        systemUnderTest = FetchUserUseCaseSyncImpl(
            fetchUserHttpEndpointSyncMock,
            usersCacheMock
        )
        success()
        userNotFoundInCache()
    }

    // call the endpoint if user is null
    @Test
    fun fetchUser_nullUser_endpointCalled() {
        systemUnderTest.fetchUserSync(USER_ID)
        verify(
            fetchUserHttpEndpointSyncMock,
            times(1)
        ).fetchUserSync(any())
    }

    // no interactions with endpoint when user is found in cache
    @Test
    fun fetchUser_userFound_endpointNotBeingCalled() {
        userFoundInCache()
        systemUnderTest.fetchUserSync(USER_ID)
        verifyNoMoreInteractions(fetchUserHttpEndpointSyncMock)
    }

    // cache the user after endpoint succeeds
    @Test
    fun fetchUser_endpointSuccess_usersCacheCalled() {
        val captor = ArgumentCaptor.forClass(User::class.java)

        systemUnderTest.fetchUserSync(USER_ID)
        verify(
            usersCacheMock,
            times(1)
        ).cacheUser(captor.capture())

        assert(captor.value.userId == USER_ID)
        assert(captor.value.username == USERNAME)
    }

    // correct parameters passed to the endpoint
    @Test
    fun fetchUser_correctParametersPassedToEndpoint() {
        val captor = ArgumentCaptor.forClass(String::class.java)

        systemUnderTest.fetchUserSync(USER_ID)
        verify(
            fetchUserHttpEndpointSyncMock,
            times(1)
        ).fetchUserSync(captor.capture())

        assert(captor.value == USER_ID)
    }

    // endpoint success - success returned
    @Test
    fun fetchUser_endpointSuccess_successReturned() {
        val result = systemUnderTest.fetchUserSync(USER_ID)
        assert(result.status == FetchUserUseCaseSync.Status.SUCCESS)
    }

    // general error - failure returned
    @Test
    fun fetchUser_endpointGeneralError_failureReturned() {
        generalError()
        val result = systemUnderTest.fetchUserSync(USER_ID)
        assert(result.status == FetchUserUseCaseSync.Status.FAILURE)
    }

    // auth error - failure returned
    @Test
    fun fetchUser_endpointAuthError_failureReturned() {
        authError()
        val result = systemUnderTest.fetchUserSync(USER_ID)
        assert(result.status == FetchUserUseCaseSync.Status.FAILURE)
    }

    // network exception - failure returned
    @Test
    fun fetchUser_endpointNetworkException_networkErrorReturned() {
        networkException()
        val result = systemUnderTest.fetchUserSync(USER_ID)
        assert(result.status == FetchUserUseCaseSync.Status.NETWORK_ERROR)
    }

    /**
    * helper methods for the different scenarios
    * */
    private fun success() {
        `when`(
            fetchUserHttpEndpointSyncMock.fetchUserSync(any())
        ).thenReturn(
            EndpointResult(
                EndpointStatus.SUCCESS,
                USER_ID, USERNAME
            )
        )
    }

    private fun authError() {
        `when`(
            fetchUserHttpEndpointSyncMock.fetchUserSync(any())
        ).thenReturn(
            EndpointResult(
                EndpointStatus.AUTH_ERROR,
                "", ""
            )
        )
    }

    private fun generalError() {
        `when`(
            fetchUserHttpEndpointSyncMock.fetchUserSync(any())
        ).thenReturn(
            EndpointResult(
                EndpointStatus.GENERAL_ERROR,
                "", ""
            )
        )
    }

    private fun networkException() {
        `when`(
            fetchUserHttpEndpointSyncMock.fetchUserSync(any())
        ).thenThrow(NetworkErrorException())
    }

    private fun userNotFoundInCache() {
        `when`(
            usersCacheMock.getUser(any())
        ).thenReturn(null)
    }

    private fun userFoundInCache() {
        `when`(
            usersCacheMock.getUser(any())
        ).thenReturn(User(USER_ID, USERNAME))
    }
}
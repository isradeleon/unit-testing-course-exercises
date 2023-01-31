package com.cocosystems.unittestingexercises.exercises

import com.cocosystems.unittestingexercises.exercises.example4.networking.NetworkErrorException
import com.cocosystems.unittestingexercises.exercises.exercise4.FetchUserProfileUseCaseSync
import com.cocosystems.unittestingexercises.exercises.exercise4.networking.UserProfileHttpEndpointSync
import com.cocosystems.unittestingexercises.exercises.exercise4.users.User
import com.cocosystems.unittestingexercises.exercises.exercise4.users.UsersCache
import org.junit.Before
import org.junit.Test

class FetchUserProfileUseCaseSyncTest {
    lateinit var systemUnderTest: FetchUserProfileUseCaseSync

    // SUT dependencies
    private lateinit var userProfileHttpEndpointSync: UserProfileHttpEndpointSyncTest
    private lateinit var usersCache: UsersCacheTest

    companion object {
        const val USER_ID = "13"
        const val USER_FULLNAME = "John Wick"
        const val USER_IMAGE = "User profile pic"
    }

    @Before
    fun setup() {
        userProfileHttpEndpointSync = UserProfileHttpEndpointSyncTest()
        usersCache = UsersCacheTest()

        systemUnderTest = FetchUserProfileUseCaseSync(
            userProfileHttpEndpointSync, usersCache
        )
    }

    // Check use case passes the userId to the endpoint
    @Test
    fun fetch_userId_passedToEndpoint() {
        systemUnderTest.fetchUserProfileSync(USER_ID)
        assert(userProfileHttpEndpointSync.userId == USER_ID)
    }

    // Check user info is cached when fetch succeeds
    @Test
    fun fetch_success_userInfoCached() {
        systemUnderTest.fetchUserProfileSync(USER_ID)
        assert(usersCache.getUser(USER_ID) != null)
        assert(usersCache.getUser(USER_ID)!!.userId == USER_ID)
        assert(usersCache.getUser(USER_ID)!!.fullName == USER_FULLNAME)
        assert(usersCache.getUser(USER_ID)!!.imageUrl == USER_IMAGE)
    }

    // Check user info is not cached when general error
    @Test
    fun fetch_generalError_userInfoNotCached() {
        userProfileHttpEndpointSync.forceGeneralError = true
        systemUnderTest.fetchUserProfileSync(USER_ID)
        assert(usersCache.getUser(USER_ID) == null)
    }

    // Check user info is not cached when auth error
    @Test
    fun fetch_authError_userInfoNotCached() {
        userProfileHttpEndpointSync.forceAuthError = true
        systemUnderTest.fetchUserProfileSync(USER_ID)
        assert(usersCache.getUser(USER_ID) == null)
    }

    // Check user info is not cached when server error
    @Test
    fun fetch_serverError_userInfoNotCached() {
        userProfileHttpEndpointSync.forceServerError = true
        systemUnderTest.fetchUserProfileSync(USER_ID)
        assert(usersCache.getUser(USER_ID) == null)
    }

    // Check user info is not cached when network exception
    @Test
    fun fetch_networkException_userInfoNotCached() {
        userProfileHttpEndpointSync.forceNetworkException = true
        systemUnderTest.fetchUserProfileSync(USER_ID)
        assert(usersCache.getUser(USER_ID) == null)
    }

    // Check success result when use case succeeds
    @Test
    fun fetch_success_useCaseSuccessReturned() {
        val result = systemUnderTest.fetchUserProfileSync(USER_ID)
        assert(result == FetchUserProfileUseCaseSync.UseCaseResult.SUCCESS)
    }

    // Check failure result when general error
    @Test
    fun fetch_generalError_useCaseFailureReturned() {
        userProfileHttpEndpointSync.forceGeneralError = true
        val result = systemUnderTest.fetchUserProfileSync(USER_ID)
        assert(result == FetchUserProfileUseCaseSync.UseCaseResult.FAILURE)
    }

    // Check failure result when auth error
    @Test
    fun fetch_authError_useCaseFailureReturned() {
        userProfileHttpEndpointSync.forceAuthError = true
        val result = systemUnderTest.fetchUserProfileSync(USER_ID)
        assert(result == FetchUserProfileUseCaseSync.UseCaseResult.FAILURE)
    }

    // Check failure result when server error
    @Test
    fun fetch_serverError_useCaseFailureReturned() {
        userProfileHttpEndpointSync.forceServerError = true
        val result = systemUnderTest.fetchUserProfileSync(USER_ID)
        assert(result == FetchUserProfileUseCaseSync.UseCaseResult.FAILURE)
    }

    // Check failure result when server error
    @Test
    fun fetch_networkException_useCaseNetworkErrorReturned() {
        userProfileHttpEndpointSync.forceNetworkException = true
        val result = systemUnderTest.fetchUserProfileSync(USER_ID)
        assert(result == FetchUserProfileUseCaseSync.UseCaseResult.NETWORK_ERROR)
    }

    // Test implementations
    class UserProfileHttpEndpointSyncTest: UserProfileHttpEndpointSync {
        var userId: String = ""
        var forceGeneralError = false
        var forceAuthError = false
        var forceServerError = false
        var forceNetworkException = false

        override fun getUserProfile(userId: String): UserProfileHttpEndpointSync.EndpointResult {
            this.userId = userId

            return when {
                forceGeneralError -> UserProfileHttpEndpointSync.EndpointResult(
                    UserProfileHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,
                    "", "", ""
                )
                forceAuthError -> UserProfileHttpEndpointSync.EndpointResult(
                    UserProfileHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,
                    "", "", ""
                )
                forceServerError -> UserProfileHttpEndpointSync.EndpointResult(
                    UserProfileHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,
                    "", "", ""
                )
                forceNetworkException -> throw NetworkErrorException()
                else -> UserProfileHttpEndpointSync.EndpointResult(
                    UserProfileHttpEndpointSync.EndpointResultStatus.SUCCESS,
                    userId, USER_FULLNAME, USER_IMAGE
                )
            }
        }
    }

    class UsersCacheTest: UsersCache {
        // In this case the type of the test double is FAKE
        private var cachedUser: User? = null

        override fun cacheUser(user: User) {
            cachedUser = user
        }

        override fun getUser(userId: String): User? {
            return cachedUser
        }
    }
}
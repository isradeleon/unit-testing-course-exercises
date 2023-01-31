package com.cocosystems.unittestingexercises.exercises.exercise6

import com.cocosystems.unittestingexercises.exercises.exercise6.networking.FetchUserHttpEndpointSync
import com.cocosystems.unittestingexercises.exercises.exercise6.networking.NetworkErrorException
import com.cocosystems.unittestingexercises.exercises.exercise6.users.User
import com.cocosystems.unittestingexercises.exercises.exercise6.users.UsersCache

class FetchUserUseCaseSyncImpl(
    private val fetchUserHttpEndpointSync: FetchUserHttpEndpointSync,
    private val usersCache: UsersCache
) : FetchUserUseCaseSync {

    override fun fetchUserSync(userId: String): FetchUserUseCaseSync.UseCaseResult {
        return usersCache.getUser(userId)?.let {
            FetchUserUseCaseSync.UseCaseResult(
                FetchUserUseCaseSync.Status.SUCCESS, it
            )
        } ?: run {
            try {
                val endpointResult = fetchUserHttpEndpointSync.fetchUserSync(userId)
                if (isEndpointResultSuccessful(endpointResult)) {
                    val user = User(endpointResult.userId, endpointResult.username)
                    usersCache.cacheUser(user)
                    FetchUserUseCaseSync.UseCaseResult(
                        FetchUserUseCaseSync.Status.SUCCESS,
                        user
                    )
                } else
                    FetchUserUseCaseSync.UseCaseResult(
                        FetchUserUseCaseSync.Status.FAILURE, null
                    )
            } catch (e: NetworkErrorException) {
                FetchUserUseCaseSync.UseCaseResult(
                    FetchUserUseCaseSync.Status.NETWORK_ERROR, null
                )
            }
        }
    }

    private fun isEndpointResultSuccessful(
        endpointResult: FetchUserHttpEndpointSync.EndpointResult
    ): Boolean {
        return endpointResult.status == FetchUserHttpEndpointSync.EndpointStatus.SUCCESS
    }

}
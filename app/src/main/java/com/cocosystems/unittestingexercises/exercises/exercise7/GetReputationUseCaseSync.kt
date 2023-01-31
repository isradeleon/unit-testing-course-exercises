package com.cocosystems.unittestingexercises.exercises.exercise7

import com.cocosystems.unittestingexercises.exercises.exercise7.networking.GetReputationHttpEndpointSync

class GetReputationUseCaseSync(
    private val getReputationHttpEndpointSync: GetReputationHttpEndpointSync
) {
    enum class UseCaseStatus {
        SUCCESS,
        FAILURE
    }

    data class UseCaseResult(
        val status: UseCaseStatus,
        val reputation: Int
    )

    fun getReputation(): UseCaseResult {
        val fetchedReputation = getReputationHttpEndpointSync.reputationSync

        return when (fetchedReputation.status) {
            GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR -> UseCaseResult(
                UseCaseStatus.FAILURE,
                fetchedReputation.reputation
            )
            GetReputationHttpEndpointSync.EndpointStatus.NETWORK_ERROR -> UseCaseResult(
                UseCaseStatus.FAILURE,
                fetchedReputation.reputation
            )
            else -> UseCaseResult(
                UseCaseStatus.SUCCESS,
                fetchedReputation.reputation
            )
        }
    }
}
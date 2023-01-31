package com.cocosystems.unittestingexercises.exercise7

import com.cocosystems.unittestingexercises.exercises.exercise7.GetReputationUseCaseSync
import com.cocosystems.unittestingexercises.exercises.exercise7.GetReputationUseCaseSync.*
import com.cocosystems.unittestingexercises.exercises.exercise7.networking.GetReputationHttpEndpointSync
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetReputationUseCaseSyncTest {
    /**
     * SUT: System Under Test
     * */
    private lateinit var systemUnderTest: GetReputationUseCaseSync

    /**
     * SUT dependencies
     * */
    @Mock private lateinit var getReputationHttpEndpointSync: GetReputationHttpEndpointSync

    companion object {
        const val REPUTATION = 1
        const val ZERO_REPUTATION = 0
    }

    @Before
    fun setup() {
        systemUnderTest = GetReputationUseCaseSync(
            getReputationHttpEndpointSync
        )
        // Default success scenario
        successScenario()
    }

    @Test
    fun getReputation_endpointSuccess_successReturned() {
        val result = systemUnderTest.getReputation()
        assert(result.status == UseCaseStatus.SUCCESS)
    }

    @Test
    fun getReputation_endpointSuccess_reputationReturned() {
        val result = systemUnderTest.getReputation()
        assert(result.reputation == REPUTATION)
    }

    @Test
    fun getReputation_endpointGeneralError_failureReturned() {
        generalErrorScenario()

        val result = systemUnderTest.getReputation()
        assert(result.status == UseCaseStatus.FAILURE)
    }

    @Test
    fun getReputation_endpointNetworkError_failureReturned() {
        networkErrorScenario()

        val result = systemUnderTest.getReputation()
        assert(result.status == UseCaseStatus.FAILURE)
    }

    @Test
    fun getReputation_endpointGeneralError_zeroReputation() {
        generalErrorScenario()

        val result = systemUnderTest.getReputation()
        assert(result.reputation == ZERO_REPUTATION)
    }

    @Test
    fun getReputation_endpointNetworkError_zeroReputation() {
        networkErrorScenario()

        val result = systemUnderTest.getReputation()
        assert(result.reputation == ZERO_REPUTATION)
    }

    /**
     * Helper scenario methods
     * */
    private fun successScenario() {
        `when`(
            getReputationHttpEndpointSync.reputationSync
        ).thenReturn(
            GetReputationHttpEndpointSync.EndpointResult(
                GetReputationHttpEndpointSync.EndpointStatus.SUCCESS,
                REPUTATION
            )
        )
    }

    private fun generalErrorScenario() {
        `when`(
            getReputationHttpEndpointSync.reputationSync
        ).thenReturn(
            GetReputationHttpEndpointSync.EndpointResult(
                GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR,
                0
            )
        )
    }

    private fun networkErrorScenario() {
        `when`(
            getReputationHttpEndpointSync.reputationSync
        ).thenReturn(
            GetReputationHttpEndpointSync.EndpointResult(
                GetReputationHttpEndpointSync.EndpointStatus.NETWORK_ERROR,
                0
            )
        )
    }
}
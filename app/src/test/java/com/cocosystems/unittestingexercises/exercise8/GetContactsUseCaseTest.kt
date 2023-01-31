package com.cocosystems.unittestingexercises.exercise8

import com.cocosystems.unittestingexercises.exercises.exercise8.GetContactsUseCase
import com.cocosystems.unittestingexercises.exercises.exercise8.contacts.Contact
import com.cocosystems.unittestingexercises.exercises.exercise8.networking.ContactSchema
import com.cocosystems.unittestingexercises.exercises.exercise8.networking.GetContactsHttpEndpoint
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetContactsUseCaseTest {
    /**
     * System Under Test
     * */
    private lateinit var systemUnderTest: GetContactsUseCase

    /**
     * SUT dependencies
     * */
    @Mock private lateinit var getContactsHttpEndpoint: GetContactsHttpEndpoint
    @Mock private lateinit var listener1: GetContactsUseCase.Listener
    @Mock private lateinit var listener2: GetContactsUseCase.Listener

    /**
     * Mockito captors
     * */
    @Captor private lateinit var stringCaptor: ArgumentCaptor<String>
    @Captor private lateinit var contactsCaptor: ArgumentCaptor<List<Contact>>

    // Constants
    companion object {
        const val FILTER_TERM = "filter string"
        const val CONTACT_ID = "13"
        const val CONTACT_FULL_NAME = "John Wick"
        const val CONTACT_PHONE = "777223344"
        const val CONTACT_IMAGE = "profile image"
        const val CONTACT_AGE = 28.5
    }

    @Before
    fun setup() {
        systemUnderTest = GetContactsUseCase(
            getContactsHttpEndpoint
        )
        // Default endpoint scenario
        endpointSuccessScenario()
    }

    @Test
    fun fetchContacts_correctFilterPassedToEndpoint() {
        systemUnderTest.fetchContacts(FILTER_TERM)
        verify(
            getContactsHttpEndpoint,
            times(1)
        ).getContacts(
            stringCaptor.capture(),
            any()
        )
        assert(stringCaptor.value == FILTER_TERM)
    }

    @Test
    fun fetchContacts_endpointSuccess_observersNotifiedWithData() {
        systemUnderTest.registerListener(listener1)
        systemUnderTest.registerListener(listener2)
        systemUnderTest.fetchContacts(FILTER_TERM)

        verify(listener1).onContactsFetched(contactsCaptor.capture())
        verify(listener2).onContactsFetched(contactsCaptor.capture())

        val capture1 = contactsCaptor.allValues[0]
        val capture2 = contactsCaptor.allValues[1]

        assert(capture1 == getContacts())
        assert(capture2 == getContacts())
    }

    @Test
    fun fetchContacts_endpointSuccess_unregisteredObserversNotNotified() {
        systemUnderTest.registerListener(listener1)
        systemUnderTest.registerListener(listener2)
        systemUnderTest.unregisterListener(listener2)
        systemUnderTest.fetchContacts(FILTER_TERM)

        verify(listener1).onContactsFetched(any())
        verifyNoMoreInteractions(listener2)
    }

    @Test
    fun fetchContacts_endpointGeneralError_observersNotifiedOfFailure() {
        endpointGeneralErrorScenario()

        systemUnderTest.registerListener(listener1)
        systemUnderTest.registerListener(listener2)
        systemUnderTest.fetchContacts(FILTER_TERM)

        verify(listener1).onFetchContactsFailed()
        verify(listener2).onFetchContactsFailed()
    }

    @Test
    fun fetchContacts_endpointNetworkError_observersNotifiedOfNetworkError() {
        endpointNetworkErrorScenario()

        systemUnderTest.registerListener(listener1)
        systemUnderTest.registerListener(listener2)
        systemUnderTest.fetchContacts(FILTER_TERM)

        verify(listener1).onFetchNetworkError()
        verify(listener2).onFetchNetworkError()
    }

    /**
     * Helper scenario methods
     * */
    private fun endpointSuccessScenario() {
        doAnswer { invocation ->
            val callback = invocation.getArgumentAt(1, GetContactsHttpEndpoint.Callback::class.java)
            callback.onGetContactsSucceeded(
                getContactSchemas()
            )
            null
        }.`when`(getContactsHttpEndpoint).getContacts(any(), any())
    }

    private fun endpointGeneralErrorScenario() {
        doAnswer { invocation ->
            val callback = invocation.getArgumentAt(1, GetContactsHttpEndpoint.Callback::class.java)
            callback.onGetContactsFailed(
                GetContactsHttpEndpoint.FailReason.GENERAL_ERROR
            )
            null
        }.`when`(getContactsHttpEndpoint).getContacts(any(), any())
    }

    private fun endpointNetworkErrorScenario() {
        doAnswer { invocation ->
            val callback = invocation.getArgumentAt(1, GetContactsHttpEndpoint.Callback::class.java)
            callback.onGetContactsFailed(
                GetContactsHttpEndpoint.FailReason.NETWORK_ERROR
            )
            null
        }.`when`(getContactsHttpEndpoint).getContacts(any(), any())
    }

    private fun getContactSchemas() = mutableListOf(
        ContactSchema(
            CONTACT_ID,
            CONTACT_FULL_NAME,
            CONTACT_PHONE,
            CONTACT_IMAGE,
            CONTACT_AGE
        )
    )

    private fun getContacts() = mutableListOf(
        Contact(
            CONTACT_ID,
            CONTACT_FULL_NAME,
            CONTACT_IMAGE
        )
    )
}
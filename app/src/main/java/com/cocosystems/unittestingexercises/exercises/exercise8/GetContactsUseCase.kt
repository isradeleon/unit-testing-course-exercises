package com.cocosystems.unittestingexercises.exercises.exercise8

import com.cocosystems.unittestingexercises.exercises.exercise8.contacts.Contact
import com.cocosystems.unittestingexercises.exercises.exercise8.networking.ContactSchema
import com.cocosystems.unittestingexercises.exercises.exercise8.networking.GetContactsHttpEndpoint

class GetContactsUseCase(
    private val getContactsHttpEndpoint: GetContactsHttpEndpoint
) {
    /**
     * Observer design pattern
     * */
    interface Listener {
        fun onContactsFetched(contacts: List<Contact>?)
        fun onFetchContactsFailed()
        fun onFetchNetworkError()
    }

    private val mListeners = mutableListOf<Listener>()
    fun registerListener(listener: Listener) = mListeners.add(listener)
    fun unregisterListener(listener: Listener) = mListeners.remove(listener)

    /**
     * Use case entry point
     * */
    fun fetchContacts(filterTerm: String) {
        getContactsHttpEndpoint.getContacts(
            filterTerm,
            object : GetContactsHttpEndpoint.Callback {
                override fun onGetContactsSucceeded(contactSchemas: MutableList<ContactSchema>) {
                    val contacts = parseSchemasToContacts(contactSchemas)
                    mListeners.forEach { listener -> listener.onContactsFetched(contacts) }
                }

                override fun onGetContactsFailed(failReason: GetContactsHttpEndpoint.FailReason) {
                    when (failReason) {
                        GetContactsHttpEndpoint.FailReason.GENERAL_ERROR -> mListeners.forEach { listener ->
                            listener.onFetchContactsFailed()
                        }
                        GetContactsHttpEndpoint.FailReason.NETWORK_ERROR -> mListeners.forEach { listener ->
                            listener.onFetchNetworkError()
                        }
                    }
                }
            }
        )
    }

    private fun parseSchemasToContacts(
        schemas: List<ContactSchema>
    ) = schemas.map {
        Contact(
            it.id,
            it.fullName,
            it.imageUrl
        )
    }
}
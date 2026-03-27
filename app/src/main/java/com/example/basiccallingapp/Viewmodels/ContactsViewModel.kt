package com.example.basiccallingapp.Viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.basiccallingapp.Repositories.ContactsRepository
import com.example.basiccallingapp.models.ContactEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactsViewModel(private val repository: ContactsRepository) : ViewModel() {
    private val _contacts = MutableStateFlow<List<ContactEntry>>(emptyList())
    val contacts = _contacts.asStateFlow()

    // logic to load contacts
    fun loadContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.fetchContacts()
            _contacts.value = data
        }
    }

    class Factory(private val repository: ContactsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ContactsViewModel(repository) as T
        }
    }
}
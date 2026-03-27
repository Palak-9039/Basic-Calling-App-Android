package com.example.basiccallingapp.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.basiccallingapp.Viewmodel.ContactsViewModel
import com.example.basiccallingapp.model.ContactEntry

@Composable
fun ContactsScreen(
    contactsViewModel: ContactsViewModel,
    onContactClick: (String) -> Unit
) {

    val contacts by contactsViewModel.contacts.collectAsState()

    if (contacts.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No contacts found. Check permissions.")
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(contacts) { contact ->
                ContactItem(contact, onContactClick)
            }
        }
    }
}

@Composable
fun ContactItem(contact: ContactEntry, onContactClick: (String) -> Unit) {
    ListItem(
        headlineContent = { Text(contact.name) },
        supportingContent = { Text(contact.number) },
        leadingContent = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        modifier = Modifier.clickable { onContactClick(contact.number) }
    )
}
package com.example.basiccallingapp.Repositories

import android.content.Context
import android.provider.ContactsContract
import com.example.basiccallingapp.models.ContactEntry

class ContactsRepository(private val context: Context) {

    //to fetch contacts from the system
    fun fetchContacts(): List<ContactEntry> {
        val contactList = mutableListOf<ContactEntry>()
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
        )

        cursor?.use {
            val nameIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val name = it.getString(nameIdx) ?: "Unknown"
                val number = it.getString(numberIdx) ?: ""
                contactList.add(ContactEntry(name, number))
            }
        }
        return contactList
    }
}
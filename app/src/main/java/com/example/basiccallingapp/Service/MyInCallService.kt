package com.example.basiccallingapp.Service

import android.content.Intent
import android.telecom.Call
import android.telecom.InCallService
import com.example.basiccallingapp.MainActivity
import com.example.basiccallingapp.Repositories.CallManager
import com.example.basiccallingapp.Repositories.ContactsRepository

class MyInCallService : InCallService() {
    override fun onCallAdded(call: Call?) {
        super.onCallAdded(call)
        //updating the call object
        CallManager.updateCall(call)


        // intent for moving to the active call screen
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            putExtra("SHOW_ACTIVE_CALL", true)
        }
        startActivity(intent)

        // Perform lookup using the Repository
        val repository = ContactsRepository(this)
        val number = call?.details?.handle?.schemeSpecificPart ?: ""
        val name = repository.getContactNameByNumber(number)
        // Saving to Manager so ActiveCallScreen can see it
        CallManager.updateContactName(name)


    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
        // to update the call object when call is disconnected
        CallManager.updateCall(null)
    }
}
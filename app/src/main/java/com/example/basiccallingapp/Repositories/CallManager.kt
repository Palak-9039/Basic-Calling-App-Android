package com.example.basiccallingapp.Repositories

import android.telecom.Call
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object CallManager {
    // This holds the actual active call from the system
    private val _activeCall = MutableStateFlow<Call?>(null)
    val activeCall = _activeCall.asStateFlow()

    fun updateCall(call: Call?) {
        _activeCall.value = call
    }

    // Function to hang up the real call
    fun disconnect() {
        _activeCall.value?.disconnect()
        _activeCall.value = null
    }
}
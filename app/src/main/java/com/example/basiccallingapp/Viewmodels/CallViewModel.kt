package com.example.basiccallingapp.Viewmodels

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.telecom.TelecomManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basiccallingapp.Repositories.CallManager
import com.example.basiccallingapp.models.CallState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CallViewModel : ViewModel() {

    // managing the numbers typed on the Dial Pad
    var phoneNumber by mutableStateOf("")
        private set

    //ui state for mute button
    var isMuted by mutableStateOf(false)
        private set

    //ui state for speaker button
    var isSpeakerOn by mutableStateOf(false)
        private set

    // Event for Toast
    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    // managing the overall app state
    private val _uiState = MutableStateFlow<CallState>(CallState.Idle)
    val uiState = _uiState.asStateFlow()

    // Timer Job for the Active Call
    private var timerJob: Job? = null
    private var _seconds = MutableStateFlow(0)
    var seconds = _seconds.asStateFlow()


    // Logic for digit buttons
    fun onDigitClick(digit: String) {
        if (phoneNumber.length < 15) { // Basic limit for UI sanity
            phoneNumber += digit
        }
    }

    // logic for Backspace Button
    fun onBackspace() {
        if (phoneNumber.isNotBlank()) {
            phoneNumber = phoneNumber.dropLast(1)
        }
    }

    //triggered when Call is pressed
    fun StartOutgoingCall() {
        if (phoneNumber.isNotBlank()) {
            _uiState.value = CallState.Outgoing(phoneNumber)
        }
    }


    // to toggle mute button
    fun toggleMute() {
        isMuted = !isMuted
    }


    //to toggle speaker button
    fun toggleSpeaker() {
        isSpeakerOn = !isSpeakerOn
    }

    // Private helper to avoid repeating yourself
    private fun clearCallData() {
        timerJob?.cancel()
        timerJob = null
        _seconds.value = 0
        phoneNumber = ""
        _uiState.value = CallState.Idle
    }

    fun stopTimerOnly() {
        timerJob?.cancel()
        timerJob = null
        clearCallData()
    }


    // to make real phone call
    fun placeRealCall(context: Context, phoneNumber: String) {
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        val uri = Uri.fromParts("tel", phoneNumber, null)

        val extras = Bundle().apply {
            putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, false)
        }

        try {
            // This tells the system: "I'm the dialer, let me handle this"
            telecomManager.placeCall(uri, extras)
        } catch (e: SecurityException) {
            // Handle permission error
        }
    }

    fun startTimer() {
//       reset any existing timer
        timerJob?.cancel()
        _seconds.value = 0

        //the ticking logic
        timerJob = viewModelScope.launch {
            while (isActive) { //checking if the coroutine is still active
                delay(1000) //wait for 1 second
                _seconds.value += 1
            }
        }
    }

    // to get the real call seconds for active call screen
    fun observeRealCall() {
        viewModelScope.launch {
            CallManager.activeCall.collect { call ->
                if (call != null) {
                    // Listening for state changes
                    call.registerCallback(object : android.telecom.Call.Callback() {
                        override fun onStateChanged(call: android.telecom.Call, state: Int) {
                            if (state == android.telecom.Call.STATE_ACTIVE) {
                                startTimer() // Only starts when connected
                            }
                        }
                    })

                } else {
                    // Call ended: Stop timer and refresh logs
                    stopTimerOnly()
                }
            }
        }
    }

}

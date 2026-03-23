package com.example.basiccallingapp.Viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basiccallingapp.model.CallState
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

    // to check if the call is incoming simulation one or not
    var isRealSimCall by mutableStateOf(false)

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


    //logic for startActiveCall
    fun startActiveCall(isReal: Boolean = false) {

        isRealSimCall = isReal // Setting the flag

        //updating the state
        _uiState.value = CallState.Active(phoneNumber)

        //reset any existing timer
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

    // to toggle mute button
    fun toggleMute() {
        isMuted = !isMuted
    }


    //to toggle speaker button
    fun toggleSpeaker() {
        isSpeakerOn = !isSpeakerOn
    }

    //logic for making calls from sim
    fun initiateRealCall(context: Context) {
        val number = phoneNumber // The string the user typed in the DialPad
        if (number.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$number")
                // This flag is needed because we are starting the activity from a non-activity context (ViewModel)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            // Final safety check: ensuring the permission is actually granted before firing
            if (intent.resolveActivity(context.packageManager) != null) {
                try {
                    context.startActivity(intent)
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            } else {
                // Edge Case: Device has no calling hardware or dialer app
                Toast.makeText(
                    context,
                    "No calling application found on this device",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
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
    }

    fun endCall() {
        clearCallData()
        viewModelScope.launch {
            _toastEvent.emit("Call Ended")
        }
    }

    // Special function for the Auto-Return
    fun autoResetAfterCall() {
        viewModelScope.launch {
            _toastEvent.emit("Call Ended")
            delay(2000) // Give the user time to see the final duration
            clearCallData()
        }
    }

}

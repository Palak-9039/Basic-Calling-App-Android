package com.example.basiccallingapp.Viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basiccallingapp.model.CallState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CallViewModel : ViewModel() {

    // managing the numbers typed on the Dial Pad
    var inputNumber by mutableStateOf("")
        private set

    // managing the overall app state
    private val _uiState = MutableStateFlow<CallState>(CallState.Idle)
    val uiState = _uiState.asStateFlow()

    // Timer Job for the Active Call
    private var timerJob: Job? = null
    private var _seconds = MutableStateFlow(0)
    var seconds = _seconds.asStateFlow()

    // Logic for digit buttons
    fun onDigitClick(digit: String) {
        if (inputNumber.length < 15) { // Basic limit for UI sanity
            inputNumber += digit
        }
    }

    // logic for Backspace Button
    fun onBackspace() {
        if (inputNumber.isNotBlank()) {
            inputNumber = inputNumber.dropLast(1)
        }
    }

    //triggered when Call is pressed
    fun StartOutgoingCall() {
        if (inputNumber.isNotBlank()) {
            _uiState.value = CallState.Outgoing(inputNumber)
        }
    }


    //call end logic (resetting timer)
    fun endCall() {
        timerJob?.cancel()
        _uiState.value = CallState.Ended

        viewModelScope.launch {
            delay(1500)
            _uiState.value = CallState.Idle
            inputNumber = "" //clear the dialer
        }
    }

    //logic for startActiveCall
    fun startActiveCall() {
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

    fun stopActiveCall() {
        timerJob?.cancel()
        _seconds.value = 0
    }


}

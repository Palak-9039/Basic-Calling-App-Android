package com.example.basiccallingapp.models

sealed class CallState {
    data object Idle : CallState()
    data class Outgoing(val number: String) : CallState()
    data class Incoming(val number: String) : CallState()
    data class Active(val number: String) : CallState()
    data object Ended : CallState()
}
package com.example.basiccallingapp.model

sealed class CallState {
    object Idle : CallState()
    data class Outgoing(val number: String) : CallState()
    data class Incoming(val number: String) : CallState()
    data class Active(val seconds: Int) : CallState()
    object Ended : CallState()
}
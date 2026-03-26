package com.example.basiccallingapp.model

data class CallLogEntry (
    val name: String?,       // Contact name
    val number: String,      // Phone number
    val type: Int,           // Incoming/Outgoing/Missed
    val date: Long,          // Date & time
    val duration: String     // Call duration
)
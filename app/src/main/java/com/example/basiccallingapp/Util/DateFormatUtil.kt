package com.example.basiccallingapp.Util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    // "dd MMM, hh:mm a" produces something like "26 Mar, 01:34 PM"
    val formatter = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    return formatter.format(date)
}
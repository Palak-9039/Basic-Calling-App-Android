package com.example.basiccallingapp.Repositories

import android.content.Context
import android.provider.CallLog
import com.example.basiccallingapp.models.CallLogEntry

class CallLogRepository(private val context: Context) {
    fun getCallLogs(): List<CallLogEntry> {
        val callLogList = mutableListOf<CallLogEntry>()

        // Reading data from CallLog.Calls
        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            "${CallLog.Calls.DATE} DESC"
        )

        cursor?.use {
            val nameIdx = it.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val numberIdx = it.getColumnIndex(CallLog.Calls.NUMBER)
            val typeIdx = it.getColumnIndex(CallLog.Calls.TYPE)
            val dateIdx = it.getColumnIndex(CallLog.Calls.DATE)
            val durationIdx = it.getColumnIndex(CallLog.Calls.DURATION)

            while (it.moveToNext()) {
                callLogList.add(
                    CallLogEntry(
                        name = it.getString(nameIdx),
                        number = it.getString(numberIdx) ?: "Unknown",
                        type = it.getInt(typeIdx),
                        date = it.getLong(dateIdx),
                        duration = it.getString(durationIdx) ?: "0"
                    )
                )
            }
        }
        return callLogList
    }
}

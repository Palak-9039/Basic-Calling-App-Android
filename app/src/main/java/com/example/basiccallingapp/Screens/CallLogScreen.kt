package com.example.basiccallingapp.Screens

import android.provider.CallLog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallMissed
import androidx.compose.material.icons.filled.CallReceived
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.basiccallingapp.Util.formatDate
import com.example.basiccallingapp.Viewmodels.CallLogViewModel
import com.example.basiccallingapp.models.CallLogEntry

@Composable
fun CallLogScreen(viewModel: CallLogViewModel, onCallClick: (String) -> Unit) {
    val logs by viewModel.callLogs.collectAsState()

    if (logs.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No call logs found or permission denied.")
        }
    } else {
        LazyColumn {
            items(logs) { log ->
                CallLogItem(log, onCallClick)
            }
        }
    }
}

@Composable
fun CallLogItem(log: CallLogEntry, onCallClick: (String) -> Unit) {
    // Determine the label and color based on the type
    val (typeLabel, typeColor, typeIcon) = when (log.type) {
        CallLog.Calls.INCOMING_TYPE -> Triple("Incoming", Color(0xFF4CAF50), Icons.Default.CallReceived)
        CallLog.Calls.OUTGOING_TYPE -> Triple("Outgoing", Color(0xFF2196F3), Icons.Default.CallMade)
        CallLog.Calls.MISSED_TYPE -> Triple("Missed", Color.Red, Icons.Default.CallMissed)
        else -> Triple("Unknown", Color.Gray, Icons.Default.Call)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCallClick(log.number) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = log.name ?: "Unknown",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (log.type == CallLog.Calls.MISSED_TYPE) Color.Red else Color.Unspecified
                )
                Spacer(Modifier.width(8.dp))
                // Visual Indicator for Call Type
                Icon(
                    imageVector = typeIcon,
                    contentDescription = null,
                    tint = typeColor,
                    modifier = Modifier.size(14.dp)
                )
            }
            Text(text = log.number, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "$typeLabel • ${formatDate(log.date)} • ${log.duration}s",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Icon(Icons.Default.Call, contentDescription = "Call Back", tint = Color.Green.copy(alpha = 0.7f))
    }
}
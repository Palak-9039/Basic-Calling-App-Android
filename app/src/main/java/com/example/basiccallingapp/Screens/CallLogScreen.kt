package com.example.basiccallingapp.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
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
import com.example.basiccallingapp.Viewmodel.CallLogViewModel
import com.example.basiccallingapp.model.CallLogEntry

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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCallClick(log.number) }
    .padding(16.dp),
    verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = log.name ?: "Unknown", style = MaterialTheme.typography.titleMedium)
            Text(text = log.number, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "${formatDate(log.date)} • ${log.duration}s",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Icon(Icons.Default.Call, contentDescription = "Call Back", tint = Color.Green)
    }
}
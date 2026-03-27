package com.example.basiccallingapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.basiccallingapp.Viewmodels.CallViewModel

@Composable
fun IncomingCallScreen(navController: NavController, viewModel: CallViewModel) {
    val incomingNumber = "9876543210" // Hardcoded for simulation

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1C1E))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Incoming Call",
            color = Color.Gray,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = incomingNumber,
            color = Color.White,
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(150.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Reject Button
            FloatingActionButton(
                onClick = {
//                    viewModel.endCall()
//                    navController.popBackStack()
                },
                containerColor = Color.Red,
                shape = CircleShape
            ) {
                Icon(Icons.Default.CallEnd, contentDescription = "Reject", tint = Color.White)
            }

            // Accept Button
            FloatingActionButton(
                onClick = {
                    //to start the timer from 00:00
////                    viewModel.startActiveCall(isReal = false)
//                    navController.navigate(Screen.ActiveCall.route) {
//                        popUpTo(Screen.MainScreen.route)
//                    }
                },
                containerColor = Color.Green,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Call, contentDescription = "Accept", tint = Color.White)
            }
        }
    }
}
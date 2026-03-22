package com.example.basiccallingapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.basiccallingapp.Navigation.Screen
import com.example.basiccallingapp.Viewmodel.CallViewModel
import kotlinx.coroutines.delay

@Composable
fun OutgoingCallScreen(navController: NavController, viewModel: CallViewModel) {
    val dialedNumber = viewModel.inputNumber

    // Requirement: Simulate connecting to the call after a delay
    LaunchedEffect(Unit) {
        delay(3000) // Wait 3 seconds
        viewModel.startActiveCall() // Update state to 'Active'
        navController.navigate(Screen.ActiveCall.route) {
            popUpTo(Screen.DialPad.route) // Remove 'Outgoing' from the backstack
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1C1E)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Calling...", color = Color.Gray, style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = dialedNumber,
            color = Color.White,
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(modifier = Modifier.height(550.dp))

        // End Call Button
        FloatingActionButton(
            onClick = {
                viewModel.endCall()
                navController.popBackStack()
            },
            containerColor = Color.Red
        ) {
            Icon(Icons.Default.CallEnd, contentDescription = "End Call", tint = Color.White)
        }
    }
}

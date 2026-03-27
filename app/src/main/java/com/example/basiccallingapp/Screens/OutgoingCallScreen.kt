package com.example.basiccallingapp.Screens

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.basiccallingapp.Navigation.Screen
import com.example.basiccallingapp.Viewmodels.CallViewModel
import kotlinx.coroutines.delay

@Composable
fun OutgoingCallScreen(navController: NavController, viewModel: CallViewModel) {
    val dialedNumber = viewModel.phoneNumber
    val context: Context = LocalContext.current



    LaunchedEffect(Unit) {
        delay(1000) // Wait 1 seconds

//        viewModel.startActiveCall(isReal = true)
//        viewModel.initiateRealCall(context)

        navController.navigate(Screen.ActiveCall.route) {
            popUpTo(Screen.MainScreen.route) // Remove 'Outgoing' from the backstack
        }
    }


    // Screen UI
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
//                viewModel.endCall()
//                navController.popBackStack()
            },
            containerColor = Color.Red
        ) {
            Icon(Icons.Default.CallEnd, contentDescription = "End Call", tint = Color.White)
        }
    }
}

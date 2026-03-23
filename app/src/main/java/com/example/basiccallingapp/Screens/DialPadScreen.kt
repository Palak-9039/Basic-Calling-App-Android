package com.example.basiccallingapp.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.basiccallingapp.Navigation.Screen
import com.example.basiccallingapp.Viewmodel.CallViewModel

@Composable
fun DialPadScreen(navController: NavController, viewModel: CallViewModel) {
    //collecting the entered number from the viewmodel
    val phoneNumber = viewModel.phoneNumber


    // screen UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Input Number Display
        Text(
            text = phoneNumber,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 64.dp),
            maxLines = 1
        )

        // Numeric Keypad
        val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#")

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.width(280.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(keys) { key ->
                    DialButton(key) { viewModel.onDigitClick(key) }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons (Call & Backspace)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                // Backspace Button
                IconButton(onClick = { viewModel.onBackspace() }) {
                    Icon(
                        Icons.Default.Backspace,
                        contentDescription = "Delete",
                        tint = Color.Gray,
                        modifier = Modifier.size(30.dp)
                    )
                }

                // Call Button
                FloatingActionButton(
                    onClick = {
                        if (phoneNumber.isNotEmpty()) {
                            //starting outgoing call
                            viewModel.StartOutgoingCall()

                            //navigating to the screen
                            navController.navigate(Screen.OutgoingCall.route)
                        }
                    },
                    containerColor = Color(0xFF4CAF50) // Material Green
                ) {
                    Icon(Icons.Default.Call, contentDescription = "Call", tint = Color.White)
                }

                // Empty box for symmetry
                Box(modifier = Modifier.size(48.dp))
            }
        }
    }
}

@Composable
fun DialButton(text: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = Color.LightGray.copy(alpha = 0.2f),
        modifier = Modifier.size(72.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
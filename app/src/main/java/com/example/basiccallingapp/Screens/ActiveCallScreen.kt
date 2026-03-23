package com.example.basiccallingapp.Screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.basiccallingapp.Navigation.Screen
import com.example.basiccallingapp.Util.formatTime
import com.example.basiccallingapp.Viewmodel.CallViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ActiveCallScreen(
    navController: NavController,
    viewModel: CallViewModel
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current



    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {

                //only reset if it's returning from a real call
                if (viewModel.isRealSimCall) {


                    lifecycleOwner.lifecycleScope.launch {

                        delay(150) // A small buffer to let the UI settle

                        viewModel.stopTimerOnly()
                        viewModel.autoResetAfterCall()

                        // handling the crash
                        try {
                            navController.navigate(Screen.DialPad.route) {
                                popUpTo(Screen.DialPad.route) { inclusive = true }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


// Listening for the events
    LaunchedEffect(Unit) {
        viewModel.toastEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }


    // Collecting the ticking seconds from the ViewModel
    val seconds by viewModel.seconds.collectAsState()
    val dialedNumber = viewModel.phoneNumber


    // screen UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1C1E))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Contact Info
        Text(
            text = if (viewModel.isRealSimCall) dialedNumber else "9876543210",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )

        // Timer display
        Text(
            text = formatTime(seconds),
            style = MaterialTheme.typography.displayLarge,
            color = Color.White.copy(alpha = 0.9f),
            modifier = Modifier.padding(vertical = 32.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // UI Toggles
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            //mute button
            CallActionIcon(
                icon = if (viewModel.isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                label = if (viewModel.isMuted) "Muted" else "Mute",
                isActive = viewModel.isMuted,
                onClick = { viewModel.toggleMute() }
            )

            // speaker button
            CallActionIcon(
                icon = if (viewModel.isSpeakerOn) Icons.Default.VolumeUp else Icons.Default.VolumeDown,
                label = if (viewModel.isSpeakerOn) "Speaker On" else "Speaker",
                isActive = viewModel.isSpeakerOn,
                onClick = { viewModel.toggleSpeaker() }
            )

            //dial pad button
            CallActionIcon(icon = Icons.Default.Dialpad, label = "Keypad", isActive = false) {}
        }

        Spacer(modifier = Modifier.height(100.dp))

        // End Call Button
        FloatingActionButton(
            onClick = {
                viewModel.endCall()
                navController.navigate(Screen.DialPad.route) {
                    popUpTo(Screen.DialPad.route) { inclusive = true }
                }
            },
            containerColor = Color.Red,
            shape = CircleShape
        ) {
            Icon(
                Icons.Default.CallEnd,
                contentDescription = "End Call",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun CallActionIcon(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    if (isActive) Color.White else Color.White.copy(alpha = 0.1f),
                    CircleShape
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon, contentDescription = label,
                tint = if (isActive) Color.Black else Color.White
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, color = Color.Gray, style = MaterialTheme.typography.labelSmall)
    }
}

package com.example.basiccallingapp.Navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.basiccallingapp.Screens.ActiveCallScreen
import com.example.basiccallingapp.Screens.DialPadScreen
import com.example.basiccallingapp.Screens.OutgoingCallScreen
import com.example.basiccallingapp.Viewmodel.CallViewModel

@Composable
fun navigation() {

    val navController = rememberNavController()
    val viewmodel: CallViewModel = viewModel()


    NavHost(
        navController = navController,
        startDestination = Screen.DialPad.route
    ) {

        composable(Screen.DialPad.route) {
            DialPadScreen(navController, viewmodel)
        }

        composable(Screen.OutgoingCall.route) {
            OutgoingCallScreen(navController, viewmodel)
        }

        composable(Screen.ActiveCall.route) {
            ActiveCallScreen(navController, viewmodel)
        }

        composable(Screen.IncomingCall.route) {

        }


    }
}
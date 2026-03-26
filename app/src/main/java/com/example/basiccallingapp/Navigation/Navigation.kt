package com.example.basiccallingapp.Navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.basiccallingapp.Repository.CallLogRepository
import com.example.basiccallingapp.Screens.ActiveCallScreen
import com.example.basiccallingapp.Screens.DialPadScreen
import com.example.basiccallingapp.Screens.IncomingCallScreen
import com.example.basiccallingapp.Screens.MainScreen
import com.example.basiccallingapp.Screens.OutgoingCallScreen
import com.example.basiccallingapp.Viewmodel.CallLogViewModel
import com.example.basiccallingapp.Viewmodel.CallViewModel

@Composable
fun navigation() {
    val context = LocalContext.current
    val navController = rememberNavController()

    // callViewModel
    val callViewmodel: CallViewModel = viewModel()

    // Initialize the Repository manually
    val callLogRepository = remember { CallLogRepository(context) }

    // Use the Factory to create the CallLogViewModel
    val callLogViewModel: CallLogViewModel = viewModel(
        factory = CallLogViewModel.Factory(callLogRepository)
    )


    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route,
        //adding animation
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        }

    ) {
        composable(Screen.MainScreen.route){
            MainScreen(navController,callViewmodel,callLogViewModel)
        }

        composable(Screen.DialPad.route) {
            DialPadScreen(navController, callViewmodel)
        }

        composable(Screen.OutgoingCall.route) {
            OutgoingCallScreen(navController, callViewmodel)
        }

        composable(Screen.ActiveCall.route) {
            ActiveCallScreen(navController, callViewmodel)
        }

        composable(route = Screen.IncomingCall.route,
            enterTransition = { // applying animations
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(400)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(400)
                )
            }
        ) {
            IncomingCallScreen(navController, callViewmodel)
        }
    }
}
package com.example.basiccallingapp.Navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.basiccallingapp.Screens.ActiveCallScreen
import com.example.basiccallingapp.Screens.DialPadScreen
import com.example.basiccallingapp.Screens.IncomingCallScreen
import com.example.basiccallingapp.Screens.OutgoingCallScreen
import com.example.basiccallingapp.Viewmodel.CallViewModel

@Composable
fun navigation() {

    val navController = rememberNavController()
    val viewmodel: CallViewModel = viewModel()


    NavHost(
        navController = navController,
        startDestination = Screen.DialPad.route,
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

        composable(Screen.DialPad.route) {
            DialPadScreen(navController, viewmodel)
        }

        composable(Screen.OutgoingCall.route) {
            OutgoingCallScreen(navController, viewmodel)
        }

        composable(Screen.ActiveCall.route) {
            ActiveCallScreen(navController, viewmodel)
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
            IncomingCallScreen(navController, viewmodel)
        }
    }
}
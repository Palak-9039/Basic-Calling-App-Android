package com.example.basiccallingapp

import android.app.role.RoleManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.basiccallingapp.Navigation.Screen
import com.example.basiccallingapp.Navigation.navigation
import com.example.basiccallingapp.ui.theme.BasicCallingAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    // Defining the launcher to handle the user's choice
    private val roleLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // User made the app as default dialer!
        }
    }

    private lateinit var navController: NavHostController


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // If a call is added while the app is open, this triggers
        if (intent.getBooleanExtra("SHOW_ACTIVE_CALL", false)) {
            navController.navigate(Screen.ActiveCall.route) {
                // Ensuring we don't pile up screens
                popUpTo(Screen.MainScreen.route) { inclusive = false }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Triggering the request
        checkAndRequestDialerRole()

        setContent {

            navController = rememberNavController()
            // Use LaunchedEffect to react to the Intent from the Service
            LaunchedEffect(intent) {
                delay(300) // Small delay to allow NavHost to attach fully
                if (intent?.getBooleanExtra("SHOW_ACTIVE_CALL", false) == true) {
                    navController.navigate(Screen.ActiveCall.route)
                }
            }

            BasicCallingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navigation(navController)
                }
            }
        }
    }

    // to check and request to set the app as default dialer app
    private fun checkAndRequestDialerRole() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(RoleManager::class.java)
            if (roleManager.isRoleAvailable(RoleManager.ROLE_DIALER) &&
                !roleManager.isRoleHeld(RoleManager.ROLE_DIALER)
            ) {

                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
                roleLauncher.launch(intent)
            }
        }
    }
}


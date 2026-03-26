package com.example.basiccallingapp.Screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.basiccallingapp.Viewmodel.CallLogViewModel
import com.example.basiccallingapp.Viewmodel.CallViewModel

@Composable
fun MainScreen(
    navController: NavController,
    callViewModel: CallViewModel,
    callLogViewModel: CallLogViewModel
) {

    var selectedTab by remember { mutableIntStateOf(0) }


    val context = LocalContext.current
    val permissions = arrayOf(
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_PHONE_STATE
    )

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if (result.values.all { it }) {
            callLogViewModel.loadCallLogs()
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(permissions)
    }

    Scaffold(
        bottomBar = {
                MyBottomBar(  // Passing the state and the logic down to the bar
                    selectedTab = selectedTab,
                    onTabSelected = { index ->
                        selectedTab = index
                        if (index == 1) callLogViewModel.loadCallLogs()
                    }
                )
            }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            // Your Navigation Host will go here
            // Switch between screens based on the selected tab
            when (selectedTab) {
                0 -> DialPadScreen(navController, callViewModel)
                1 -> CallLogScreen(callLogViewModel) { phoneNumber ->
                    // Logic to place real call from logs
                    callViewModel.onCallLogClick(phoneNumber, context)
                }
            }
        }
    }
}



    @Composable
    fun MyBottomBar(
        selectedTab : Int,
        onTabSelected: (Int) -> Unit
    ) {
        NavigationBar {
//            var selectedTab = null
            NavigationBarItem(
                selected = selectedTab == 0,
                onClick = { onTabSelected(0)},
                label = { Text("Dialer") },
                icon = { Icon(Icons.Default.Dialpad, contentDescription = "Dialer") }
            )
            NavigationBarItem(
                selected = selectedTab == 1,
                onClick = {onTabSelected(1) },
                label = { Text("History") },
                icon = { Icon(Icons.Default.History, contentDescription = "Call Logs") }
            )
        }
    }

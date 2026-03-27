package com.example.basiccallingapp.Screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
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
import com.example.basiccallingapp.Viewmodels.CallLogViewModel
import com.example.basiccallingapp.Viewmodels.CallViewModel
import com.example.basiccallingapp.Viewmodels.ContactsViewModel

@Composable
fun MainScreen(
    navController: NavController,
    callViewModel: CallViewModel,
    callLogViewModel: CallLogViewModel,
    contactsViewModel: ContactsViewModel
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
                    if (index == 2) contactsViewModel.loadContacts()
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            // Switch between screens based on the selected tab
            when (selectedTab) {
                0 -> DialPadScreen(navController, callViewModel)
                1 -> CallLogScreen(callLogViewModel) { phoneNumber ->
                    // Logic to place real call from logs
                    callViewModel.placeRealCall(context, phoneNumber)
                }

                2 -> ContactsScreen(contactsViewModel) { phoneNumber ->
                    // Logic to place real call from logs
                    callViewModel.placeRealCall(context, phoneNumber)
                }
            }
        }
    }
}


@Composable
fun MyBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            label = { Text("Dialer") },
            icon = { Icon(Icons.Default.Dialpad, contentDescription = "Dialer") }
        )
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            label = { Text("History") },
            icon = { Icon(Icons.Default.History, contentDescription = "Call Logs") }
        )
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            label = { Text("Contacts") },
            icon = { Icon(Icons.Default.Contacts, contentDescription = "Contacts") }
        )
    }
}

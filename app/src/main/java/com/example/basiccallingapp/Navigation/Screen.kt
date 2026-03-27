package com.example.basiccallingapp.Navigation

    sealed class Screen(val route: String) {
        object MainScreen : Screen("main_screen")
        object DialPad : Screen("dial_pad")
        object OutgoingCall : Screen("outgoing_call")
        object IncomingCall : Screen("incoming_call")
        object ActiveCall : Screen("active_call")
        object ContactsScreen : Screen("contacts")
    }

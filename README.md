# Basic-Calling-App-Android

##  App Preview
<p align="center">
  <img src="screenshots/dialer.jpeg" width="30%" />
  <img src="screenshots/active.jpeg" width="30%" />
  <img src="screenshots/incoming.jpeg" width="30%" />
</p>


A professional-grade Android Dialer built with **Jetpack Compose** and **Clean Architecture**, leveraging the **Android Telecom Framework** for native system integration.

## Features
* **Native Telecom Integration:** Implements InCallService and ConnectionService to manage real hardware calls directly within the app UI.
* **Real-time Call Monitoring:** Displays live call duration by observing real-time system call states.
* **Device Data Sync:** Full integration with ContactsContract and CallLog providers to fetch real device data.
* **Default Dialer Support:** Implements the Android Role Manager flow to allow the app to act as the system's primary phone handler.
* **Premium UI:** Material 3 components with optimized 60fps slide transitions.
  
## Tech Stack
* **Language:** Kotlin(Coroutines/Flow)
* **UI:** Jetpack Compose (Material 3)
* **Navigation:** Compose Navigation with Animated Transitions
* **State Management:** ViewModel with StateFlow/SharedFlow
* **Architecture:** MVVM with Repository Pattern
* **API/Frameworks:** Android Telecom Manager, Content Resolvers, Role Manager API

## Key Technical Challenges
1. **Telecom Lifecycle Management:** Moving beyond simple Intents, I implemented a custom InCallService. This allowed the app to "bind" to the system's calling engine, ensuring the custom UI remains active and synced with the hardware line state.
2. **System Role Negotiation:** Navigating the Android 10+ security requirements, I integrated the RoleManager API to gracefully handle the "Default Dialer" handshake, ensuring a seamless user transition from the system dialer to my application.
3. **Data Layer Concurrency:** Efficiently fetching large volumes of Contacts and Call Logs using ContentResolver on background threads (Dispatchers.IO) to prevent UI jank and ensure 60fps performance during list scrolling.


## Screen Flow
1. **Dialer**:T9-style keypad with real-time phone number formatting.
2. **Contacts**: Alphabetical list of device contacts with instant-call functionality.
3. **History**: Real-time call logs showing incoming, outgoing, and missed calls.
4. **Active Call**: Professional calling interface with a live duration timer and hardware-linked disconnect.


## How to Run
1. Clone the repository.
2. Open in Android Studio Ladybug (or newer).
3. **Important**: On the first launch, allow the "Default Phone App" request.
4. Ensure you have a SIM card installed for the "Real Call" feature, or use the "Simulate" button on the DialPad to test the UI flow on an emulator.  

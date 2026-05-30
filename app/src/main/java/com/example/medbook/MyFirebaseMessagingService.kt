package com.example.medbook

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService :
    FirebaseMessagingService() {

    override fun onNewToken(
        token: String
    ) {

        super.onNewToken(token)

        Log.d(
            "FCM_TOKEN",
            token
        )
    }

    override fun onMessageReceived(
        remoteMessage: RemoteMessage
    ) {

        super.onMessageReceived(
            remoteMessage
        )

        Log.d(
            "FCM_MESSAGE",
            remoteMessage.notification?.body
                ?: ""
        )
    }
}
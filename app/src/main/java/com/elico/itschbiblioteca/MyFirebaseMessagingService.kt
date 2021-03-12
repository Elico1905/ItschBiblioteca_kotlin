package com.elico.itschbiblioteca

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {

        Looper.prepare()

        Handler().post {
            Toast.makeText(baseContext,p0.notification?.body , Toast.LENGTH_SHORT).show()
        }
        Looper.loop()
    }


}
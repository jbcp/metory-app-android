package com.devmon.crcp.push

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import timber.log.Timber

class FirebaseTokenReceiver(private val coroutineScope: CoroutineScope) {

    suspend fun getToken(): String? {
        val channel = Channel<String>()

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.w("Fetching FCM registration token failed: ${task.exception}")
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result ?: return@addOnCompleteListener

            coroutineScope.launch {
                channel.send(token)
                channel.close()
            }
        }

        for (s in channel) {
            return s
        }

        return null
    }
}
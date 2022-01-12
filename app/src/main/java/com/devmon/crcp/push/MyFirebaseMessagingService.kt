package com.devmon.crcp.push

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher
import androidx.lifecycle.lifecycleScope
import com.devmon.crcp.CRCPApplication
import com.devmon.crcp.R
import com.devmon.crcp.domain.repository.MemberRepository
import com.devmon.crcp.ui.chat.ChatDetailFragment
import com.devmon.crcp.ui.splash.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService(), LifecycleOwner {

    private val dispatcher = ServiceLifecycleDispatcher(this)

    override fun onCreate() {
        dispatcher.onServicePreSuperOnCreate()
        super.onCreate()
    }

    override fun onStart(intent: Intent?, startId: Int) {
        dispatcher.onServicePreSuperOnStart()
        super.onStart(intent, startId)
    }

    override fun onDestroy() {
        dispatcher.onServicePreSuperOnDestroy()
        super.onDestroy()
    }

    override fun onMessageReceived(rm: RemoteMessage) {
        super.onMessageReceived(rm)

        Timber.d("onMessageReceived $rm")

        if (rm.data.isNotEmpty()) {
            Timber.d("data is not empty")
            val data = rm.data
            val title = data["title"]
            val content = data["body"]
            val page = data["page"] ?: Page.NONE.code

            val message = Message(
                title = title ?: "",
                body = content ?: "",
                page = Page.of(page),
            )
            handleNow(message)
        }

        rm.notification?.let {
            Timber.d("notification push")

            val message = Message(
                title = it.title ?: "",
                body = it.body ?: "",
            )
            handleNow(message)
            Timber.d("notification is not empty")
            return
        }
        Timber.d("notification is empty")
    }

    private fun handleNow(message: Message) {
        if (message.title.isEmpty() || message.body.isEmpty()) {
            return
        }

        NotificationChannelFactory(this).createChannel()

        val intent = Intent(this, SplashActivity::class.java).apply {
            putExtra("page", message.page)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, NotificationChannelFactory.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round_crcp)
            .apply {
                val icon = BitmapFactory.decodeResource(this@MyFirebaseMessagingService.resources,
                    R.mipmap.ic_launcher_crcp)
                setLargeIcon(icon)
            }
            .setColor(ContextCompat.getColor(this, R.color.color_white))
            .setColorized(true)
            .setContentTitle(message.title)
            .setContentText(message.body)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationId = 3000

        if (getCurrentScreen() == ChatDetailFragment::class.java.simpleName) {
            return
        }

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    private fun getCurrentScreen(): String {
        return (application as? CRCPApplication)?.currentScreen ?: ""
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        lifecycleScope.launch {
            val pushEntryPoint =
                EntryPointAccessors.fromApplication(applicationContext, PushEntryPoint::class.java)
            val memberRepository = pushEntryPoint.memberRepository()

            val user = memberRepository.getUserInfo()
            if (user == null || user.applmail.isNullOrEmpty()) {
                return@launch
            }
            memberRepository.sendPushToken(user.applmail, token)
        }
    }

    override fun getLifecycle(): Lifecycle {
        return dispatcher.lifecycle
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface PushEntryPoint {
        fun memberRepository(): MemberRepository
    }
}
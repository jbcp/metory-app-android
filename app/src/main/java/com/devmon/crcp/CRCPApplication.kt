package com.devmon.crcp

import android.app.Application
import com.devmon.crcp.domain.repository.MemberRepository
import com.devmon.crcp.push.FirebaseTokenReceiver
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltAndroidApp
class CRCPApplication : Application() {

    private val tokenReceiver: FirebaseTokenReceiver = FirebaseTokenReceiver(MainScope())

    var currentScreen: String = ""

    @Inject
    lateinit var memberRepository: MemberRepository

    var isDebug: Boolean = false
        @Inject set

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        if (isDebug) {
            Timber.plant(Timber.DebugTree())
        }
    }

    fun sendPushToken() {
        MainScope().launch {
            val token = tokenReceiver.getToken() ?: return@launch
            val user = memberRepository.getUserInfo()
            if (user == null || user.applmail.isNullOrEmpty()) {
                return@launch
            }
            try {
                memberRepository.sendPushToken(user.applmail, token)
            } catch (e: Exception) {
                Timber.w(e)
            }

        }
    }
}
package com.devmon.crcp.data.datasource

import android.content.SharedPreferences
import androidx.core.content.edit
import com.devmon.crcp.data.network.response.UserResponse
import com.google.gson.Gson

class MemberDataSourceImpl(private val sharedPreferences: SharedPreferences) : MemberDataSource {
    override fun setAutoLogin(isAutoLogin: Boolean) {
        sharedPreferences.edit {
            putBoolean(AUTO_LOGIN, isAutoLogin)
        }
    }

    override fun getAutoLogin(): Boolean {
        return sharedPreferences.getBoolean(AUTO_LOGIN, false)
    }

    override fun setUserInfo(userResponse: UserResponse?) {
        sharedPreferences.edit {
            putString(USER_INFO, Gson().toJson(userResponse))
        }
    }

    override fun getUserInfo(): UserResponse? {
        return if (sharedPreferences.getString(USER_INFO, null) == null) {
            null
        } else {
            Gson().fromJson(sharedPreferences.getString(USER_INFO, ""), UserResponse::class.java)
        }
    }

    override fun getPassword(): String {
        return sharedPreferences.getString(PASSWORD, "") ?: ""
    }

    override fun setPassword(password: String) {
        sharedPreferences.edit {
            putString(PASSWORD, password)
        }
    }

    override fun saveSiteIp(ip: String) {
        sharedPreferences.edit {
            putString(SITE_IP, ip)
        }
    }

    override fun getSiteIp(): String {
        return sharedPreferences.getString(SITE_IP, "") ?: ""
    }

    override fun saveSubjectIp(ip: String) {
        sharedPreferences.edit {
            putString(SUBJECT_IP, ip)
        }
    }

    override fun getSubjectIp(): String {
        return sharedPreferences.getString(SUBJECT_IP, "") ?: ""
    }

    companion object {
        const val AUTO_LOGIN = "AUTO_LOGIN"
        const val USER_INFO = "USER_INFO"
        const val PASSWORD = "PASSWORD"
        const val SITE_IP = "SITE_IP"
        const val SUBJECT_IP = "SUBJECT_IP"
    }
}
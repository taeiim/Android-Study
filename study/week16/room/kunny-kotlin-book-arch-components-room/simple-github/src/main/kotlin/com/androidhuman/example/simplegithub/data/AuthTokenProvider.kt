package com.androidhuman.example.simplegithub.data

import android.content.Context
import android.preference.PreferenceManager

class AuthTokenProvider(private val context: Context) {

    companion object {

        private const val KEY_AUTH_TOKEN = "auth_token"
    }

    fun updateToken(token: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(KEY_AUTH_TOKEN, token)
                .apply()
    }

    val token: String?
        get() = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_AUTH_TOKEN, null)
}

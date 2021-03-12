package com.irvan.movieku.sessions

import android.content.Context
import android.util.Log
import com.irvan.movieku.BuildConfig
import com.irvan.movieku.database.AppDatabase
import com.irvan.movieku.utils.App


class SessionManager(context: Context) {
    private val TAG = "SessionManager";

    init {
        val db = AppDatabase.getInstance(App.get(context))
    }

    private val rootPackage: String = BuildConfig.APPLICATION_ID
    private val sharedPreferences =
        context.getSharedPreferences("${rootPackage}_setSession", Context.MODE_PRIVATE)

    private fun writeLoginStatus(status: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("${rootPackage}_statusLogin", status)
        editor.apply()
    }

    fun readLoginStatus(): Boolean =
        sharedPreferences.getBoolean("${rootPackage}_statusLogin", false)

    fun setUserSession(
        token: String?
    ) {
        writeLoginStatus(true)
        this.token = token
    }

    fun logUserSession() {
        if (BuildConfig.DEBUG && readLoginStatus()) {
            Log.d(TAG, "logUserSession: session token ${token}")
        }
    }

    fun setLogoutSession() {
        writeLoginStatus(false)
        token = null
    }

    var token: String?
        get() = sharedPreferences.getString(rootPackage + "_token", null)
        private set(data) {
            val editor = sharedPreferences.edit()
            editor.putString(rootPackage + "_token", data)
            editor.apply()
        }

    fun generateSession() : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..64)
            .map { allowedChars.random() }
            .joinToString("")
    }
}
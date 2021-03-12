package com.irvan.movieku.utils

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.irvan.movieku.receivers.BootCompletedIntentReceiver
import com.irvan.movieku.receivers.RestartBroadcastReceiver
import com.irvan.movieku.receivers.TimeoutToastReceiver
import com.irvan.movieku.services.AppService
import com.irvan.movieku.sessions.SessionManager


class App : Application() {
    private val TAG = "AppApplication"

    override fun onCreate() {
        super.onCreate()
        instance = this
        startService()
        startReceiver()
    }

    fun startService() {
        val intent = Intent(instance!!.applicationContext, AppService::class.java)
        val sessionManager = SessionManager(applicationContext)
        if (sessionManager.readLoginStatus()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                startService(intent)
            } else {
                startForegroundService(intent)
            }
        }
    }

    fun stopService() {
        instance?.let {
            stopService(Intent(it.applicationContext, AppService::class.java))
        }
    }

    fun startReceiver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val completedIntentReceiver = BootCompletedIntentReceiver()
            val intentBootComplete = IntentFilter()
            intentBootComplete.addAction("android.intent.action.BOOT_COMPLETED")
            registerReceiver(completedIntentReceiver, intentBootComplete)
            val restartBroadcastReceiver = RestartBroadcastReceiver()
            val intentRestart = IntentFilter()
            registerReceiver(restartBroadcastReceiver, intentRestart)
        }
    }

    fun broadcastTimeout() {
        instance?.let {
            val intent = Intent(it.applicationContext, TimeoutToastReceiver::class.java)
            sendBroadcast(intent)
        }
    }

    companion object {
        @get:Synchronized
        var instance: App? = null
            private set

        operator fun get(context: Context): App {
            return context.applicationContext as App
        }
    }
}
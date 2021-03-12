package com.irvan.movieku.receivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.irvan.movieku.BuildConfig
import com.irvan.movieku.sessions.SessionManager
import com.irvan.movieku.utils.App

class RestartBroadcastReceiver : BroadcastReceiver() {
    private val TAG = "RestartBroadcastReceive"

    override fun onReceive(context: Context, intent: Intent) {
        val sessionManager = SessionManager(context)
        if (sessionManager.readLoginStatus()) {
            Log.i(TAG, "Restart: Service Stops! Oooooooooooooppppssssss!!!!")
            App.get(context).onCreate()

            try {
                val intents = Intent(context, RestartBroadcastReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intents,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                val alarmManager =
                    (context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager)

                if (Build.VERSION.SDK_INT >= 26) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + 15 * 60 * 1000,
                        pendingIntent
                    )
                } else {
                    alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + 5000,
                        60 * 1000,
                        pendingIntent
                    )
                }
                Log.d(TAG, "onReceive: alarm active")
            } catch (e: Throwable) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "onReceive: ", e)
                    Log.e(TAG, "onReceive: alarm error")
                }
            }
        }
    }
}
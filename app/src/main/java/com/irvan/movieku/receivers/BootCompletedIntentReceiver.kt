package com.irvan.movieku.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.irvan.movieku.sessions.SessionManager


class BootCompletedIntentReceiver : BroadcastReceiver() {
    private val TAG = "BootCompletedIntentRece"

    override fun onReceive(context: Context, intent: Intent) {
        val sessionManager = SessionManager(context)
        if (sessionManager.readLoginStatus()) {
            if ("android.intent.action.BOOT_COMPLETED" == intent.action) {
                Log.i(TAG, "Boot Complete: Service Stops! Oooooooooooooppppssssss!!!!")
                val broadcastIntent = Intent(context, RestartBroadcastReceiver::class.java)
                context.sendBroadcast(broadcastIntent)
            }
        }
    }
}
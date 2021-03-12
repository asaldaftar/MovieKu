package com.irvan.movieku.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.irvan.movieku.services.AppService
import com.irvan.movieku.sessions.SessionManager

class TimeoutToastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val sessionManager = SessionManager(context)
        if (sessionManager.readLoginStatus()) {
            if (!AppService.isTimeout) {
                AppService.isTimeout = true
                Toast.makeText(context, "Koneksi timeout", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
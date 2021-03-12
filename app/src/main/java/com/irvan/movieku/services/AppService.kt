package com.irvan.movieku.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.irvan.movieku.BuildConfig
import com.irvan.movieku.receivers.RestartBroadcastReceiver
import com.irvan.movieku.sessions.SessionManager
import java.net.Socket
import java.util.*

class AppService : Service() {

    companion object {
        const val TAG = "AppServiceClass"

        var isServiceRunning = false
        var isConnected = false
        var isTimeout = false

        fun killSocket() {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "killSocket: ")
            }
        }
    }

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    private lateinit var sessionManager: SessionManager

    override fun onCreate() {
        super.onCreate()
        if (!isServiceRunning) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onCreate: ")
            }
            sessionManager = SessionManager(applicationContext)
            if (sessionManager.readLoginStatus()) {
                startTimer()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onStartCommand: ")
        }
        startTimer()
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onTaskRemoved: ")
        }
        if (sessionManager.readLoginStatus()) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onDestroy: ")
            }
            try {
                killSocket()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isServiceRunning = false
            val broadcastIntent = Intent(this, RestartBroadcastReceiver::class.java)
            sendBroadcast(broadcastIntent)
            stopTimer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (sessionManager.readLoginStatus()) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onDestroy: ")
            }
            try {
                killSocket()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isServiceRunning = false
            val broadcastIntent = Intent(this, RestartBroadcastReceiver::class.java)
            sendBroadcast(broadcastIntent)
            stopTimer()
        } else {
            try {
                killSocket()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            stopTimer()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun startTimer() {
        if (sessionManager.readLoginStatus()) {
            if (timer != null) {
                timer!!.cancel()
                timer = null
            }
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "startTimer: ")
            }
            if (isServiceRunning) return
            isServiceRunning = true
            timer = Timer()
            initializeTimerTask()
            timer!!.schedule(timerTask, 1000, 1000)
        }
    }

    private fun stopTimer() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "stopTimer: ")
        }
        isServiceRunning = false
        stopForeground(true)
        stopSelf()
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

    private fun initializeTimerTask() {
        if (sessionManager.readLoginStatus()) {
            timerTask = object : TimerTask() {
                override fun run() {
                    if (!isConnected && checkInternetConnection()) {
                        isTimeout = false
                    }
                    if (isConnected != checkInternetConnection()) {
                        isConnected = checkInternetConnection()
                        if (BuildConfig.DEBUG) {
                            Log.d(
                                TAG,
                                "run: initializeTimerTask checkInternetConnection $isConnected"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun checkInternetConnection(): Boolean {
        var result = false

        if (sessionManager.readLoginStatus()) {
            val connectivityManager =
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
                connectivityManager.allNetworks
            } else {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }

                    }
                }
            }
        }

        return result
    }
}
package com.adts.app.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.adts.app.MainActivity
import com.adts.app.R
import com.adts.app.activity.Login
import com.adts.app.network.MyApp
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseMessageReceiver : FirebaseMessagingService() {
    private var notificationType: String? = ""
    var title: String? = ""
    var message: String? = ""
    var intent: Intent? = null
    private val myApp = MyApp(this)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        notificationType = remoteMessage.data["NotificationType"]
        title = remoteMessage.data["title"]
        message = remoteMessage.data["message"]
        val isLogin = myApp.getSharedPrefBoolean(MyApp.isLogin)
        if (isLogin) {
            intent = Intent(this, MainActivity::class.java)

        } else {
            intent = Intent(this, Login::class.java)

        }



        showNotification(title, message)
    }


/*
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        myApp.setSharedPrefString(MyApp.DEVICE_ID, token)
    }
*/


    fun showNotification(title: String?, message: String?) {
        val channelId = "eduapp"
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.app_icon)
                .setAutoCancel(true)
                .setVibrate(
                    longArrayOf(
                        1000, 1000, 1000,
                        1000, 1000
                    )
                )
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(
            NOTIFICATION_SERVICE
        ) as NotificationManager
        if (Build.VERSION.SDK_INT
            >= Build.VERSION_CODES.O
        ) {
            val notificationChannel = NotificationChannel(
                channelId, "eduapp",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(
                notificationChannel
            )
        }
        notificationManager.cancelAll()
        notificationManager.notify(0, builder.build())
    }


}
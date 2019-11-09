package com.classroomjoin.app.helper_utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.content.LocalBroadcastManager
import com.classroomjoin.app.R
import com.classroomjoin.app.studentEventDetailPage.EventDetailActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error


class MyFirebaseMessagingService : FirebaseMessagingService(), AnkoLogger {

    val TAG = "message"

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        error { "message received--->"+remoteMessage!!.data.toString() }
        if (remoteMessage!!.data.isNotEmpty()) {
            sendNotification(remoteMessage.data["title"]!!,
                    remoteMessage.data["body"]!!,
                    remoteMessage.data["id"],
                    remoteMessage.data["eventTypeId"],
                    remoteMessage.data["studentId"])
        }

        if (remoteMessage!!.notification != null) {
            error { remoteMessage.notification!!.body.toString() }
            error { remoteMessage.notification!!.title.toString() }
        }
    }

    private fun sendNotification(title: String, message: String, id: String?,
                                 event_type: String?,
                                 student_id: String?) {
        val intent = Intent(this, EventDetailActivity::class.java).putExtra(EventDetailActivity.EVENT_ID_KEY, id?.toInt()).putExtra(EventDetailActivity.EVENT_TYPE_ID_KEY, event_type)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val intentqq = Intent("fcm_refresh")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentqq)

        val pendingIntent = PendingIntent.getActivity(this, id!!.toInt(), intent,
                PendingIntent.FLAG_ONE_SHOT)

        val notifyID = 1
        val CHANNEL_ID = "my_channel_01"// The id of the channel.
        val name = getString(R.string.app_name)// The user-visible name of the channel.
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder: Notification?

        @RequiresApi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)

            notificationBuilder = Notification.Builder(this.applicationContext, "my_channel_id")
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI, AudioAttributes.USAGE_NOTIFICATION)
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .build()

            notificationManager.createNotificationChannel(mChannel);

        } else {
            notificationBuilder = Notification.Builder(this.applicationContext)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build()
        }


        var notification_id = 0
        notification_id = id!!.toInt()
        notificationManager.notify(notification_id, notificationBuilder)
    }

}

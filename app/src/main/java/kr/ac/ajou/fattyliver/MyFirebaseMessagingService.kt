package kr.ac.ajou.fattyliver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "MessagingService"


    override fun onMessageReceived(p0: RemoteMessage?) {
        // 앱이 foreground일 때 푸시알람을 받으면 실행
        if (p0!!.notification != null) {
            Log.d(TAG, "From: " + p0.from)
            Log.d(TAG, "${p0.notification?.body}")

            // 안드로이드 켜질 때 화면 켜짐
            val pm: PowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            val wakeLock: PowerManager.WakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG)
            wakeLock.acquire(3000)

            sendNotification(p0.notification?.body.toString(), p0.notification?.title.toString())

        }

        if (p0.data.isNotEmpty()) {
            Log.d(TAG, "${p0.data} : 이것은 data")
        }
    }

    private fun sendNotification(messageBody: String, messageTitle: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val channelId: String = getString(R.string.default_notification_channel_name)
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.alcohol)
                .setColor(resources.getColor(R.color.colorPrimary))
                .setContentTitle("집 갈 시간: $messageTitle")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 안드로이드 8.0이상 알림채널 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName: String = getString(R.string.default_notification_channel_name)
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}
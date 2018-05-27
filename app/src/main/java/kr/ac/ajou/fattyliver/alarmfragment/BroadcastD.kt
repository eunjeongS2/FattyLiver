package kr.ac.ajou.fattyliver.alarmFragment

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.PowerManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import kr.ac.ajou.fattyliver.MainActivity
import kr.ac.ajou.fattyliver.R

class BroadcastD : BroadcastReceiver() {

    //val INTENT_ACTION: String = Intent.ACTION_BOOT_COMPLETED

    var password: String? = null
    var phone: String? = null

    override fun onReceive(p0: Context?, p1: Intent?) {
        password = p1?.getStringExtra("password")!!
        phone = p1.getStringExtra("phone")
        sendNotification(p0)

    }


    private fun sendNotification(context: Context?) {
        val notificationManager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notiIconClickIntent = Intent(context, MainActivity::class.java)
        notiIconClickIntent.putExtra("ReleaseAlarmFragment", "notiIntent")
        notiIconClickIntent.putExtra("password", "" + password)
        notiIconClickIntent.putExtra("phone", "" + phone)
        //val pendingIntent = PendingIntent.getActivity(context, 0, notiIconClickIntent, PendingIntent.FLAG_ONE_SHOT)
        val releasePendingIntent = PendingIntent.getActivity(context, 0, notiIconClickIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context)
        builder.setSmallIcon(R.drawable.gohome)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.gohome))
                .addAction(R.drawable.navigation_empty_icon, "해제", releasePendingIntent)
                .setTicker("알림!!")
                .setWhen(System.currentTimeMillis())
                .setNumber(1)
                .setContentTitle("집 가기 30분 전!!!")
                .setContentText("일어나자 ~")
                .setVibrate(longArrayOf(0, 3000))
                .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
                //.setContentIntent(pendingIntent)
                .setAutoCancel(false)

        Log.d("Eeee", "notification!!!!")

        notificationManager.notify(1, builder.build())
    }

}
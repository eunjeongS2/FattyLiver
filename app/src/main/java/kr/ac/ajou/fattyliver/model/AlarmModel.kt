package kr.ac.ajou.fattyliver.model

import android.util.Log
import com.google.firebase.database.*
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


class AlarmModel {
    companion object {
        const val TAG = "AlarmModel"
        const val FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send"
        const val SERVER_KEY = "AAAAJK0wJs4:APA91bFgNobtyghiC9XqXpvWBQBDWRA8RxAPymqZ9JVGQQQU0I0CI50U_3AgQOVsDLj-ql2e1aA2Mlx1b0ZVBT1hKYa7JSL_IgCFQu7zzXXBIw6mZKTrVKG4EHRMxaBgtWdTqNnTXgsd"
    }

    private var alarmRef: DatabaseReference? = null

    private var alarms: MutableList<Alarm>? = null
    private var onAlarmLoadListener: OnAlarmLoadListener? = null

    interface OnAlarmLoadListener {
        fun onFetchAlarm(alarmList: MutableList<Alarm>)
    }

    init {
        alarms = mutableListOf()

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        alarmRef = UserModel.instance.user?.uid?.let { database.getReference("user").child(it).child("alarms")}
        alarmRef?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val newAlarms: MutableList<Alarm> = mutableListOf()
                val children: MutableIterable<DataSnapshot>? = p0.children

                for (e in children!!) {
                    val alarm: Alarm? = e.getValue(Alarm::class.java)
                    alarm?.let { newAlarms.add(it) }
                }

                alarms = newAlarms
                if (onAlarmLoadListener != null) {
                    onAlarmLoadListener?.onFetchAlarm(alarms!!)
                }            }


        })

    }

    fun setOnAlarmLoadListener(onAlarmLoadListener: OnAlarmLoadListener) {
        this.onAlarmLoadListener = onAlarmLoadListener
    }


    fun fetchAlarm() {
        if (onAlarmLoadListener != null)
            alarms?.let { onAlarmLoadListener?.onFetchAlarm(it) }
    }

    fun saveAlarm(meridiem: String, time: String, phoneNum: String, password: String) {
        val childRef: DatabaseReference = alarmRef?.push()!!
        childRef.setValue(childRef.key?.let { Alarm(it, meridiem, time, phoneNum, password) })
    }

    fun deleteAlarm(alarmId: String) {
        alarmRef?.child(alarmId)?.removeValue()
    }

    fun changeAlarmActivate(alarmId: String, activate: Boolean) {
        alarmRef?.child(alarmId)?.child("activate")?.setValue(activate)
    }

    // push notification 전송
    fun sendPostToFCM(token: String?, time: String) {
        Thread(Runnable {
            val root = JSONObject()
            val notification = JSONObject()
            notification.put("body", "집 가자 ~")
            notification.put("title1", time)
            root.put("notification", notification)
            root.put("to", token)

            val url = URL(FCM_MESSAGE_URL)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.doOutput = true
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Authorization", "key=$SERVER_KEY")

            val os: OutputStream = conn.outputStream
            os.write(root.toString().toByteArray())
            os.flush()
            os.close()

            Log.d(TAG, root.toString())
            Log.d(TAG, "responseCode: " + conn.responseCode)
        }).start()
    }

}
package kr.ac.ajou.fattyliver.model

import android.util.Log
import com.google.firebase.database.*
import kr.ac.ajou.fattyliver.Tab2Fragment
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


class AlarmModel {
    private val TAG = "AlarmModel"
    private val FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send"
    private val SERVER_KEY = "AAAAJK0wJs4:APA91bFgNobtyghiC9XqXpvWBQBDWRA8RxAPymqZ9JVGQQQU0I0CI50U_3AgQOVsDLj-ql2e1aA2Mlx1b0ZVBT1hKYa7JSL_IgCFQu7zzXXBIw6mZKTrVKG4EHRMxaBgtWdTqNnTXgsd"


    private var alarmRef: DatabaseReference

    private var alarms: MutableList<Alarm>? = null
    private var onAlarmLoadListener: OnAlarmLoadListener? = null

    interface OnAlarmLoadListener {
        fun onFetchAlarm(alarmList: MutableList<Alarm>)
    }

    init {
        alarms = mutableListOf()
//        alarmList?.add(Alarm("오전", "6:55", "01012345678"))
//        alarmList?.add(Alarm("오후", "7:05", "01012345678"))

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        alarmRef = database.getReference("alarms")
        alarmRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                println(p0?.message)
            }

            override fun onDataChange(p0: DataSnapshot?) {
                val newAlarms: MutableList<Alarm> = mutableListOf()
                val children: MutableIterable<DataSnapshot>? = p0?.children

                for (e in children!!) {
                    val alarm: Alarm? = e.getValue(Alarm::class.java)
                    alarm?.let { newAlarms.add(it) }
                }

                alarms = newAlarms
                if (onAlarmLoadListener != null) {
                    onAlarmLoadListener?.onFetchAlarm(alarms!!)
                }
            }

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
        val childRef: DatabaseReference = alarmRef.push()
        childRef.setValue(Alarm(childRef.key, meridiem, time, phoneNum, password))
    }

    fun deleteAlarm(alarmId: String) {
        alarmRef.child(alarmId).removeValue()
    }

    // push notification 전송
    fun sendPostToFCM(token: String?, time: String) {
        Thread(Runnable {
            val root = JSONObject()
            val notification = JSONObject()
            notification.put("body", "집 가자 ~")
            notification.put("title", time)
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
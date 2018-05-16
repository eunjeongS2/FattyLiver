package kr.ac.ajou.fattyliver.model

import com.google.firebase.database.*



class AlarmModel {
    private var alarmRef:DatabaseReference

    private var alarms : MutableList<Alarm>? = null
    private var onAlarmLoadListener : OnAlarmLoadListener? = null

    interface OnAlarmLoadListener {
        fun onFetchAlarm(alarmList: MutableList<Alarm>)
    }

    init {
        alarms = mutableListOf()
//        alarmList?.add(Alarm("오전", "6:55", "01012345678"))
//        alarmList?.add(Alarm("오후", "7:05", "01012345678"))

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        alarmRef = database.getReference("alarms")
        alarmRef.addValueEventListener(object : ValueEventListener{
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
                if(onAlarmLoadListener != null) {
                    onAlarmLoadListener?.onFetchAlarm(alarms!!)
                }
            }

        })

    }



    fun setOnAlarmLoadListener(onAlarmLoadListener: OnAlarmLoadListener){
        this.onAlarmLoadListener = onAlarmLoadListener
    }

    fun fetchAlarm(){
        if(onAlarmLoadListener!=null)
            alarms?.let { onAlarmLoadListener?.onFetchAlarm(it) }
    }

    fun saveAlarm(meridiem: String, time: String, phoneNum: String) {
        val childRef: DatabaseReference = alarmRef.push()
        childRef.setValue(Alarm(meridiem, time, phoneNum))
    }




}
package kr.ac.ajou.fattyliver

import com.google.firebase.database.*

class AlarmModel {
    private var alarmRef:DatabaseReference

    private var alarmList : MutableList<Alarm>? = null
    private var onAlarmLoadListener : OnAlarmLoadListener? = null

    init {
        alarmList = mutableListOf()
//        alarmList?.add(Alarm("오전", "6:55", "01012345678"))
//        alarmList?.add(Alarm("오후", "7:05", "01012345678"))

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        alarmRef = database.getReference("alarms")
        alarmRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                System.out.println(p0?.message)
            }

            override fun onDataChange(p0: DataSnapshot?) {
                val newAlarmList: MutableList<Alarm> = mutableListOf()
                val children: MutableIterable<DataSnapshot>? = p0?.children

                for (e in children!!) {
                    val alarm: Alarm? = e.getValue(Alarm::class.java)
                    alarm?.let { newAlarmList.add(it) }
                }

                alarmList = newAlarmList
                if(onAlarmLoadListener != null) {
                    onAlarmLoadListener?.onFetchAlarm(alarmList!!)
                }
            }

        })

    }



    fun setOnAlarmLoadListener(onAlarmLoadListener: OnAlarmLoadListener){
        this.onAlarmLoadListener = onAlarmLoadListener
    }

    fun fetchAlarm(){
        if(onAlarmLoadListener!=null)
            alarmList?.let { onAlarmLoadListener?.onFetchAlarm(it) }
    }

    fun saveAlarm(meridiem: String, time: String, phoneNum: String) {
        val childRef: DatabaseReference = alarmRef.push()
        childRef.setValue(Alarm(meridiem, time, phoneNum))
    }




}
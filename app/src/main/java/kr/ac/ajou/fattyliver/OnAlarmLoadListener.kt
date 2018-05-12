package kr.ac.ajou.fattyliver

interface OnAlarmLoadListener {
    fun onFetchAlarm(alarmList: MutableList<Alarm>)
    fun onAddAlarm(alarm: Alarm)
//    fun onDataChanged()
}
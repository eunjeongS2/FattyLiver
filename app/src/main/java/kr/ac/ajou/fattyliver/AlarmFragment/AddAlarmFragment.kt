package kr.ac.ajou.fattyliver.alarmFragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.model.AlarmModel


class AddAlarmFragment : Fragment(), TimePicker.OnTimeChangedListener {

    private lateinit var timePicker: TimePicker
    private var nHour: Int = 0
    private var nMinute: Int = 0
    private var nMeridiem: String = ""
    private lateinit var phone: EditText
    private lateinit var password: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_alarm, container, false)

        timePicker = view.findViewById(R.id.timePicker)
        timePicker.setOnTimeChangedListener(this)

        phone = view.findViewById(R.id.alarm_phone_edit)
        password = view.findViewById(R.id.alarm_password_edit)

        initTime(timePicker.hour, timePicker.minute)

        val alarmModel = AlarmModel()
        saveButton = view.findViewById(R.id.ok_button)
        saveButton.setOnClickListener {
            Log.d("aaa", """${phone.text}/${password.text}/$nMeridiem $nHour:$nMinute""") // 저장
            alarmModel.saveAlarm(nMeridiem, "$nHour:$nMinute", phone.text.toString())
            fragmentManager?.popBackStackImmediate()
        }

        cancelButton = view.findViewById(R.id.cancel_button)
        cancelButton.setOnClickListener { fragmentManager?.popBackStackImmediate() }

        return view
    }

    fun initTime(hour: Int, minute: Int) {
        nMinute = minute
        nHour = if (hour > 12) hour - 12 else hour
        nMeridiem = if (hour >= 12 && hour != 0) "오후" else "오전"
    }

    override fun onTimeChanged(timePicker: TimePicker?, hourOfDay: Int, minute: Int) {
        initTime(hourOfDay, minute)
    }


}

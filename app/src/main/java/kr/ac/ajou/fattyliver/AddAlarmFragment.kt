package kr.ac.ajou.fattyliver


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker


class AddAlarmFragment : RootFragment(), TimePicker.OnTimeChangedListener {

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

        val alarmModel = AlarmModel()
        saveButton = view.findViewById(R.id.ok_button)
        saveButton.setOnClickListener{
            Log.d("aaa", """${phone.text}/${password.text}/$nMeridiem $nHour:$nMinute""") // 저장
            alarmModel.saveAlarm(nMeridiem, "$nHour:$nMinute", phone.text.toString())
            fragmentManager?.popBackStackImmediate()
            
        }

        cancelButton = view.findViewById(R.id.cancel_button)
        cancelButton.setOnClickListener { fragmentManager?.popBackStackImmediate() }

        return view
    }

    override fun onTimeChanged(timePicker: TimePicker?, hourOfDay: Int, minute: Int) {
        nHour = hourOfDay
        nMinute = minute
        nMeridiem = if(nHour >= 12) "오후" else "오전"
    }

}

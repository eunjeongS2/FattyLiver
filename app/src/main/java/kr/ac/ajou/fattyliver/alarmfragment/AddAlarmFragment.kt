package kr.ac.ajou.fattyliver.alarmFragment


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.model.AlarmModel
import java.util.*


class AddAlarmFragment : Fragment(), TimePicker.OnTimeChangedListener {
    companion object {
        private val TAG = AddAlarmFragment::class.java.simpleName
    }

    private lateinit var timePicker: TimePicker
    private var nHour: String = ""
    private var nMinute: String = ""
    private var oHour: Int = 0
    private var oMinute: Int = 0
    private var nMeridiem: String = ""
    private lateinit var phone: EditText
    private lateinit var password: EditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    //private var fcm: MyFirebaseMessagingService? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_alarm, container, false)

        timePicker = view.findViewById(R.id.timePicker)
        timePicker.setOnTimeChangedListener(this)

        phone = view.findViewById(R.id.alarm_phone_edit)
        password = view.findViewById(R.id.alarm_password_edit)
        passwordLayout = view.findViewById(R.id.alarm_password_layout)

        //비밀번호 4자리 입력 확인
        password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (password.length() != 4) {
                    passwordLayout.error = "비밀번호는 4자리로 입력해주세요!"
                } else {
                    passwordLayout.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        //fcm = MyFirebaseMessagingService()

        initTime(timePicker.hour, timePicker.minute)

        val alarmModel = AlarmModel()
        saveButton = view.findViewById(R.id.ok_button)
        saveButton.setOnClickListener {
            if (phone.text.toString().isEmpty()) {
                Toast.makeText(context, "문자를 전송할 번호를 입력하세요!", Toast.LENGTH_SHORT).show()
                phone.requestFocus()
                return@setOnClickListener
            }
            if (password.text.toString().isEmpty()) {
                Toast.makeText(context, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show()
                password.requestFocus()
                return@setOnClickListener
            }
            if(password.text.toString().length != 4) {
                Toast.makeText(context, "비밀번호를 4자리로 입력하세요!", Toast.LENGTH_SHORT).show()
                password.setText("")
                password.requestFocus()
                return@setOnClickListener
            }

            Log.d(TAG, """${phone.text}/${password.text}/$nMeridiem $nHour:$nMinute""") // 저장
            alarmModel.saveAlarm(nMeridiem, "$nHour:$nMinute", phone.text.toString(), password.text.toString())

//            val token = FirebaseInstanceId.getInstance().token
//            Log.d(TAG, token)
//            alarmModel.sendPostToFCM(token, "$nHour:$nMinute")
            val diff = diffMillis(oHour, oMinute)
            setAlarm(diff)

            fragmentManager?.popBackStackImmediate()

        }

        cancelButton = view.findViewById(R.id.cancel_button)
        cancelButton.setOnClickListener { fragmentManager?.popBackStackImmediate() }

        return view
    }

    private fun initTime(hour: Int, minute: Int) {
        nMinute = set2CharNum(minute)
        nHour = if (hour > 12) set2CharNum(hour - 12) else set2CharNum(hour)
        nMeridiem = if (hour >= 12 && hour != 0) "오후" else "오전"
        oMinute = minute
        oHour = hour
    }

    private fun set2CharNum(num: Int): String {
        return if (num >= 10)
            num.toString() + ""
        else
            "0$num"
    }


    override fun onTimeChanged(timePicker: TimePicker?, hourOfDay: Int, minute: Int) {
        initTime(hourOfDay, minute)
    }

    private fun diffMillis(hour: Int, minute: Int): Long {
        val currentTime = Calendar.getInstance()
        println("현재날짜: "+currentTime.time)

        val pickerTime = Calendar.getInstance()

        //오전일때 오전도 해결해야해!
        if(nMeridiem == "오전") {
            pickerTime.add(Calendar.DATE, 1)
        }

        pickerTime.set(Calendar.HOUR_OF_DAY, hour)
        pickerTime.set(Calendar.MINUTE, minute)
        pickerTime.set(Calendar.SECOND, 0)
        println("알람날짜: "+pickerTime.time)

        val diffMillis: Long = pickerTime.timeInMillis - currentTime.timeInMillis
        println(diffMillis)
        return diffMillis
    }

    private fun setAlarm(second: Long) {
        val am: AlarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, BroadcastD::class.java)
        intent.putExtra("password", password.text.toString())
        intent.putExtra("phone", phone.text.toString())

        val sender: PendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        am.set(AlarmManager.RTC, System.currentTimeMillis() + second, sender)
    }


}

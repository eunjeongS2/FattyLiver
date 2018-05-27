package kr.ac.ajou.fattyliver.alarmFragment


import android.Manifest
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*

import kr.ac.ajou.fattyliver.R
import android.content.Intent
import android.graphics.Color
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import android.app.Activity
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager
import android.widget.*
import kr.ac.ajou.fattyliver.model.UserModel


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class ReleaseAlarmFragment : Fragment() {

    companion object {
        const val PERMISSION_SEND_SMS = 1
        const val TAG = "ReleaseAlarmFragment"
    }

    private var userEntered: String? = null
    private var userPin = "0000"
    private var phoneNum = "01012345678"

    private val PIN_LENGTH = 4
    private var keyPadLockedFlag = false
    private var appContext: Context? = null

    private var titleView: TextView? = null

    private var statusView: TextView? = null
    private var statusCount: Int = 0

    private var button0: Button? = null
    private var button1: Button? = null
    private var button2: Button? = null
    private var button3: Button? = null
    private var button4: Button? = null
    private var button5: Button? = null
    private var button6: Button? = null
    private var button7: Button? = null
    private var button8: Button? = null
    private var button9: Button? = null
    private var buttonExit: Button? = null
    private var buttonDelete: Button? = null
    private var passwordInput: EditText? = null
    private var backSpace: ImageView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_release_alarm, container, false)

        appContext = context
        userEntered = ""

        val bundle = this.arguments
        if (bundle != null) {
            userPin = bundle.getString("password", "0000")
            phoneNum = bundle.getString("phone", "01012345678")
        }
        println("비번: $userPin")
        println("번호: $phoneNum")

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        statusView = view.findViewById(R.id.statusview)
        passwordInput = view.findViewById(R.id.editText)
        backSpace = view.findViewById(R.id.imageView)
        backSpace?.bringToFront()
        buttonExit = view.findViewById(R.id.buttonExit)
        backSpace?.setOnClickListener {
            if (keyPadLockedFlag) {
                return@setOnClickListener
            }

            passwordInput?.setText(passwordInput?.text.toString().substring(0, passwordInput?.text.toString().length - 1))
            passwordInput?.setSelection(passwordInput?.text.toString().length)
            if (userEntered!!.isNotEmpty()) {
                userEntered = userEntered!!.substring(0, userEntered!!.length - 1)
                Log.v("PinView", "User entered=$userEntered")
            }
        }
        buttonExit?.setOnClickListener {
            //Exit app
            val i = Intent()
            i.action = Intent.ACTION_MAIN
            i.addCategory(Intent.CATEGORY_HOME)
            appContext?.startActivity(i)
            fragmentManager?.popBackStackImmediate()
        }

        buttonDelete = view.findViewById(R.id.buttonDeleteBack) as Button
        buttonDelete?.setOnClickListener(View.OnClickListener {
            if (keyPadLockedFlag === true) {
                return@OnClickListener
            }

            if (userEntered!!.isNotEmpty()) {
                userEntered = ""
                passwordInput?.setText("")
            }
        })

        titleView = view.findViewById(R.id.time)
        val date = Date()
        val sdf = SimpleDateFormat("hh:mm")
        titleView?.text = sdf.format(date)

        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val pinButtonHandler = View.OnClickListener { v ->
            if (keyPadLockedFlag === true) {
                return@OnClickListener
            }

            val pressedButton = v as Button


            if (userEntered!!.length < PIN_LENGTH) {
                userEntered += pressedButton.text
                Log.v("PinView", "User entered=$userEntered")

                //Update pin boxes
                passwordInput?.setText(passwordInput?.text.toString() + "*")
                passwordInput?.setSelection(passwordInput?.text.toString().length)

                if (userEntered!!.length === PIN_LENGTH) {
                    //Check if entered PIN is correct
                    if (userEntered.equals(userPin)) {
                        statusView?.setTextColor(Color.GREEN)
                        statusView?.text = "비밀번호 일치"
                        Log.v("PinView", "Correct PIN")
                        //notificationManager.cancel(NOTIFICATION_ID)
                        notificationManager.cancelAll()
//                            finish()
                        fragmentManager?.beginTransaction()?.addToBackStack(null)?.replace(R.id.main_container, Tab2Fragment())?.commit()

                    } else {
                        statusView?.setTextColor(Color.RED)
                        statusView?.text = "틀렸습니다"
                        userEntered = ""
                        passwordInput?.setText("")
                        statusCount += 1
                        Log.v("PinView", "Wrong PIN")

                        //LockKeyPadOperation().execute("")
                    }

                    if (statusCount >= 3) {
                        statusView?.text = "입력 3번 초과. 문자 전송~"
                        keyPadLockedFlag = true
                        //notificationManager.cancel(NOTIFICATION_ID)

                        //문자보내기
                        //sendSMS(phoneNum, UserModel.instance.user?.name)
                        Log.d(TAG, "문자보낼타")
                        checkPermission()

                        notificationManager.cancelAll()
                    }
                }
            } else {
                //Roll over
                passwordInput?.setText("")
                userEntered = ""
                statusView?.setTextColor(Color.GRAY)
                statusView?.text = "4자리만 입력 가능합니다"
                userEntered += pressedButton.text
                Log.v("PinView", "User entered=$userEntered")

                //Update pin boxes
                passwordInput?.setText("8")

            }
        }

//            inner class LockKeyPadOperation : AsyncTask<String, Unit, String>() {
//                override fun doInBackground(vararg p0: String?): String {
//                    for (i in 0..1) {
//                        try {
//                            Thread.sleep(1000)
//                        } catch (e: InterruptedException) {
//                            e.printStackTrace()
//                        }
//
//                    }
//
//                    return "Executed"
//                }
//
//                override fun onPostExecute(result: String?) {
//                    statusView?.text = ""
//                    passwordInput?.setText("")
//                    userEntered = "";
//                    keyPadLockedFlag = false;
//                }
//            }

        button0 = view.findViewById(R.id.button0)
        button0?.setOnClickListener(pinButtonHandler)

        button1 = view.findViewById(R.id.button1)
        button1?.setOnClickListener(pinButtonHandler)

        button2 = view.findViewById(R.id.button2)
        button2?.setOnClickListener(pinButtonHandler)

        button3 = view.findViewById(R.id.button3)
        button3?.setOnClickListener(pinButtonHandler)

        button4 = view.findViewById(R.id.button4)
        button4?.setOnClickListener(pinButtonHandler)

        button5 = view.findViewById(R.id.button5)
        button5?.setOnClickListener(pinButtonHandler)

        button6 = view.findViewById(R.id.button6)
        button6?.setOnClickListener(pinButtonHandler)

        button7 = view.findViewById(R.id.button7)
        button7?.setOnClickListener(pinButtonHandler)

        button8 = view.findViewById(R.id.button8)
        button8?.setOnClickListener(pinButtonHandler)

        button9 = view.findViewById(R.id.button9)
        button9?.setOnClickListener(pinButtonHandler)

        buttonDelete = view.findViewById(R.id.buttonDeleteBack)


        return view
    }

    private fun checkPermission() {
        if(context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.SEND_SMS) } != PackageManager.PERMISSION_GRANTED) {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.SEND_SMS), PERMISSION_SEND_SMS) }

        } else {
            //send message
            sendSMS(phoneNum, UserModel.instance.user?.name)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            PERMISSION_SEND_SMS -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //send message
                    Log.d(TAG, "Permission granted")
                    sendSMS(phoneNum, UserModel.instance.user?.name)
                } else {
                    Log.d(TAG, "Permission always deny")
                }
            }
        }
    }

    private fun sendSMS(smsNumber: String, smsText: String?) {
        Log.d(TAG, "sendSMS 함수")

        activity?.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                when(resultCode) {
                    Activity.RESULT_OK -> Toast.makeText(context, "전송 완료", Toast.LENGTH_SHORT).show()
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> Toast.makeText(context, "전송 실패", Toast.LENGTH_SHORT).show()
                    SmsManager.RESULT_ERROR_NO_SERVICE -> Toast.makeText(context, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show()
                    SmsManager.RESULT_ERROR_RADIO_OFF -> Toast.makeText(context, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show()
                    SmsManager.RESULT_ERROR_NULL_PDU -> Toast.makeText(context, "PDU NULL", Toast.LENGTH_SHORT).show()
                }
            }
        }, IntentFilter("SMS_SENT_ACTION"))

        activity?.registerReceiver(object: BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                when(resultCode) {
                    Activity.RESULT_OK -> {
                        Log.d(TAG, "sms 도착")
                        Toast.makeText(context, "SMS 도착 완료", Toast.LENGTH_SHORT).show()
                    }
                    Activity.RESULT_CANCELED -> Toast.makeText(context, "SMS 전송 실패", Toast.LENGTH_SHORT).show()
                }
            }

        }, IntentFilter("SMS_DELIVERED_ACTION"))

        val mSmsManager: SmsManager = SmsManager.getDefault()
        mSmsManager.sendTextMessage(smsNumber, null, smsText+"이 집에 못가는 상황입니다", null, null)

    }

}



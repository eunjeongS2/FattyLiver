package kr.ac.ajou.fattyliver.chatFragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.AppCompatButton
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TimePicker
import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.model.ChatModel


class AddNoticeDialogFragment : DialogFragment(), TimePicker.OnTimeChangedListener{

    companion object {
        const val TAG = "AddNoticeDialogFragment"
    }

    private var hour: Int = 0
    private var minute: Int = 0
    private var meridiem: String = ""

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val chatId: String? = arguments?.getString("chatId")

        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)

        val inflater : LayoutInflater? = activity?.layoutInflater
        val view : View? = inflater?.inflate(R.layout.fragment_chat_notice_add, null)

        val noticeEditText: EditText? = view?.findViewById(R.id.editText_chat_notice)
        val timePicker: TimePicker? = view?.findViewById(R.id.timePicker_chat_notice)
        val saveButton: AppCompatButton? = view?.findViewById(R.id.button_chat_notice_save)
        val cancelButton: AppCompatButton? = view?.findViewById(R.id.button_chat_notice_cancel)

        timePicker?.setOnTimeChangedListener(this)


        saveButton?.setOnClickListener{
            chatId?.let { it1 -> ChatModel(it1).addNotice(noticeEditText?.text.toString(), "$meridiem $hour:$minute") }
            dismiss()
        }

        cancelButton?.setOnClickListener{ dismiss() }

        builder.setView(view)

        val alertDialog : AlertDialog = builder.create()
        alertDialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                fragmentManager?.popBackStack()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        alertDialog.setCanceledOnTouchOutside(false)
        return alertDialog
    }

    override fun onTimeChanged(p0: TimePicker?, p1: Int, p2: Int) {
        meridiem = if(p1 in 0..11) "오전" else "오후"
        hour = p1
        minute = p2
    }

}


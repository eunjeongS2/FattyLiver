package kr.ac.ajou.fattyliver.mainTabFragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.timessquare.CalendarPickerView
import kr.ac.ajou.fattyliver.R
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : DialogFragment(){
    companion object {
        const val TAG = "CalendarFragment"
    }

    interface OnDateChangedListener {
        fun onDateChanged(dates : MutableList<String>)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val format = SimpleDateFormat("yyyy/M.d/a h:mm", Locale.KOREA)
        val startDate = format.parse(arguments?.getString("startDate"))

        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater : LayoutInflater? = activity?.layoutInflater
        val view : View? = inflater?.inflate(R.layout.fragment_calendar, null)

        val calendarView = view?.findViewById<CalendarPickerView>(R.id.calendar_view)
        val selectText = view?.findViewById<TextView>(R.id.textView_select)
        val closeButton = view?.findViewById<ImageView>(R.id.button_calendar_close)

        val nextDate = Calendar.getInstance()
        nextDate.time = Date()
        nextDate.add(Calendar.DATE, 1)

        calendarView?.init(startDate, Date(nextDate.timeInMillis))
                    ?.withSelectedDate(Date())
                    ?.inMode(CalendarPickerView.SelectionMode.RANGE)

        selectText?.setOnClickListener {
            val selectedDates = mutableListOf<String>()
            calendarView?.selectedDates?.forEach { selectedDates.add(format.format(it)) }

            val listener = targetFragment as OnDateChangedListener
            listener.onDateChanged(selectedDates)

            dismiss()
        }

        closeButton?.setOnClickListener {
            dismiss()
        }

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
}


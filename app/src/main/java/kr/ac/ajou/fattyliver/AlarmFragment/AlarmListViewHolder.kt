package kr.ac.ajou.fattyliver.AlarmFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import kr.ac.ajou.fattyliver.AbstractViewHolder
import kr.ac.ajou.fattyliver.Model.Alarm
import kr.ac.ajou.fattyliver.R

class AlarmListViewHolder(parent: ViewGroup) : AbstractViewHolder<Alarm>(
        LayoutInflater.from(parent.context)
                .inflate(R.layout.item_alarm, parent, false)) {

    private val meridiem: TextView = itemView.findViewById(R.id.alarm_time_meridiem_text)
    private val time: TextView = itemView.findViewById(R.id.alarm_time_text)
    private val phoneNum: TextView = itemView.findViewById(R.id.alarm_phone_text)

    override fun onBindView(item: Alarm, position: Int) {
        meridiem.text = item.meridiem
        time.text = item.time
        phoneNum.text = item.phoneNum

    }

}

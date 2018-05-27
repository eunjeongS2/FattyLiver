package kr.ac.ajou.fattyliver.alarmFragment

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.Switch
import kr.ac.ajou.fattyliver.AbstractRecyclerAdapter
import kr.ac.ajou.fattyliver.AbstractViewHolder
import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.model.Alarm
import kr.ac.ajou.fattyliver.model.AlarmModel

class AlarmListRecyclerAdapter : AbstractRecyclerAdapter<Alarm>() {
    private val alarmModel: AlarmModel = AlarmModel()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Alarm> {
        return AlarmListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Alarm>, @SuppressLint("RecyclerView") position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnLongClickListener {
            alarmModel.deleteAlarm(getItem(position)?.alarmId!!)
            true
        }

        val switch: Switch = holder.itemView.findViewById(R.id.alarm_switch)
        switch.isChecked = getItem(position)?.activate!!
        switch.setOnClickListener { getItem(position)?.alarmId?.let { it1 -> alarmModel.changeAlarmActivate(it1, switch.isChecked) } }
//        switch.setOnClickListener { onAlarmChangedListener?.onAlarmChange(getItem(position)?.alarmId, switch.isChecked) }

    }
}
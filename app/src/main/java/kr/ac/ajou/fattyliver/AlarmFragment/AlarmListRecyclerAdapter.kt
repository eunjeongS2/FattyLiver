package kr.ac.ajou.fattyliver.AlarmFragment

import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import kr.ac.ajou.fattyliver.AbstractRecyclerAdapter
import kr.ac.ajou.fattyliver.AbstractViewHolder
import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.model.Alarm
import kr.ac.ajou.fattyliver.model.AlarmModel

class AlarmListRecyclerAdapter : AbstractRecyclerAdapter<Alarm>() {
    val alarmModel: AlarmModel = AlarmModel()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Alarm> {
        return AlarmListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Alarm>, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                alarmModel.deleteAlarm(getItem(position)?.alarmId!!)
                return true
            }

        })

        val switch: Switch = holder.itemView.findViewById(R.id.alarm_switch)
        switch.isChecked = true
        switch.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if(p1) {
                    // checked
                    println("checked")
                } else {

                }
            }
        })


    }
}
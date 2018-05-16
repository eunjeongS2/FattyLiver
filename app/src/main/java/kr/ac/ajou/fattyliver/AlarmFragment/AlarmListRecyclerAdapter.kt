package kr.ac.ajou.fattyliver

import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import kr.ac.ajou.fattyliver.alarmFragment.AlarmListViewHolder
import kr.ac.ajou.fattyliver.model.Alarm

class AlarmListRecyclerAdapter : AbstractRecyclerAdapter<Alarm>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Alarm> {
        return AlarmListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Alarm>, position: Int) {
        super.onBindViewHolder(holder, position)

        val switch: Switch = holder.itemView.findViewById(R.id.alarm_switch)
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
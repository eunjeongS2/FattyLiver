package com.example.janghanna.fattyliver

import android.view.ViewGroup

class AlarmListRecyclerAdapter : AbstractRecyclerAdapter<Alarm>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Alarm> {
        return AlarmListViewHolder(parent)
    }

}
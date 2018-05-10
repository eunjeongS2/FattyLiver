package com.example.janghanna.fattyliver

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class AbstractViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBindView(item: T, position: Int)

}
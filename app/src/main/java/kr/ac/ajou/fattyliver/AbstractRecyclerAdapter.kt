package com.example.janghanna.fattyliver

import android.support.v7.widget.RecyclerView
import java.util.ArrayList

abstract class AbstractRecyclerAdapter<T> : RecyclerView.Adapter<AbstractViewHolder<T>>() {

    private var items: MutableList<T> = emptyList<T>() as MutableList<T>

    private var onItemClickListener: OnItemClickListener<T>? = null

    interface OnItemClickListener<in T> {
        fun onItemClick(item: T, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>) {
        this.onItemClickListener = onItemClickListener
    }

    init {
        items = ArrayList()
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<T>, position: Int) {
        holder.onBindView(getItem(holder.adapterPosition), position)
        holder.itemView.setOnClickListener({ _ ->
            if (onItemClickListener != null) {
                onItemClickListener!!.onItemClick(getItem(position), position)
            }
        })
    }

    fun getItem(position: Int): T {
        return items[position]
    }

    operator fun get(item: T): Int {
        return items.indexOf(item)
    }

    fun setItems(items: MutableList<T>) {
        this.items.clear()
        this.items = items
    }

    fun clear() {
        this.items.clear()
    }

    fun addItemToIndex(index: Int, item: T) {
        items.add(index, item)
    }

    fun addItem(item: T) {
        items.add(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

}


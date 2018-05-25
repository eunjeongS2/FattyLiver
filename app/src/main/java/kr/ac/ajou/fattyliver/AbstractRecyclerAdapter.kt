package kr.ac.ajou.fattyliver

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import java.util.ArrayList

abstract class AbstractRecyclerAdapter<T> : RecyclerView.Adapter<AbstractViewHolder<T>>() {

    private var items: MutableList<T>? = null

    private var onItemClickListener: OnItemClickListener<T>? = null

    interface OnItemClickListener<in T> {
        fun onItemClick(item: T, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>) {
        this.onItemClickListener = onItemClickListener
    }

    init {
        items = mutableListOf()
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<T>, position: Int) {
        getItem(holder.adapterPosition)?.let { holder.onBindView(it, position) }
        holder.itemView.setOnClickListener({
            if (onItemClickListener != null) {
                getItem(position)?.let { onItemClickListener!!.onItemClick(it, position) }
            }
        })

    }

    fun getItem(position: Int): T? {
        return items?.get(position)
    }

    operator fun get(item: T): Int? {
        return items?.indexOf(item)
    }

    fun setItems(items: MutableList<T>) {
        this.items?.clear()
        this.items = items
    }

    fun clear() {
        this.items?.clear()
    }

    fun addItemToIndex(index: Int, item: T) {
        items?.add(index, item)
    }

    fun addItem(item: T) {
        items?.add(item)
    }

    override fun getItemCount(): Int {
        return items?.size!!
    }

}


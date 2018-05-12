package kr.ac.ajou.fattyliver

import android.view.ViewGroup

class AlcoholListRecyclerAdapter : AbstractRecyclerAdapter<Alcohol>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Alcohol> {
        return AlcoholListViewHolder(parent)
    }
}

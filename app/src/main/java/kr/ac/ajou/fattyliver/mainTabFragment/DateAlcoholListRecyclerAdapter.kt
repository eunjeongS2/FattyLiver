package kr.ac.ajou.fattyliver.mainTabFragment

import android.view.ViewGroup
import kr.ac.ajou.fattyliver.AbstractRecyclerAdapter
import kr.ac.ajou.fattyliver.AbstractViewHolder
import kr.ac.ajou.fattyliver.model.Alcohol

class DateAlcoholListRecyclerAdapter : AbstractRecyclerAdapter<Alcohol>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Alcohol> {
        return DateAlcoholListViewHolder(parent)
    }
}
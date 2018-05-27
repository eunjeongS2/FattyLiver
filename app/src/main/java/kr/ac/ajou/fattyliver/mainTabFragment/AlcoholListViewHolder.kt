package kr.ac.ajou.fattyliver.mainTabFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_alcohol.view.*
import kr.ac.ajou.fattyliver.AbstractViewHolder
import kr.ac.ajou.fattyliver.model.Alcohol
import kr.ac.ajou.fattyliver.R

class AlcoholListViewHolder(parent : ViewGroup) : AbstractViewHolder<Alcohol>(LayoutInflater.from(parent.context).inflate(R.layout.item_alcohol, parent, false)) {

    private val dateTextView : TextView = itemView.text_date
    private val firstValueTextView : TextView = itemView.textView_value

    override fun onBindView(item: Alcohol, position: Int) {
        dateTextView.text = item.timestamp.split("/")[1]
        firstValueTextView.text = item.value.toString()
    }


}

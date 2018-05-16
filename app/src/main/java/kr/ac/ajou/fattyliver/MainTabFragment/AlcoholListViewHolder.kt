package kr.ac.ajou.fattyliver.mainTabFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import kr.ac.ajou.fattyliver.AbstractViewHolder
import kr.ac.ajou.fattyliver.model.Alcohol
import kr.ac.ajou.fattyliver.R

class AlcoholListViewHolder(parent : ViewGroup) : AbstractViewHolder<Alcohol>(LayoutInflater.from(parent.context).inflate(R.layout.item_alcohol, parent, false)) {

    private var dateTextView : TextView? = null
    private var firstValueTextView : TextView? = null
    private var secondValueTextView : TextView? = null
    private var thirdValueTextView : TextView? = null

    init {
        dateTextView = itemView.findViewById(R.id.text_date)
        firstValueTextView = itemView.findViewById(R.id.text_firstValue)
        secondValueTextView = itemView.findViewById(R.id.text_secondValue)
        thirdValueTextView = itemView.findViewById(R.id.text_thirdValue)
    }

    override fun onBindView(item: Alcohol, position: Int) {
        dateTextView?.text = item.timestamp.split("/")[0]
        firstValueTextView?.text = item.value.toString()
    }


}
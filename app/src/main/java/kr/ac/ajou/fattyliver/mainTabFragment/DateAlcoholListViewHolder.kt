package kr.ac.ajou.fattyliver.mainTabFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import kr.ac.ajou.fattyliver.AbstractViewHolder
import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.model.Alcohol

class DateAlcoholListViewHolder(parent : ViewGroup) : AbstractViewHolder<Alcohol>(LayoutInflater.from(parent.context).inflate(R.layout.item_date_alcohol, parent, false)) {

    private var dateTimeTextView : TextView? = null
    private var dateValueTextView : TextView? = null

    init {
        dateTimeTextView = itemView.findViewById(R.id.textView_date_time)
        dateValueTextView = itemView.findViewById(R.id.textView_date_value)

    }

    override fun onBindView(item: Alcohol, position: Int) {
        dateTimeTextView?.text = item.timestamp.split('/')[1]
        dateValueTextView?.text = item.value.toString()
    }


}

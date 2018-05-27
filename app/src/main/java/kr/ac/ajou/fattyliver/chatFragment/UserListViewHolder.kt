package kr.ac.ajou.fattyliver.chatFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_user_list.view.*
import kr.ac.ajou.fattyliver.AbstractViewHolder
import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.model.UserSelect

class UserListViewHolder(parent : ViewGroup) : AbstractViewHolder<UserSelect>(LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent, false)) {

    private val userTextView : TextView = itemView.textView_user_list

    override fun onBindView(item: UserSelect, position: Int) {
        userTextView.text = item.name
    }


}
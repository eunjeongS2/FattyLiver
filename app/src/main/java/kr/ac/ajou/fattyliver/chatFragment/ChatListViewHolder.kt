package kr.ac.ajou.fattyliver.chatFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_chat_list.view.*
import kr.ac.ajou.fattyliver.AbstractViewHolder
import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.model.ChatList


class ChatListViewHolder(parent : ViewGroup) : AbstractViewHolder<ChatList>(LayoutInflater.from(parent.context).inflate(R.layout.item_chat_list, parent, false)) {

    private val usersTextView: TextView = itemView.textView_people
    private val usersNumberTextView: TextView = itemView.textView_people_number
    private val chatListTextView: TextView = itemView.textView_chatList_time

    @SuppressLint("SetTextI18n")
    override fun onBindView(item: ChatList, position: Int) {
        usersTextView.text = item.users.joinToString(", ")
        usersNumberTextView.text = "${item.users.size}명"
        chatListTextView.text = item.timestamp
    }


}

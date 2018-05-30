package kr.ac.ajou.fattyliver.chatFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_chat_list.view.*
import kr.ac.ajou.fattyliver.AbstractViewHolder
import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.model.ChatList
import kr.ac.ajou.fattyliver.model.UserModel


class ChatListViewHolder(parent : ViewGroup) : AbstractViewHolder<ChatList>(LayoutInflater.from(parent.context).inflate(R.layout.item_chat_list, parent, false)) {

    private val usersTextView: TextView = itemView.textView_people
    private val usersNumberTextView: TextView = itemView.textView_people_number
    private val timeTextView: TextView = itemView.textView_chatList_time
    private val lastMessageTextView: TextView = itemView.textView_chatList_lastMessage
    private val alcoholImageView: ImageView = itemView.imageView_chat_list_alcohol

    @SuppressLint("SetTextI18n")
    override fun onBindView(item: ChatList, position: Int) {
        usersTextView.text = item.users.filter { it != UserModel.instance.user?.name }.joinToString(", ")
        usersNumberTextView.text = "${item.users.size}명"
        timeTextView.text = item.timestamp
        lastMessageTextView.text = item.lastMessage?.message

        var alcohol = 0
        when(item.alcoholImage){
            "beer" -> alcohol = R.drawable.beer
            "soju" -> alcohol = R.drawable.soju
            "cocktail" -> alcohol = R.drawable.cocktail
            "wine" -> alcohol = R.drawable.wine
        }
        alcoholImageView.setImageResource(alcohol)
    }


}

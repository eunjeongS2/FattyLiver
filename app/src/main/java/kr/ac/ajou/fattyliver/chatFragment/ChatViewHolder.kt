package kr.ac.ajou.fattyliver.chatFragment

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import kr.ac.ajou.fattyliver.AbstractViewHolder
import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.model.Chat
import kr.ac.ajou.fattyliver.model.UserModel

class ChatViewHolder(parent: ViewGroup): AbstractViewHolder<Chat>(
        LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)) {

    private val layout: LinearLayout = itemView.findViewById(R.id.item_chat_layout)
    private val textHolder: LinearLayout = itemView.findViewById(R.id.text_holder)
    private val user: TextView = itemView.findViewById(R.id.user_textView)
    private val message: TextView = itemView.findViewById(R.id.message_textView)
    private val timestamp: TextView = itemView.findViewById(R.id.time_textView)

    override fun onBindView(item: Chat, position: Int) {
        if(UserModel.instance.user?.uid == item.uid) {
            setTextRight(item)
        } else {
            setTextLeft(item)
        }
    }

    private fun setText(item: Chat) {
        user.text = item.userId
        message.text = item.message
        timestamp.text = item.timestamp
    }

    private fun setTextRight(item: Chat) {
        layout.gravity = Gravity.END
        setText(item)
        message.setBackgroundResource(R.drawable.bg_msg_to)
        textHolder.getChildAt(0).bringToFront()
        timestamp.translationX = -15f
    }

    private fun setTextLeft(item: Chat) {
        layout.gravity = Gravity.START
        setText(item)
        message.setBackgroundResource(R.drawable.bg_msg_from)
    }

}

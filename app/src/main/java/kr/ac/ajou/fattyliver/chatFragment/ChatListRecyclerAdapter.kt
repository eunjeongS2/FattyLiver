package kr.ac.ajou.fattyliver.chatFragment

import android.view.ViewGroup
import kr.ac.ajou.fattyliver.AbstractRecyclerAdapter
import kr.ac.ajou.fattyliver.AbstractViewHolder
import kr.ac.ajou.fattyliver.model.Chat

class ChatListRecyclerAdapter : AbstractRecyclerAdapter<Chat>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Chat> {
        return ChatListViewHolder(parent)
    }
}

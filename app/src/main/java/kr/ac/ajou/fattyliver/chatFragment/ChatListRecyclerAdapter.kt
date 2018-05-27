package kr.ac.ajou.fattyliver.chatFragment

import android.view.ViewGroup
import kr.ac.ajou.fattyliver.AbstractRecyclerAdapter
import kr.ac.ajou.fattyliver.AbstractViewHolder
import kr.ac.ajou.fattyliver.model.ChatList

class ChatListRecyclerAdapter : AbstractRecyclerAdapter<ChatList>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<ChatList> {
        return ChatListViewHolder(parent)
    }
}
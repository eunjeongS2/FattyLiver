package kr.ac.ajou.fattyliver.model

import com.google.firebase.database.*

class ChatListModel{
    private var chatList: MutableList<ChatList>? = null
    private var chatListRef: DatabaseReference? = null

    var onChatListLoadListener: OnChatListLoadListener? = null

    interface OnChatListLoadListener {
        fun onFetchChatList(chatList: MutableList<ChatList>)
    }

    init {
        chatList = mutableListOf()
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        chatListRef = database.getReference("chats")

        this.chatListRef?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val newChatList: MutableList<ChatList> = mutableListOf()
                val children: MutableIterable<DataSnapshot>? = p0.children

                for (e in children!!) {
                    val chatId : String? = e.child("chatId").getValue(String::class.java)
                    val users: MutableList<String> = mutableListOf()
                    val timestamp : String? = e.child("timestamp").getValue(String::class.java)

                    val lastMessage: Chat? = if(e.child("message").hasChildren())
                        e.child("message").children.last().getValue(Chat::class.java)
                    else Chat()

                    e.child("users").children.forEach {
                        it.getValue(String::class.java)?.let { users.add(it) }
                    }

                    if(users.contains(UserModel.instance.user?.name)){

                        chatId?.let { timestamp?.let { it1 -> lastMessage?.let { it2 -> ChatList(users, it, it1, it2) }?.let { it2 -> newChatList.add(it2) } } }
                    }
                }

                chatList = newChatList
                onChatListLoadListener?.onFetchChatList(chatList!!)
            }

        })

    }

    fun addChat(users: MutableList<String>) {
        val childRef = chatListRef?.push()
        childRef?.setValue(childRef.key?.let { ChatList.newChatList(users = users, chatId = it) })
    }

}
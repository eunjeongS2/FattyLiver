package kr.ac.ajou.fattyliver.model

import com.google.firebase.database.*

class ChatModel(chatId: String) {
    private var chats: MutableList<Chat>? = null
    //private var onDataChangedListener : OnDataChangedListener? = null
    private var onChatLoadListener: OnChatLoadListener? = null

    interface OnChatLoadListener {
        fun onFetchChat(chatList: MutableList<Chat>)
    }

    private var chatRef : DatabaseReference? = null

    init {
        chats = mutableListOf()
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        //chatRef = database.getReference("user").child(UserModel.instance.user?.uid).child("chats")
        chatRef = database.getReference("chats").child(chatId).child("message")


        this.chatRef?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val newChats: MutableList<Chat> = mutableListOf()
                val children: MutableIterable<DataSnapshot>? = p0.children

                for (e in children!!) {
                    val chat: Chat? = e.getValue(Chat::class.java)
                    chat?.let { newChats.add(it) }
                }

                chats = newChats
                onChatLoadListener?.onFetchChat(chats!!)
            }
        })

    }

    fun setOnChatLoadListener(onChatLoadListener: OnChatLoadListener) {
        this.onChatLoadListener = onChatLoadListener
    }

    fun sendMessage(message: String) {
        val childRef = chatRef?.push()
        childRef?.setValue(Chat.newChat(UserModel.instance.user?.name, message))
    }





}
package kr.ac.ajou.fattyliver.model

import com.google.firebase.database.*

class ChatModel(chatId: String) {
    private var chats: MutableList<Chat>? = null
    var onChatLoadListener: OnChatLoadListener? = null
    private var database: FirebaseDatabase? = null

    interface OnChatLoadListener {
        fun onFetchChat(chatList: MutableList<Chat>, notice: Notice?)
    }

    private var chatRef : DatabaseReference? = null

    init {
        chats = mutableListOf()
        database = FirebaseDatabase.getInstance()
        //chatRef = database.getReference("user").child(UserModel.instance.user?.uid).child("chats")
        chatRef = database?.getReference("chats")?.child(chatId)


        this.chatRef?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val newChats: MutableList<Chat> = mutableListOf()
                val children: MutableIterable<DataSnapshot>? = p0.children
                var notice: Notice? = null

                loop@ for (e in children!!) {
                    when(e.key){
                        "message" -> {
                            e.children
                                    .map { it.getValue(Chat::class.java) }
                                    .forEach { chat -> chat?.let { newChats.add(it) } }
                        }
                        "notice" -> notice = e.getValue(Notice::class.java)
                        else -> continue@loop
                    }
                }
                chats = newChats
                onChatLoadListener?.onFetchChat(chats!!, notice)

            }
        })

    }

    fun sendMessage(message: String) {
        val childRef = chatRef?.child("message")?.push()
        childRef?.setValue(Chat.newChat(UserModel.instance.user?.name, message))
    }

    fun addNotice(notice: String, time: String){
        chatRef?.child("notice")?.child("notice")?.setValue(notice)
        chatRef?.child("notice")?.child("time")?.setValue(time)
    }


}
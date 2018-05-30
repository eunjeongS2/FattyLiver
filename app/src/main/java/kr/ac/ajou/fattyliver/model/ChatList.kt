package kr.ac.ajou.fattyliver.model

import java.text.SimpleDateFormat
import java.util.*

class ChatList(var users: MutableList<String> = mutableListOf(), var chatId: String = "", var timestamp: String = "", var lastMessage: Chat? = null, var alcoholImage: String){
    companion object {
        fun newChatList(users: MutableList<String>, chatId: String, lastMessage: Chat = Chat(), alcoholImage: String): ChatList {
            return ChatList(users, chatId, timestamp(), lastMessage, alcoholImage)
        }

        private fun timestamp(): String {
            val date = Date()
            val dateFormat = SimpleDateFormat("a h:mm", Locale.KOREA)
            return dateFormat.format(date)
        }
    }
}

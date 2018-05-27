package kr.ac.ajou.fattyliver.model

import java.text.SimpleDateFormat
import java.util.*

class ChatList(var users: MutableList<String> = mutableListOf(), var chatId: String = "", var timestamp: String = ""){
    companion object {
        fun newChatList(users: MutableList<String>, chatId: String): ChatList {
            return ChatList(users, chatId, timestamp())
        }

        private fun timestamp(): String {
            val date = Date()
            val dateFormat = SimpleDateFormat("a h:mm", Locale.KOREA)
            return dateFormat.format(date)
        }
    }
}

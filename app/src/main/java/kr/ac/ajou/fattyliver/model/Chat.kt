package kr.ac.ajou.fattyliver.model

import java.text.SimpleDateFormat
import java.util.*

class Chat(val uid: String?="", val userId: String?="", val message: String="", val timestamp: String="", val imageUrl: String="") {

    companion object {
        fun newChat(userId: String?, message: String): Chat {
            return Chat(UserModel.instance.user?.uid, userId, message, timestamp())
        }

        fun newChatWithImage(userId: String?, message: String, imageUrl: String): Chat {
            return Chat(UserModel.instance.user?.uid, userId, message, timestamp(), imageUrl)
        }

        private fun timestamp(): String {
            val date = Date()
            val dateFormat = SimpleDateFormat("a h:mm", Locale.KOREA)
            return dateFormat.format(date)
        }
    }
}
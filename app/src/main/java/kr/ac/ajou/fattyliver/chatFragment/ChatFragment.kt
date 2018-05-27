package kr.ac.ajou.fattyliver.chatFragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.ac.ajou.fattyliver.OnDataChangedListener

import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.model.Alarm
import kr.ac.ajou.fattyliver.model.Chat
import kr.ac.ajou.fattyliver.model.ChatModel

class ChatFragment : Fragment(), ChatModel.OnChatLoadListener {

    private var chatModel: ChatModel? = null
    private lateinit var chatListRecyclerView : RecyclerView
    private var adapter : ChatListRecyclerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        chatModel = ChatModel()

        chatListRecyclerView = view.findViewById(R.id.chat_recyclerView)
        chatModel?.setOnChatLoadListener(this)

        setUpChatListView()

        val chatEdit: AppCompatEditText = view.findViewById(R.id.field_message)
        val sendButton: AppCompatButton = view.findViewById(R.id.send_message_btn)
        sendButton.setOnClickListener {
            val message = chatEdit.text.toString()
            if(message.isNotEmpty()) {
                chatModel?.sendMessage(message)
                chatEdit.setText("")
            }

        }

        return view
    }

    private fun setUpChatListView() {
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        chatListRecyclerView.layoutManager = manager
        adapter = ChatListRecyclerAdapter()
        chatListRecyclerView.adapter = adapter
    }

    override fun onFetchAlarm(chatList: MutableList<Chat>) {
        adapter?.setItems(chatList)
        adapter?.notifyDataSetChanged()
        chatListRecyclerView.scrollToPosition(chatList.size - 1)
    }


}

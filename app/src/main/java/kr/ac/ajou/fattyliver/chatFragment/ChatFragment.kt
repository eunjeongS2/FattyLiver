package kr.ac.ajou.fattyliver.chatFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.model.Chat
import kr.ac.ajou.fattyliver.model.ChatModel
import kr.ac.ajou.fattyliver.model.Notice

class ChatFragment : Fragment(), ChatModel.OnChatLoadListener {

    companion object {
        const val REQUEST_CODE = 100
    }

    private lateinit var chatListRecyclerView : RecyclerView
    private lateinit var noticeLayout : ConstraintLayout
    private var adapter : ChatRecyclerAdapter? = null
    private var chatModel: ChatModel? = null
    private lateinit var noticeTextView: TextView
    private lateinit var noticeTimeTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        val chatId: String? = arguments?.getString("chatId")
        val backButton : ImageView = view.findViewById(R.id.button_chat_cancel)
        noticeTextView = view.findViewById(R.id.textView_chat_notice)
        noticeTimeTextView = view.findViewById(R.id.textView_chat_notice_time)

        backButton.setOnClickListener{ fragmentManager?.popBackStackImmediate() }

        chatModel = chatId?.let { ChatModel(it) }
        chatModel?.onChatLoadListener = this

        chatListRecyclerView = view.findViewById(R.id.chat_recyclerView)

        setUpChatListView()

        noticeLayout = view.findViewById(R.id.layout_chat_notice)
        noticeLayout.visibility = View.INVISIBLE

        val chatEdit: AppCompatEditText = view.findViewById(R.id.field_message)
        val sendButton: ImageView = view.findViewById(R.id.button_chat_send)

        sendButton.setOnClickListener {
            val message = chatEdit.text.toString()
            if(message.isNotEmpty()) {
                chatModel?.sendMessage(message)
                chatEdit.setText("")
            }
        }

        val noticeButton: ImageView = view.findViewById(R.id.filter_button)
        noticeButton.setOnClickListener{
            val dialog = AddNoticeDialogFragment()
            val transaction : FragmentTransaction? = fragmentManager?.beginTransaction()
            transaction?.addToBackStack(null)

            val args = Bundle()
            args.putString("chatId", chatId)
            dialog.arguments = args

            dialog.setTargetFragment(this, ChatFragment.REQUEST_CODE)
            dialog.show(transaction, AddNoticeDialogFragment.TAG)
        }

        return view
    }

    private fun setUpChatListView() {
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        chatListRecyclerView.layoutManager = manager
        adapter = ChatRecyclerAdapter()
        chatListRecyclerView.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    override fun onFetchChat(chatList: MutableList<Chat>, notice: Notice?) {
        adapter?.setItems(chatList)
        adapter?.notifyDataSetChanged()
        chatListRecyclerView.scrollToPosition(chatList.size - 1)

        notice?.let {
            noticeLayout.visibility = View.VISIBLE
            noticeTextView.text = notice.notice
            noticeTimeTextView.text = "마감시간 : ${notice.time}"
        }
    }

}

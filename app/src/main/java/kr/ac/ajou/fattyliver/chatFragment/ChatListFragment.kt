package kr.ac.ajou.fattyliver.chatFragment

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.ac.ajou.fattyliver.AbstractRecyclerAdapter
import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.mainTabFragment.MainTabFragment
import kr.ac.ajou.fattyliver.model.ChatList
import kr.ac.ajou.fattyliver.model.ChatListModel


class ChatListFragment : Fragment(), AbstractRecyclerAdapter.OnItemClickListener<ChatList>, ChatListModel.OnChatListLoadListener {

    private lateinit var chatListRecyclerView : RecyclerView
    private lateinit var addButton : FloatingActionButton
    private lateinit var chatListModel : ChatListModel

    private var adapter : ChatListRecyclerAdapter? = null

    companion object {
        const val REQUEST_CODE = 300
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragemnt_chat_list, container, false)

        chatListRecyclerView = view.findViewById(R.id.recyclerView_chat_list)
        addButton = view.findViewById(R.id.button_chatList_add)
        chatListModel = ChatListModel()
        chatListModel.onChatListLoadListener = this

        addButton.setOnClickListener{
            val dialog = AddChatDialogFragment()
            val transaction : FragmentTransaction? = fragmentManager?.beginTransaction()
            transaction?.addToBackStack(null)
            dialog.setTargetFragment(this, ChatListFragment.REQUEST_CODE)
            dialog.show(transaction, AddChatDialogFragment.TAG)
        }

        setUpChatListView()

        return view

    }
    private fun setUpChatListView(){
        chatListRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager(context).orientation))
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        chatListRecyclerView.layoutManager = manager
        adapter = ChatListRecyclerAdapter()
        chatListRecyclerView.adapter = adapter
        adapter?.onItemClickListener = this

    }

    override fun onItemClick(item: ChatList, position: Int) {
        val chat = ChatFragment()
        val transaction : FragmentTransaction? = fragmentManager?.beginTransaction()

        val args = Bundle()
        args.putString("chatId", item.chatId)
        chat.arguments = args
        chat.setTargetFragment(this, MainTabFragment.REQUEST_CODE)

        transaction?.replace(R.id.main_container, chat)
        transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction?.addToBackStack(null)
        transaction?.commit()

    }

    override fun onFetchChatList(chatList: MutableList<ChatList>) {
        adapter?.setItems(chatList)
        adapter?.notifyDataSetChanged()

    }
}


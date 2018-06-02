package kr.ac.ajou.fattyliver.chatFragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.model.ChatListModel
import kr.ac.ajou.fattyliver.model.UserModel
import kr.ac.ajou.fattyliver.model.UserSelect
import kr.ac.ajou.fattyliver.model.UsersModel

class AddChatDialogFragment : DialogFragment(), UsersModel.OnUserListLoadListener, UserListRecyclerAdapter.OnUserSelectedListener{

    companion object {
        const val TAG = "AddChatDialogFragment"
    }

    private var recyclerView : RecyclerView? = null
    private var adapter : UserListRecyclerAdapter? = null
    private var addButton : Button? = null
    private var usersModel : UsersModel? = null
    private var alcoholImage: String = "beer"


    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)

        val inflater : LayoutInflater? = activity?.layoutInflater
        val view : View? = inflater?.inflate(R.layout.fragment_chat_add, null)

        recyclerView = view?.findViewById(R.id.recyclerView_chat_add)
        val closeButton = view?.findViewById<ImageView>(R.id.button_add_cancel)
        addButton = view?.findViewById(R.id.button_add_user)

        val beerImageView = view?.findViewById<ImageView>(R.id.imageView_beer)
        val sojuImageView = view?.findViewById<ImageView>(R.id.imageView_soju)
        val cocktailImageView = view?.findViewById<ImageView>(R.id.imageView_cocktail)
        val wineImageView = view?.findViewById<ImageView>(R.id.imageView_wine)

        beerImageView?.setOnClickListener{
            v -> v.isSelected = !v.isSelected

            if (v.isSelected){
                alcoholImage = "beer"
                sojuImageView?.isSelected = false
                cocktailImageView?.isSelected = false
                wineImageView?.isSelected = false
            }
        }

        sojuImageView?.setOnClickListener{
            v -> v.isSelected = !v.isSelected

            if (v.isSelected){
                alcoholImage = "soju"
                beerImageView?.isSelected = false
                cocktailImageView?.isSelected = false
                wineImageView?.isSelected = false
            }
        }

        cocktailImageView?.setOnClickListener{
            v -> v.isSelected = !v.isSelected

            if (v.isSelected){
                alcoholImage = "cocktail"
                beerImageView?.isSelected = false
                sojuImageView?.isSelected = false
                wineImageView?.isSelected = false
            }
        }

        wineImageView?.setOnClickListener{
            v -> v.isSelected = !v.isSelected

            if (v.isSelected){
                alcoholImage = "wine"
                beerImageView?.isSelected = false
                sojuImageView?.isSelected = false
                cocktailImageView?.isSelected = false
            }
        }

        val currentUser = UserModel.instance.user

        addButton?.setOnClickListener{
            if (usersModel?.selectedUserList?.size != 0){
                currentUser?.let {
                    usersModel?.addUsers(UserSelect.newUserSelect(currentUser.name, currentUser.uid, true))
                }
                ChatListModel().addChat(usersModel?.selectedUserList?.map { it.name } as MutableList<String>, alcoholImage)
            }
            dismiss()
        }

        closeButton?.setOnClickListener { dismiss() }

        usersModel = UsersModel()
        usersModel?.onUserListLoadListener = this

        setUpRecyclerView()


        builder.setView(view)

        val alertDialog : AlertDialog = builder.create()
        alertDialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                fragmentManager?.popBackStack()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        alertDialog.setCanceledOnTouchOutside(false)
        return alertDialog
    }

    private fun setUpRecyclerView(){
        recyclerView?.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager(context).orientation))
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = UserListRecyclerAdapter()
        adapter?.onUserSelectedListener = this
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = manager

    }
    override fun onFetchUserList(userList: MutableList<UserSelect>) {
        adapter?.setItems(userList)
        adapter?.notifyDataSetChanged()

    }

    override fun onUserSelect(userSelect: UserSelect, selected: Boolean) {
        if(selected) usersModel?.addUsers(userSelect)

        else usersModel?.removeUsers(userSelect)

    }

//    override fun onStart() {
//        super.onStart()
//
//        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//    }
}

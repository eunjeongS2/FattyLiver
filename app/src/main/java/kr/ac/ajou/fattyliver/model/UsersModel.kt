package kr.ac.ajou.fattyliver.model

import com.google.firebase.database.*

class UsersModel{
    private var userList: MutableList<UserSelect>? = null
    private var userListRef: DatabaseReference? = null
    var selectedUserList: MutableSet<UserSelect>? = null

    var onUserListLoadListener: OnUserListLoadListener? = null

    interface OnUserListLoadListener {
        fun onFetchUserList(userList: MutableList<UserSelect>)
    }

    init {
        userList = mutableListOf()
        selectedUserList = mutableSetOf()
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        userListRef = database.getReference("user")

        this.userListRef?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val newUserList: MutableList<UserSelect> = mutableListOf()
                val children: MutableIterable<DataSnapshot>? = p0.children

                for (e in children!!) {
                    val userList = e.child("info").children.first().getValue<User>(User::class.java)
                    userList?.let {
                        newUserList.add(UserSelect.newUserSelect(userList.name, userList.uid, false))
                    }
                }
                userList = newUserList

                onUserListLoadListener?.onFetchUserList(userList!!)
            }
        })
    }

    fun addUsers(user: UserSelect){
        selectedUserList?.add(user)
    }

    fun removeUsers(user: UserSelect){
        selectedUserList?.remove(user)
    }
}
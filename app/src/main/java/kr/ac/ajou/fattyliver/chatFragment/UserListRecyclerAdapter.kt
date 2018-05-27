package kr.ac.ajou.fattyliver.chatFragment

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import kr.ac.ajou.fattyliver.AbstractRecyclerAdapter
import kr.ac.ajou.fattyliver.AbstractViewHolder
import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.model.UserSelect
import kr.ac.ajou.fattyliver.model.UsersModel

class UserListRecyclerAdapter : AbstractRecyclerAdapter<UserSelect>() {

    var onUserSelectedListener: OnUserSelectedListener? = null

    interface OnUserSelectedListener{
        fun onUserSelect(userSelect: UserSelect, selected: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<UserSelect> {
        return UserListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<UserSelect>, @SuppressLint("RecyclerView") position: Int) {
        super.onBindViewHolder(holder, position)

        val checkBox: CheckBox = holder.itemView.findViewById(R.id.checkBox_user)
        checkBox.isChecked = getItem(position)?.selected ?: false

        checkBox.setOnCheckedChangeListener { p0, p1 -> getItem(position)?.let { onUserSelectedListener?.onUserSelect(it, p1) } }
    }
}
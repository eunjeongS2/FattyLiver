package kr.ac.ajou.fattyliver.mainTabFragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.support.v4.app.DialogFragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kr.ac.ajou.fattyliver.model.Alcohol
import kr.ac.ajou.fattyliver.R

class DateAlcoholDialogFragment : DialogFragment(){
    companion object {
        const val TAG = "DateAlcoholDialogFragment"
    }

    private var recyclerView : RecyclerView? = null

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alcohols: ArrayList<Alcohol>? = arguments?.getParcelableArrayList<Alcohol>("date alcohols")

        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)

        val inflater : LayoutInflater? = activity?.layoutInflater
        val view : View? = inflater?.inflate(R.layout.fragment_date_alcohol, null)

        recyclerView = view?.findViewById(R.id.recyclerView_date_alcohol)
        val closeButton = view?.findViewById<ImageView>(R.id.button_close)

        alcohols.let { it?.let { it1 -> setUpRecyclerView(it1) } }
        closeButton?.setOnClickListener { dismiss() }

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

    private fun setUpRecyclerView(alcohols : ArrayList<Alcohol>){
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = manager
        val adapter = DateAlcoholListRecyclerAdapter()
        recyclerView?.adapter = adapter

        alcohols.let { adapter.setItems(it) }
        adapter.notifyDataSetChanged()

    }

    override fun onStart() {
        super.onStart()

        val dialogWidth = 800

        dialog.window.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}

package kr.ac.ajou.fattyliver


import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.DividerItemDecoration


class Tab2Fragment : RootFragment(), OnAlarmLoadListener {

    private var adapter: AlarmListRecyclerAdapter? = null
    private var alarmRecyclerView : RecyclerView? = null
    private var alarmModel : AlarmModel? = null
    private var addAlarmButton: FloatingActionButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_tab2, container, false)
        alarmRecyclerView = view.findViewById(R.id.alarm_recyclerView)
        addAlarmButton = view.findViewById(R.id.add_alarm_button)

        setUpAlarmListView()

        alarmModel = AlarmModel()
        alarmModel?.setOnAlarmLoadListener(this)
        alarmModel?.fetchAlarm()

        addAlarmButton?.setOnClickListener {
            val frag: Fragment = AddAlarmFragment()
            val fmanager: FragmentManager? = fragmentManager
            val ftrans: FragmentTransaction = fmanager!!.beginTransaction()
            ftrans.add(R.id.container, frag)
            ftrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ftrans.addToBackStack(null)
            ftrans.commit()
        }

        return view
    }

    private fun setUpAlarmListView() {
        alarmRecyclerView?.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager(context).orientation))
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        alarmRecyclerView?.layoutManager = manager
        adapter = AlarmListRecyclerAdapter()
        alarmRecyclerView?.adapter = adapter
    }

    override fun onFetchAlarm(alarmList: MutableList<Alarm>) {
        adapter?.setItems(alarmList)
        adapter?.notifyDataSetChanged()
    }

    override fun onAddAlarm(alarm: Alarm) {

    }


}

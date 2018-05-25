package kr.ac.ajou.fattyliver.AlarmFragment


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
import kr.ac.ajou.fattyliver.AddAlarmFragment
import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.Model.Alarm
import kr.ac.ajou.fattyliver.model.AlarmModel


class Tab2Fragment : Fragment(), AlarmModel.OnAlarmLoadListener {

    private var adapter: AlarmListRecyclerAdapter? = null
    private var alarmRecyclerView: RecyclerView? = null
    private var alarmModel: AlarmModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_tab2, container, false)
        alarmRecyclerView = view.findViewById(R.id.alarm_recyclerView)
        val addAlarmButton: FloatingActionButton = view.findViewById(R.id.add_alarm_button)

        setUpAlarmListView()
        alarmRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    if (addAlarmButton.isShown) addAlarmButton.hide()
                }
                else if (dy < 0) {
                    if(!addAlarmButton.isShown) addAlarmButton.show()
                }
            }
        })

        alarmModel = AlarmModel()
        alarmModel?.setOnAlarmLoadListener(this)
//        alarmModel?.fetchAlarm()

        addAlarmButton.setOnClickListener {
            val fmanager: FragmentManager? = fragmentManager
            val ftrans: FragmentTransaction? = fmanager?.beginTransaction()
            ftrans?.replace(R.id.main_container, AddAlarmFragment())
            ftrans?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ftrans?.addToBackStack(null)
            ftrans?.commit()

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
        alarmRecyclerView?.scrollToPosition(alarmList.size - 1)

    }

}

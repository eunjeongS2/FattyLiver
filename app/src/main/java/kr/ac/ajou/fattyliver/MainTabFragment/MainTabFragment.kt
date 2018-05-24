package kr.ac.ajou.fattyliver.mainTabFragment

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.fragment_main_tab.view.*
import kr.ac.ajou.fattyliver.OnDataChangedListener
import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.model.Alcohol
import kr.ac.ajou.fattyliver.model.AlcoholModel
import kr.ac.ajou.fattyliver.model.ChartModel
import kr.ac.ajou.fattyliver.model.UserModel

class MainTabFragment : Fragment(), ChartModel.OnChartLoadListener, OnDataChangedListener, OnChartValueSelectedListener, CalendarFragment.OnDateChangedListener {

    private lateinit var idTextView : TextView
    private lateinit var calendarImageView : ImageView
    private lateinit var chartView : LineChart
    private var xAxis : XAxis? = null
    private var yAxis : YAxis? = null
    private lateinit var alcoholListRecyclerView : RecyclerView
    private lateinit var liverImageView : ImageView
    private lateinit var fattyLiverImageView : ImageView
    private lateinit var averageTextView : TextView
    private lateinit var colorTextView : TextView
    private lateinit var progressBar : ProgressBar

    private var adapter : AlcoholListRecyclerAdapter? = null
    private var chartModel : ChartModel? = null
    private var alcoholModel : AlcoholModel? = null
    private var alcohols : MutableList<Alcohol>? = null
    private var filterAlcohols : MutableList<Alcohol>? = null

    companion object {
        const val REQUEST_CODE = 300
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_main_tab, container, false)
        idTextView = view.textView_id
        calendarImageView = view.imageView_calendar
        chartView = view.chart_line
        alcoholListRecyclerView = view.recyclerView_main
        liverImageView = view.imageView_liver
        fattyLiverImageView = view.imageView_fattyliver
        averageTextView = view.textView_average
        colorTextView = view.textView_color
        progressBar = view.progressBar_main

        setViewVisible(View.INVISIBLE)
        progressBar.visibility = View.VISIBLE

        idTextView.text = "${UserModel.instance.user?.name} 님!"

        calendarImageView.setOnClickListener {
            val dialog = CalendarFragment()
            val transaction : FragmentTransaction? = fragmentManager?.beginTransaction()
            transaction?.addToBackStack(null)

            val args = Bundle()
            alcohols?.let {
                alcohols = alcoholModel?.getMaxAlcohols()
                args.putString("startDate", alcohols?.get(0)?.timestamp)
                dialog.arguments = args
                dialog.setTargetFragment(this, REQUEST_CODE)
                dialog.show(transaction, CalendarFragment.TAG)
            }
        }
        alcohols = mutableListOf()
        filterAlcohols = mutableListOf()

        alcoholModel = AlcoholModel()

        alcoholModel?.setOnDataChangedListener(this)

        chartModel = ChartModel()
        chartModel?.setOnChartLoadListener(this)

        initChart()

        setUpAlcoholListView()

        return view
    }

    private fun initChart(){
        chartView.setOnChartValueSelectedListener(this)
        chartView.axisRight?.isEnabled = false
        chartView.setTouchEnabled(true)
        chartView.isDragEnabled = false
        chartView.setScaleEnabled(false)
        chartView.setPinchZoom(false)
        chartView.setDrawGridBackground(true)
        chartView.description?.isEnabled = false
        chartView.legend?.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT

        xAxis = chartView.xAxis
        xAxis?.textSize = 10f
        xAxis?.textColor = Color.BLACK

        xAxis?.setDrawAxisLine(true)
        xAxis?.setDrawGridLines(false)

        yAxis = chartView.axisLeft

        xAxis?.position = XAxis.XAxisPosition.BOTTOM
    }

    private fun setUpAlcoholListView(){
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        alcoholListRecyclerView.layoutManager = manager
        adapter = AlcoholListRecyclerAdapter()
        alcoholListRecyclerView.adapter = adapter

    }

    override fun onLoad(dataSet: LineDataSet, labels: MutableSet<String>) {
        context?.let { ContextCompat.getColor(it, R.color.colorBlue) }?.let { dataSet.setCircleColor(it) }
        dataSet.color = context?.let { ContextCompat.getColor(it, R.color.colorBlue) }!!
        dataSet.circleRadius = 4.3F
        dataSet.lineWidth = 1.5f
        dataSet.valueTextSize = 8F

        chartView.data = LineData(dataSet)
        xAxis?.granularity = 1f
        xAxis?.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis?.labelCount = labels.size

        chartView.animateXY(500, 2000)
        chartView.invalidate()
    }

    override fun onDataChanged() {
        progressBar.visibility = View.INVISIBLE

        alcohols = alcoholModel?.getMaxAlcohols()


        if (filterAlcohols?.size == 0) {
            if (alcohols?.size!! <= 5)
                filterAlcohols = alcohols
            else {
                alcohols?.get(alcohols?.size!!-5)?.let { filterAlcohols?.add(it) }
                alcohols?.get(alcohols?.size!!-4)?.let { filterAlcohols?.add(it) }
                alcohols?.get(alcohols?.size!!-3)?.let { filterAlcohols?.add(it) }
                alcohols?.get(alcohols?.size!!-2)?.let { filterAlcohols?.add(it) }
                alcohols?.get(alcohols?.size!!-1)?.let { filterAlcohols?.add(it) }
            }
        }

        updateView()

    }

    private fun loadChart(alcohols: MutableList<Alcohol>?) {
        chartModel?.setDataList(alcohols as ArrayList<Alcohol>)
        chartModel?.loadChart()
    }

    override fun onNothingSelected() {
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        val dateAlcohols : MutableList<Alcohol>? = e?.x?.toInt()?.let { xAxis?.getFormattedLabel(it) }.toString().let { alcoholModel?.getDateAlcohols(it) }

        val dialog = DateAlcoholDialogFragment()
        val transaction : FragmentTransaction? = fragmentManager?.beginTransaction()
        transaction?.addToBackStack(null)

        val args = Bundle()
        args.putParcelableArrayList("date alcohols", dateAlcohols as java.util.ArrayList<out Parcelable>)
        dialog.arguments = args

        dialog.show(transaction, DateAlcoholDialogFragment.TAG)

    }

    override fun onDateChanged(dates: MutableList<String>) {
        filterAlcohols = mutableListOf()

        dates.forEach {
            val filterDate = it.split("/")[1]

            val saveAlcohol : Alcohol? = alcohols?.firstOrNull { filterDate == it.timestamp.split("/")[1] }

            saveAlcohol?.let { it1 -> filterAlcohols?.add(it1) }
        }

        updateView()

    }

    @SuppressLint("SetTextI18n")
    private fun updateView(){
        setViewVisible(View.VISIBLE)

        val alcoholsMap = filterAlcohols?.map { it.timestamp to it.value }?.toMap()

        var average = 0.0

        if (filterAlcohols?.size != 0) {
            loadChart(filterAlcohols)
            average = alcoholsMap?.values?.average()!!
        }

        fattyLiverImageView.alpha = (average.div(0.3)).toFloat()
        averageTextView.text = "평균 : ${average.toFloat()}"

        filterAlcohols?.let { adapter?.setItems(it) }
        adapter?.notifyDataSetChanged()


        val animation : Animation = AnimationUtils.loadAnimation(context, R.anim.animation_scale)

        liverImageView.startAnimation(animation)
        fattyLiverImageView.startAnimation(animation)

    }

    private fun setViewVisible(invisible: Int) {
        calendarImageView.visibility = invisible
        chartView.visibility = invisible
        alcoholListRecyclerView.visibility = invisible
        liverImageView.visibility = invisible
        fattyLiverImageView.visibility = invisible
        averageTextView.visibility = invisible
        colorTextView.visibility = invisible
    }

}

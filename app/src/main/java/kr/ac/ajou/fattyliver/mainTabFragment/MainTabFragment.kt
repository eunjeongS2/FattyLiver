package kr.ac.ajou.fattyliver.mainTabFragment

import android.annotation.SuppressLint
import android.app.Dialog
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
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
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
import kr.ac.ajou.fattyliver.R
import kr.ac.ajou.fattyliver.model.Alcohol
import kr.ac.ajou.fattyliver.model.AlcoholModel
import kr.ac.ajou.fattyliver.model.ChartModel
import kr.ac.ajou.fattyliver.model.UserModel

class MainTabFragment : Fragment(), ChartModel.OnChartLoadListener, AlcoholModel.OnDataChangedListener, OnChartValueSelectedListener, CalendarFragment.OnDateChangedListener {

    private lateinit var idTextView: TextView
    private lateinit var calendarImageView: ImageView
    private lateinit var chartView: LineChart
    private var xAxis: XAxis? = null
    private var yAxis: YAxis? = null
    private lateinit var alcoholListRecyclerView: RecyclerView
    private lateinit var liverImageView: ImageView
    private lateinit var fattyLiverImageView: ImageView
    private lateinit var averageTextView: TextView
    private lateinit var colorTextView: TextView
    private lateinit var progressBar: ProgressBar

    private var adapter: AlcoholListRecyclerAdapter? = null
    private var chartModel: ChartModel? = null
    private var alcoholModel: AlcoholModel? = null
    private var alcohols: MutableList<Alcohol>? = null
    private var filterAlcohols: MutableList<Alcohol>? = null

    companion object {
        const val REQUEST_CODE = 300
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_main_tab, container, false)
        idTextView = view.findViewById(R.id.textView_id)
        calendarImageView = view.findViewById(R.id.imageView_calendar)
        chartView = view.findViewById(R.id.chart_line)
        alcoholListRecyclerView = view.findViewById(R.id.recyclerView_main)
        liverImageView = view.findViewById(R.id.imageView_liver)
        fattyLiverImageView = view.findViewById(R.id.imageView_fattyliver)
        averageTextView = view.findViewById(R.id.textView_average)
        colorTextView = view.findViewById(R.id.textView_color)
        progressBar = view.findViewById(R.id.progressBar_main)
        val infoImageView = view.findViewById<ImageView>(R.id.imageView_main_info)

        setViewVisible(View.INVISIBLE)
        progressBar.visibility = View.VISIBLE

        idTextView.text = "${UserModel.instance.user?.name} 님!"

        infoImageView.setOnClickListener {
            val infoDialog = Dialog(context)
            infoDialog.window.requestFeature(Window.FEATURE_NO_TITLE)
            val infoDialogLayout = layoutInflater.inflate(R.layout.layout_info_image, null)
            infoDialog.setContentView(infoDialogLayout)
            infoDialogLayout.findViewById<ImageView>(R.id.imageView_info_cancel).setOnClickListener{ infoDialog.dismiss() }
            infoDialog.show()
        }

        calendarImageView.setOnClickListener {
            val dialog = CalendarFragment()
            val transaction: FragmentTransaction? = fragmentManager?.beginTransaction()
            transaction?.addToBackStack(null)

            val args = Bundle()
            alcohols = alcoholModel?.getMaxAlcohols()

            if (alcohols?.size != 0) {
                args.putString("startDate", alcohols?.get(0)?.timestamp)
                dialog.arguments = args
                dialog.setTargetFragment(this, REQUEST_CODE)
                dialog.show(transaction, CalendarFragment.TAG)
            } else Toast.makeText(context, "데이터가 없습니다 !", Toast.LENGTH_SHORT).show()
        }
        alcohols = mutableListOf()
        filterAlcohols = mutableListOf()

        alcoholModel = AlcoholModel()
        alcoholModel?.onDataChangedListener = this

        alcoholModel?.addAlcohol("2018/5.9/오후 11:00", 0.12)
        alcoholModel?.addAlcohol("2018/5.10/오후 11:00", 0.08)
        alcoholModel?.addAlcohol("2018/5.11/오후 11:00", 0.09)
        alcoholModel?.addAlcohol("2018/5.12/오후 11:00", 0.10)
        alcoholModel?.addAlcohol("2018/5.13/오후 11:00", 0.13)
        alcoholModel?.addAlcohol("2018/5.14/오후 11:00", 0.15)
        alcoholModel?.addAlcohol("2018/5.15/오후 11:00", 0.20)


        chartModel = ChartModel()
        chartModel?.onChartLoadListener = this

        initChart()

        setUpAlcoholListView()

        return view
    }

    private fun initChart() {
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

    private fun setUpAlcoholListView() {
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        alcoholListRecyclerView.layoutManager = manager
        adapter = AlcoholListRecyclerAdapter()
        alcoholListRecyclerView.adapter = adapter

    }

    override fun onLoad(dataSet: LineDataSet, labels: MutableSet<String>) {
        activity?.let {
            context?.let { ContextCompat.getColor(it, R.color.colorBlue) }?.let { dataSet.setCircleColor(it) }
            dataSet.color = context?.let { ContextCompat.getColor(it, R.color.colorBlue) }!!
            dataSet.circleRadius = 4.3F
            dataSet.lineWidth = 1.5f
            dataSet.valueTextSize = 8F

            xAxis?.granularity = 1f
            xAxis?.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis?.labelCount = labels.size

            chartView.data = LineData(dataSet)
            chartView.animateXY(500, 2000)
            chartView.invalidate()
        }
    }

    override fun onDataChanged() {
        println("onDataChanged")
        progressBar.visibility = View.INVISIBLE
        filterAlcohols = mutableListOf()

        alcohols = alcoholModel?.getMaxAlcohols()

        if (alcohols?.size!! <= 5) {
            filterAlcohols = alcohols
        } else {
            alcohols?.get(alcohols?.size!! - 5)?.let { filterAlcohols?.add(it) }
            alcohols?.get(alcohols?.size!! - 4)?.let { filterAlcohols?.add(it) }
            alcohols?.get(alcohols?.size!! - 3)?.let { filterAlcohols?.add(it) }
            alcohols?.get(alcohols?.size!! - 2)?.let { filterAlcohols?.add(it) }
            alcohols?.get(alcohols?.size!! - 1)?.let { filterAlcohols?.add(it) }
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
        val dateAlcohols = e?.x?.toInt()?.let { filterAlcohols?.get(it)?.timestamp?.split("/")?.get(1)?.let { it1 -> alcoholModel?.getDateAlcohols(it1) } }

        val dialog = DateAlcoholDialogFragment()
        val transaction: FragmentTransaction? = fragmentManager?.beginTransaction()
        transaction?.addToBackStack(null)

        val args = Bundle()

        dateAlcohols?.let {
            args.putParcelableArrayList("date alcohols", dateAlcohols as java.util.ArrayList<out Parcelable>)
            dialog.arguments = args

            dialog.show(transaction, DateAlcoholDialogFragment.TAG)
        }
    }

    override fun onDateChanged(dates: MutableList<String>) {
        filterAlcohols = mutableListOf()

        dates.forEach {
            val filterDate = it.split("/")[1]

            val saveAlcohol: Alcohol? = alcohols?.firstOrNull { filterDate == it.timestamp.split("/")[1] }

            saveAlcohol?.let { it1 -> filterAlcohols?.add(it1) }
        }
        updateView()
    }

    @SuppressLint("SetTextI18n")
    private fun updateView() {
        var average = 0.0

        val alcoholsMap = filterAlcohols?.map { it.timestamp to it.value }?.toMap()

        if (filterAlcohols?.size != 0) {
            loadChart(filterAlcohols)
            average = alcoholsMap?.values?.average()!!
        }

        fattyLiverImageView.alpha = (average.div(0.3)).toFloat()
        averageTextView.text = "평균 : ${average.toFloat()}"

        filterAlcohols?.let { adapter?.setItems(it) }
        adapter?.notifyDataSetChanged()

        activity?.let {
            setViewVisible(View.VISIBLE)

            val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.animation_scale)

            liverImageView.startAnimation(animation)
            fattyLiverImageView.startAnimation(animation)
        }
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

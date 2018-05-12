package kr.ac.ajou.fattyliver

import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class MainTabFragment : Fragment(), ChartModel.OnChartLoadListener, OnDataChangedListener {

    private var idTextView : TextView? = null
    private var calendarImageView : ImageView? = null
    private var chartView : LineChart? = null
    private var xAxis : XAxis? = null
    private var yAxis : YAxis? = null
    private var alcoholListRecyclerView : RecyclerView? = null
    private var adapter : AlcoholListRecyclerAdapter? = null
    private var chartModel : ChartModel? = null
    private var alcoholModel : AlcoholModel? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.main_tab_fragment, container, false)
        idTextView = view.findViewById(R.id.textView_id)
        calendarImageView = view.findViewById(R.id.imageView_calendar)
        chartView = view.findViewById(R.id.chart_line)
        alcoholListRecyclerView = view.findViewById(R.id.recyclerView_main)

        alcoholModel = AlcoholModel()
        alcoholModel?.setOnDataChangedListener(this)
        chartModel = ChartModel()
        chartModel?.setOnChartLoadListener(this)

        initChart()

        setUpAlcoholListView()

        return view
    }

    private fun initChart(){
        chartView?.axisRight?.isEnabled = false
        chartView?.setTouchEnabled(true)
        chartView?.isDragEnabled = false
        chartView?.setScaleEnabled(false)
        chartView?.setPinchZoom(false)
        chartView?.setDrawGridBackground(true)
        chartView?.description?.isEnabled = false
        chartView?.legend?.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT

        xAxis = chartView?.xAxis
        xAxis?.textSize = 10f
        xAxis?.textColor = Color.BLACK

        xAxis?.setDrawAxisLine(true)
        xAxis?.setDrawGridLines(false)

        yAxis = chartView?.axisLeft

        xAxis?.position = XAxis.XAxisPosition.BOTTOM
//        xAxis?.textSize =
    }

    private fun setUpAlcoholListView(){
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        alcoholListRecyclerView?.layoutManager = manager
        adapter = AlcoholListRecyclerAdapter()
        alcoholListRecyclerView?.adapter = adapter

    }

    override fun onLoad(dataSet: LineDataSet, labels: ArrayList<String>) {
        dataSet.setCircleColor(ContextCompat.getColor(context, R.color.colorBlue))
        dataSet.color = ContextCompat.getColor(context, R.color.colorBlue)
        dataSet.circleRadius = 4.3F
        dataSet.lineWidth = 1.5f
        dataSet.valueTextSize = 8F

        chartView?.data = LineData(dataSet)
        xAxis?.valueFormatter = object :IndexAxisValueFormatter(labels){

            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                return labels[Math.round(value)]
            }
        }

        chartView?.animateXY(500, 2000)
        chartView?.invalidate()
    }

    override fun onDataChanged(alcohols: MutableList<Alcohol>?) {
        alcohols?.let { adapter?.setItems(it) }
        adapter?.notifyDataSetChanged()

        loadChart(alcohols)
    }

    private fun loadChart(alcohols: MutableList<Alcohol>?) {
        chartModel?.setDataList(alcohols as ArrayList<Alcohol>)
        chartModel?.loadChart()
    }



}

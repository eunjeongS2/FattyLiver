package kr.ac.ajou.fattyliver.mainTabFragment

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
import android.widget.ImageView
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
import kr.ac.ajou.fattyliver.OnDataChangedListener
import kr.ac.ajou.fattyliver.model.Alcohol
import kr.ac.ajou.fattyliver.model.AlcoholModel
import kr.ac.ajou.fattyliver.model.ChartModel

class MainTabFragment : Fragment(), ChartModel.OnChartLoadListener, OnDataChangedListener, OnChartValueSelectedListener {

    private var idTextView : TextView? = null
    private var calendarImageView : ImageView? = null
    private var chartView : LineChart? = null
    private var xAxis : XAxis? = null
    private var yAxis : YAxis? = null
    private var alcoholListRecyclerView : RecyclerView? = null
    private var adapter : AlcoholListRecyclerAdapter? = null
    private var chartModel : ChartModel? = null
    private var alcoholModel : AlcoholModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view = LayoutInflater.from(context).inflate(R.layout.main_tab_fragment, container, false)
//        idTextView = view.findViewById(R.id.textView_id)
//        calendarImageView = view.findViewById(R.id.imageView_calendar)
//        chartView = view.findViewById(R.id.chart_line)
//        alcoholListRecyclerView = view.findViewById(R.id.recyclerView_main)

        alcoholModel = AlcoholModel()
        alcoholModel?.setOnDataChangedListener(this)
        chartModel = ChartModel()
        chartModel?.setOnChartLoadListener(this)

        initChart()

        setUpAlcoholListView()

        return view
    }

    private fun initChart(){
        chartView?.setOnChartValueSelectedListener(this)
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
    }

    private fun setUpAlcoholListView(){
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        alcoholListRecyclerView?.layoutManager = manager
        adapter = AlcoholListRecyclerAdapter()
        alcoholListRecyclerView?.adapter = adapter

    }

    override fun onLoad(dataSet: LineDataSet, labels: MutableSet<String>) {
//        context?.let { ContextCompat.getColor(it, R.color.colorBlue) }?.let { dataSet.setCircleColor(it) }
//        dataSet.color = context?.let { ContextCompat.getColor(it, R.color.colorBlue) }!!
        dataSet.circleRadius = 4.3F
        dataSet.lineWidth = 1.5f
        dataSet.valueTextSize = 8F

        chartView?.data = LineData(dataSet)
        xAxis?.granularity = 1f
        xAxis?.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis?.labelCount = labels.size

        chartView?.animateXY(500, 2000)
        chartView?.invalidate()
    }

    override fun onDataChanged() {
        val alcohols = alcoholModel?.getMaxAlcohols()
        alcohols?.let { adapter?.setItems(it) }
        adapter?.notifyDataSetChanged()

        loadChart(alcohols)
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
}

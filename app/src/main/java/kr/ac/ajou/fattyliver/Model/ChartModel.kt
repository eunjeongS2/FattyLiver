package kr.ac.ajou.fattyliver.model

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet


class ChartModel {
    private var onChartLoadListener : OnChartLoadListener? = null
    private var pointList : ArrayList<Entry>? = null
    private var dataSet : LineDataSet? = null
    private var labels : MutableSet<String>? = null
    private var dataList: MutableList<Alcohol>? = null
    private var alcohols : MutableMap<String, MutableList<Double>>? = null

    init {
        pointList = arrayListOf()
        labels = mutableSetOf()
        alcohols = mutableMapOf()

    }
    fun setDataList(dataList: MutableList<Alcohol>){
        this.dataList = dataList
        alcohols = mutableMapOf()

        pointList = arrayListOf()
        labels = mutableSetOf()

        var dataCount = 0F
        for (data : Alcohol in dataList){

            pointList?.add(Entry(dataCount, data.value.toFloat()))
            labels?.add(data.timestamp.split("/")[1])
            dataCount++
        }

    }

    interface OnChartLoadListener {
        fun onLoad(dataSet: LineDataSet, labels: MutableSet<String>)
    }

    fun setOnChartLoadListener(onChartLoadListener: OnChartLoadListener) {
        this.onChartLoadListener = onChartLoadListener
    }

    fun loadChart(){
        dataSet = LineDataSet(pointList, "Alcohol")
        labels?.let { labels -> dataSet?.let { dataSet -> onChartLoadListener?.onLoad(dataSet, labels) } }
    }

}




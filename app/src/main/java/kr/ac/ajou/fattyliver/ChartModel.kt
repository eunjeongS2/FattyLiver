package kr.ac.ajou.fattyliver

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet


class ChartModel {
    private var onChartLoadListener : OnChartLoadListener? = null
    private var pointList : ArrayList<Entry>? = null
    private var dataSet : LineDataSet? = null
    private var labels : ArrayList<String>? = null
    private var dataList: ArrayList<Alcohol>? = null

    init {
        pointList = arrayListOf()
        labels = arrayListOf()

    }

    fun setDataList(dataList: ArrayList<Alcohol>){
        this.dataList = dataList
        pointList = arrayListOf()
        labels = arrayListOf()

        var dataCount = 0F
        for (data : Alcohol in dataList){
            pointList?.add(Entry(dataCount, data.value.toFloat()))
            labels?.add(data.date)
            dataCount++
        }

    }

    interface OnChartLoadListener {
        fun onLoad(dataSet: LineDataSet, labels: ArrayList<String>)
    }

    fun setOnChartLoadListener(onChartLoadListener: OnChartLoadListener) {
        this.onChartLoadListener = onChartLoadListener
    }

    fun loadChart(){
        dataSet = LineDataSet(pointList, "Alcohol")
        labels?.let { labels -> dataSet?.let { dataSet -> onChartLoadListener?.onLoad(dataSet, labels) } }
    }


}




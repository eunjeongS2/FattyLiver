package kr.ac.ajou.fattyliver.model

import com.google.firebase.database.*
import kr.ac.ajou.fattyliver.OnDataChangedListener
import java.text.SimpleDateFormat
import java.util.*

class AlcoholModel{
    private var alcohols : MutableMap<String, MutableList<Alcohol>>? = null
    private var onDataChangedListener : OnDataChangedListener? = null

    private var ref : DatabaseReference? = null

    init {
        alcohols = mutableMapOf()
        val database = FirebaseDatabase.getInstance()
        ref = UserModel.instance.user?.uid?.let { database.getReference("user").child(it).child("alcohols") }

        val dateFormat = SimpleDateFormat("yyyy/M-d", Locale.KOREA)
        val compareFormat = SimpleDateFormat("yyyy/M.d", Locale.KOREA)

        this.ref?.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val newAlcohols: MutableMap<String, MutableList<Alcohol>> = mutableMapOf()
                val children: Iterable<DataSnapshot> = p0.children

                if(p0.value != null){
                    val firstDate = Calendar.getInstance()

                    val lastDate = Calendar.getInstance()
                    lastDate.time = Date()

                    firstDate.time = dateFormat.parse("${lastDate.get(Calendar.YEAR)}/${p0.children.first().key}")

                    while (firstDate.compareTo(lastDate) != 1) {
                        val compare = compareFormat.format(Date(firstDate.timeInMillis))

                        newAlcohols.put(compare.split("/")[1], mutableListOf(Alcohol("$compare/오전 00:00", 0.0)))
                        firstDate.add(Calendar.DATE, 1)
                    }
                }
                children
                        .asSequence()
                        .map { it.children }
                        .flatMap { it.asSequence() }
                        .map { it.getValue<Alcohol>(Alcohol::class.java) }
                        .forEach {
                            it?.let {
                                val date = it.timestamp.split('/')[1]
                                val values = newAlcohols[date]
                                values?.add(Alcohol(it.timestamp, it.value))
                            }
                        }
                alcohols = newAlcohols
                onDataChangedListener?.onDataChanged()            }

        })
    }

    fun setOnDataChangedListener(onDataChangedListener: OnDataChangedListener){
        this.onDataChangedListener = onDataChangedListener
    }


    fun addAlcohol(timestamp: String = "", value: Double){
        val saveAlcohol = Alcohol.newAlcohol(timestamp = timestamp, value = value)
        val date = saveAlcohol.timestamp.split('/')[1].split('.')
        val childRef: DatabaseReference? = ref?.child(date[0]+"-"+date[1])?.push()

        childRef?.setValue(saveAlcohol)
    }

    fun getMaxAlcohols() : MutableList<Alcohol>{
        val maxAlcohols = mutableListOf<Alcohol>()

        for (alcohols in alcohols!!){
            alcohols.value.sortBy { it.value }
            val alcohol = alcohols.value[alcohols.value.size-1]
            maxAlcohols.add(alcohol)
        }
        return maxAlcohols
    }

    fun getDateAlcohols(date: String) : MutableList<Alcohol>? {
        return alcohols?.get(date)
    }

}
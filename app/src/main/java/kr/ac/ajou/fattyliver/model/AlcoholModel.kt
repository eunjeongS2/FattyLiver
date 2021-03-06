package kr.ac.ajou.fattyliver.model

import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class AlcoholModel{
    private var alcohols : MutableMap<String, MutableList<Alcohol>>? = null
    var onDataChangedListener : OnDataChangedListener? = null

    private var ref : DatabaseReference? = null

    interface OnDataChangedListener{
        fun onDataChanged()
    }

    init {
        alcohols = mutableMapOf()
        val database = FirebaseDatabase.getInstance()
        ref = UserModel.instance.user?.uid?.let { database.getReference("user").child(it).child("alcohols") }

        val dateFormat = SimpleDateFormat("yyyy/M.d", Locale.KOREA)

        this.ref?.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val newAlcohols: MutableMap<String, MutableList<Alcohol>> = mutableMapOf()
                val children: Iterable<DataSnapshot> = p0.children

                p0.value?.let {
                    val firstDate = Calendar.getInstance()

                    val lastDate = Calendar.getInstance()
                    lastDate.time = Date()

                    val firstDataDate = p0.children.first().children.first().child("timestamp").getValue(String::class.java)?.split('/')
                    firstDate.time = dateFormat.parse("${firstDataDate?.get(0)}/${firstDataDate?.get(1)}}")

                    while (firstDate.compareTo(lastDate) != 1) {
                        val compare = dateFormat.format(Date(firstDate.timeInMillis))

                        newAlcohols.put(compare.split("/")[1], mutableListOf(Alcohol.newAlcohol("$compare/오전 00:00", 0.0)))
                        firstDate.add(Calendar.DATE, 1)
                    }
                }

                children
                        .asSequence()
                        .map { it.children }
                        .flatMap { it.asSequence() }
                        .map {
                            it.getValue<Alcohol>(Alcohol::class.java) }
                        .forEach {
                            it?.let {
                                val date = it.timestamp.split('/')[1]
                                val values = newAlcohols[date]
                                values?.add(Alcohol.newAlcohol(it.timestamp, it.value))
                            }
                        }
                alcohols = newAlcohols
                onDataChangedListener?.onDataChanged()
            }

        })
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
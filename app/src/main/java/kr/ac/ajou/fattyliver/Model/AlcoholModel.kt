package kr.ac.ajou.fattyliver.model

import com.google.firebase.database.*
import kr.ac.ajou.fattyliver.OnDataChangedListener

class AlcoholModel{
    var alcohols : MutableMap<String, MutableList<Alcohol>>? = null
    private var onDataChangedListener : OnDataChangedListener? = null

    private var database : FirebaseDatabase? = null
    private var ref : DatabaseReference? = null

    init {
        alcohols = mutableMapOf()
        database = FirebaseDatabase.getInstance()
        ref = database?.getReference("alcohols")

        this.ref?.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                println(p0?.message)
            }

            override fun onDataChange(p0: DataSnapshot?) {
                val newAlcohols: MutableMap<String, MutableList<Alcohol>> = mutableMapOf()
                val children: Iterable<DataSnapshot> = p0!!.children

                children
                        .asSequence()
                        .map { it.children }
                        .flatMap { it.asSequence() }
                        .map { it.getValue<Alcohol>(Alcohol::class.java) }
                        .forEach {
                            it?.let {
                                val date = it.timestamp.split('/')[0]
                                var values = newAlcohols[date]
                                if (values == null){
                                    newAlcohols.put(date, mutableListOf())
                                    values = newAlcohols[date]
                                }
                                values?.add(Alcohol(it.timestamp, it.value))
                            }
                        }

                alcohols = newAlcohols
                onDataChangedListener?.onDataChanged()
            }

        })
    }

    fun setOnDataChangedListener(onDataChangedListener: OnDataChangedListener){
        this.onDataChangedListener = onDataChangedListener
    }


    fun addAlcohol(value: Double){
        val saveAlcohol = Alcohol.newAlcohol(value)
        val date = saveAlcohol.timestamp.split('/')[0].split('.')
        val childRef: DatabaseReference? = ref?.child(date[0]+date[1])?.push()
        childRef?.setValue(saveAlcohol)
    }

    fun getMaxAlcohols() : MutableList<Alcohol>{
        val maxAlcohols = mutableListOf<Alcohol>()

        for (alcohols in alcohols!!){
            alcohols.value.sortBy { it.value }
            alcohols.value.forEach { println(it.timestamp+it.value) }
            val alcohol = alcohols.value[alcohols.value.size-1]
            maxAlcohols.add(alcohol)
        }
        return maxAlcohols
    }

    fun getDateAlcohols(date: String) : MutableList<Alcohol>? {
        return alcohols?.get(date)
    }

}
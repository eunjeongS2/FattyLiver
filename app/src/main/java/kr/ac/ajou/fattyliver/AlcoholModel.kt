package kr.ac.ajou.fattyliver

import com.google.firebase.database.*

class AlcoholModel{
    private var alcohols : MutableList<Alcohol>? = null
    private var onDataChangedListener : OnDataChangedListener? = null

    private var database : FirebaseDatabase? = null
    private var ref : DatabaseReference? = null

    init {
        alcohols = mutableListOf()
        database = FirebaseDatabase.getInstance()
//        ref = database?.reference
        ref = database?.getReference("alcohols")

        this.ref?.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                println(p0?.message)
            }

            override fun onDataChange(p0: DataSnapshot?) {
                val newAlcohols: MutableList<Alcohol> = mutableListOf()
                val children: Iterable<DataSnapshot> = p0!!.children

                children
                        .map { it.getValue<Alcohol>(Alcohol::class.java) }
                        .forEach { alcohol -> alcohol?.let { newAlcohols.add(it) } }

                alcohols = newAlcohols
                onDataChangedListener?.onDataChanged(alcohols)
            }

        })
    }

    fun setOnDataChangedListener(onDataChangedListener: OnDataChangedListener){
        this.onDataChangedListener = onDataChangedListener
    }

    fun addAlcohol(date: String, value: Double){
        val childRef: DatabaseReference? = ref?.push()
        childRef?.setValue(Alcohol.newAlcohol(date, value))
    }


}

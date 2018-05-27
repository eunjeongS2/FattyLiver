package kr.ac.ajou.fattyliver.model

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.*

class Alcohol(var timestamp: String = "", var value: Double = 0.0) : Parcelable{

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readDouble())


    companion object CREATOR : Parcelable.Creator<Alcohol> {
        override fun createFromParcel(parcel: Parcel): Alcohol {
            return Alcohol(parcel)
        }

        override fun newArray(size: Int): Array<Alcohol?> {
            return arrayOfNulls(size)
        }

        fun newAlcohol(timestamp: String = "",value: Double) : Alcohol {
            return if (timestamp == "")
                Alcohol(timestamp(), value)
            else
                Alcohol(timestamp, value)

        }

        private fun timestamp(): String {
            val date = Date()
            val dateFormat = SimpleDateFormat("yyyy/M.d/a h:mm", Locale.KOREA)
            return dateFormat.format(date)
        }

    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeString(timestamp)
        p0?.writeDouble(value)
    }

    override fun describeContents(): Int {
        return 0
    }

}


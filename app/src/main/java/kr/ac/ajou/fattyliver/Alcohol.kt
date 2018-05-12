package kr.ac.ajou.fattyliver

class Alcohol(var date: String = "", var value: Double = 0.0) {
    companion object {
        fun newAlcohol(date: String, value: Double) : Alcohol = Alcohol(date, value)

    }
}


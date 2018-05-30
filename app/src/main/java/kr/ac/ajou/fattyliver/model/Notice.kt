package kr.ac.ajou.fattyliver.model

class Notice(val notice: String = "", val time: String = ""){
    companion object {
        fun newNotice(notice: String, time: String): Notice{
            return Notice(notice, time)
        }
    }
}

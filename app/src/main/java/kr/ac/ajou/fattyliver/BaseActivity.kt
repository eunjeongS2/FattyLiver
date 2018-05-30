package kr.ac.ajou.fattyliver

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

open class BaseActivity : AppCompatActivity() {

    companion object {
        private var typeface: Typeface? = null
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        if (typeface == null) {
            typeface = Typeface.createFromAsset(this.assets, "DX하얀토끼B.ttf")
        }
        setGlobalFont(window.decorView)
    }

    private fun setGlobalFont(view: View?) {
        if (view != null) {
            if (view is ViewGroup) {
                val viewGroup = view as ViewGroup?
                val vgCnt = viewGroup!!.childCount
                for (i in 0 until vgCnt) {
                    val v = viewGroup.getChildAt(i)
                    if (v is TextView) {
                        v.typeface = typeface
                    }
                    setGlobalFont(v)
                }
            }
        }
    }

}

package kr.ac.ajou.fattyliver

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager

class MainActivity : AppCompatActivity() {

    private var fragment: RootFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragment = RootFragment()
        val fragmentManager: FragmentManager? = supportFragmentManager

        fragmentManager?.beginTransaction()
                ?.add(R.id.container_body, fragment)
                ?.commit()

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}


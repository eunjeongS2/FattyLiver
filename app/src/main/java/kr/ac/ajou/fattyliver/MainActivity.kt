package kr.ac.ajou.fattyliver

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val fragment = MainTabFragment()
        fragmentTransaction.add(R.id.fragment_main, fragment)
        fragmentTransaction.commit()
    }


}

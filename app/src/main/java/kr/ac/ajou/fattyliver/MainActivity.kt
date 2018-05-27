package kr.ac.ajou.fattyliver

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import kr.ac.ajou.fattyliver.alarmFragment.ReleaseAlarmFragment
import kr.ac.ajou.fattyliver.alarmFragment.Tab2Fragment
import kr.ac.ajou.fattyliver.chatFragment.ChatFragment
import kr.ac.ajou.fattyliver.mainTabFragment.MainTabFragment


class MainActivity : AppCompatActivity() {

    private var mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_graph -> {
                replaceFragment(MainTabFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_alarm -> {
                replaceFragment(Tab2Fragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_community -> {
                replaceFragment(ChatFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_alcohol -> {
                replaceFragment(Tab4Fragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_container, fragment).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val notiString = intent.getStringExtra("ReleaseAlarmFragment")
        val password = intent.getStringExtra("password")
        val phone = intent.getStringExtra("phone")

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.main_bottom_navigation)
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        if(notiString != null) {
            if(notiString == "notiIntent") {
                val fragment = ReleaseAlarmFragment()
                val bundle = Bundle()
                bundle.putString("password", password)
                bundle.putString("phone", phone)
                fragment.arguments = bundle
                fragmentTransaction.addToBackStack(null).replace(R.id.main_container, fragment).commit()
            }
        } else {
            fragmentTransaction.add(R.id.main_container, MainTabFragment()).commit()
        }

    }

}


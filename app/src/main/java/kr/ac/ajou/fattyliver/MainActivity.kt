package kr.ac.ajou.fattyliver

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import kr.ac.ajou.fattyliver.alarmFragment.AlarmFragment
import kr.ac.ajou.fattyliver.alarmFragment.ReleaseAlarmFragment
import kr.ac.ajou.fattyliver.chatFragment.ChatListFragment
import kr.ac.ajou.fattyliver.mainTabFragment.MainTabFragment
import kr.ac.ajou.fattyliver.mesurementFragment.MeasureFragment


class MainActivity : AppCompatActivity() {

    private var mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_graph -> {
                replaceFragment(MainTabFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_alarm -> {
                replaceFragment(AlarmFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_community -> {
                replaceFragment(ChatListFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_alcohol -> {
                replaceFragment(MeasureFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        return@OnNavigationItemSelectedListener false
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
//        for (i in 0 until bottomNavigationView.childCount) {
//            val iconView: View = bottomNavigationView.getChildAt(i).findViewById(android.support.design.R.id.icon)
//            val layoutParams = iconView.layoutParams
//            val displayMetrics = resources.displayMetrics
//            layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32f, displayMetrics).toInt()
//            layoutParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32f, displayMetrics).toInt()
//            iconView.layoutParams = layoutParams
//        }

        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        if (notiString != null) {
            if (notiString == "notiIntent") {
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


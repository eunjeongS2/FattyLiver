package kr.ac.ajou.fattyliver

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.MenuItem
import kr.ac.ajou.fattyliver.AlarmFragment.ReleaseAlarmFragment
import kr.ac.ajou.fattyliver.AlarmFragment.Tab2Fragment
import kr.ac.ajou.fattyliver.mainTabFragment.MainTabFragment


class MainActivity : AppCompatActivity() {

    private var mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.nav_graph -> {
                    replaceFragment(MainTabFragment())
                    return true
                }
                R.id.nav_alarm -> {
                    replaceFragment(Tab2Fragment())
                    return true
                }
                R.id.nav_community -> {
                    replaceFragment(Tab3Fragment())
                    return true
                }
                R.id.nav_alcohol -> {
                    replaceFragment(Tab4Fragment())
                    return true
                }
            }
            return false
        }
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
                fragment.setArguments(bundle)
                fragmentTransaction.addToBackStack(null).replace(R.id.main_container, fragment).commit()
            }
        } else {
            fragmentTransaction.add(R.id.main_container, MainTabFragment()).commit()
        }

    }

}


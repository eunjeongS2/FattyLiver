package kr.ac.ajou.fattyliver

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewPager
import android.view.MenuItem
import android.util.TypedValue
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup



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
        fragmentTransaction.add(R.id.main_container, MainTabFragment()).commit()
    }

}


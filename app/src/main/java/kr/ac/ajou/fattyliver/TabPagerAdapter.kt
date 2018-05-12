package kr.ac.ajou.fattyliver

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


class TabPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    private val fragments = ArrayList<FragmentInfo>()

    override fun getItem(position: Int): Fragment {
        return fragments[position].fragment
    }

    override fun getCount(): Int {
        return fragments.size
    }

    fun addFragment(iconResId: Int, title: String, fragment: Fragment) {
        val info = FragmentInfo(iconResId, title, fragment)
        fragments.add(info)
    }

    fun getFragmentInfo(position: Int): FragmentInfo {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragments[position].text
    }

}


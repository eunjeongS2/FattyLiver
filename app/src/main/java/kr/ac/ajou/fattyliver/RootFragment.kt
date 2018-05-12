package kr.ac.ajou.fattyliver


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


open class RootFragment : Fragment() {
//    override fun onBackPressed(): Boolean {
//        return BackPressImpl(this).onBackPressed()
//    }

    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_root, container, false)

        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)

        val pagerAdapter = TabPagerAdapter(fragmentManager)
        pagerAdapter.addFragment(R.drawable.timeline, "통계", MainTabFragment())
        pagerAdapter.addFragment(R.drawable.alarm,"알람", Tab2Fragment())
        pagerAdapter.addFragment(R.drawable.face,"내기", Tab3Fragment())
        pagerAdapter.addFragment(R.drawable.alcohol,"측정", Tab4Fragment())

        viewPager?.adapter = pagerAdapter
        tabLayout?.setupWithViewPager(viewPager)

        for(i in 0 until (viewPager?.adapter as TabPagerAdapter).count) {
            tabLayout?.getTabAt(i)!!.setIcon(pagerAdapter.getFragmentInfo(i).iconResId)
        }

        return view
    }


}

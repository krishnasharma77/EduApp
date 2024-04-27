package com.adts.app.activity

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.adts.app.R
import com.adts.app.databinding.ActivityAllResourcesBinding
import com.adts.app.fragment.Artical
import com.adts.app.fragment.Videos
import com.google.android.exoplayer2.util.Log
import com.google.android.material.tabs.TabLayout

class AllResources : AppCompatActivity() {
    private lateinit var binding: ActivityAllResourcesBinding
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllResourcesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewPager = findViewById(R.id.viewPager)
        setupViewPager(viewPager!!)
       binding.resourseTab.setupWithViewPager(viewPager)

        binding.closeBtn.setOnClickListener {
            onBackPressed()
        }
    }


    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(Videos(), "Videos")
        adapter.addFragment(Artical(), "Article")
        viewPager.adapter = adapter
    }


    class ViewPagerAdapter(supportFragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(supportFragmentManager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()


        override fun getItem(position: Int): Fragment {
            // return a particular fragment page
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            // return the number of tabs
            return mFragmentList.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)
            Log.d("Hellllllllllllll", "hello")
//            Toast.makeText(mContext, "hello", Toast.LENGTH_LONG).show()
        }



        fun addFragment(fragment: Fragment, title: String) {
            // add each fragment and its title to the array list
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence{
            // return title of the tab
            return mFragmentTitleList[position]
        }
    }
}




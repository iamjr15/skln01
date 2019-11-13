package com.netzwelt.tutormodule.ui.dashboard

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.autohub.skln.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.netzwelt.tutormodule.R
import com.netzwelt.tutormodule.databinding.ActivityDashboardBinding
import com.netzwelt.tutormodule.ui.dashboard.home.HomeFragment
import com.netzwelt.tutormodule.ui.dashboard.profile.ProfileFragment
import com.netzwelt.tutormodule.ui.dashboard.requests.RequestsFragment
import com.netzwelt.tutormodule.ui.dashboard.schedule.ScheduleFragment

class DashboardActivity : BaseActivity() {

    lateinit var mBinding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        mBinding.navigationView.setOnNavigationItemSelectedListener(onItemSelectedListener)

        mBinding.navigationView.selectedItemId = R.id.navigation_home
    }

    private val onItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val homeFragment = HomeFragment.newInstance()
                openFragment(homeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_schedule -> {

                val scheduleFragment = ScheduleFragment.newInstance()
                openFragment(scheduleFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_requests -> {
                val requestsFragment = RequestsFragment.newInstance()
                openFragment(requestsFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                val profileFragment = ProfileFragment.newInstance()
                openFragment(profileFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}

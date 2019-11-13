package com.netzwelt.tutormodule.ui.dashboard

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.autohub.skln.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.netzwelt.tutormodule.R
import com.netzwelt.tutormodule.databinding.ActivityDashboardBinding

class DashboardActivity : BaseActivity() {

    lateinit var mBinding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        mBinding.navigationView.setOnNavigationItemSelectedListener(onItemSelectedListener)

    }
    private val onItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_schedule -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_requests -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}

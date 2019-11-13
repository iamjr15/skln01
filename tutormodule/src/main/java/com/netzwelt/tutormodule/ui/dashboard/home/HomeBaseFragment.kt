package com.netzwelt.tutormodule.ui.dashboard.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.netzwelt.tutormodule.R
import com.netzwelt.tutormodule.ui.dashboard.classmanager.ClassManagerFragment
import com.netzwelt.tutormodule.ui.dashboard.requests.RequestsFragment

/**
 * A simple [Fragment] subclass.
 */
class HomeBaseFragment : Fragment() {

    internal var view: View? = null
    var homeFragment: HomeFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_base, container, false)
        showHomefragment()
        return view

    }


    fun showHomefragment() {
        homeFragment = HomeFragment()
        childFragmentManager
                .beginTransaction()
                .replace(R.id.homecontainer, homeFragment!!).commit()
    }

    fun showManagerFragment() {
        homeFragment = null
        childFragmentManager
                .beginTransaction()
                .replace(R.id.homecontainer, ClassManagerFragment()).commit()
    }


}

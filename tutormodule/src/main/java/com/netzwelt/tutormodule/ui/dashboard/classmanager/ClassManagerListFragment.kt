package com.netzwelt.tutormodule.ui.dashboard.classmanager


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.netzwelt.tutormodule.R

/**
 * A simple [Fragment] subclass.
 */
class ClassManagerListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_manager_list, container, false)
    }


}

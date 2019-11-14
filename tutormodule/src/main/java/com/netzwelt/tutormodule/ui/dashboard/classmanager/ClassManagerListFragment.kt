package com.netzwelt.tutormodule.ui.dashboard.classmanager


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.netzwelt.tutormodule.R
import com.netzwelt.tutormodule.databinding.FragmentClassManagerListBinding

/**
 * A simple [Fragment] subclass.
 */

class ClassManagerListFragment : Fragment() {
    private var mBinding: FragmentClassManagerListBinding? = null
    private var adaptor: ClassesAdaptor? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_manager_list, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentClassManagerListBinding.bind(view)
        mBinding!!.classesrecycleview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adaptor = ClassesAdaptor(requireContext())
        mBinding!!.classesrecycleview.adapter = adaptor


    }

}

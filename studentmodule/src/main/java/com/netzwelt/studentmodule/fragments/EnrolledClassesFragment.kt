package com.netzwelt.studentmodule.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.autohub.skln.listeners.ItemClickListener

import com.netzwelt.studentmodule.R
import com.netzwelt.studentmodule.adaptors.EnrolledClassesAdaptor
import com.netzwelt.studentmodule.databinding.FragmentEnrolledClassesBinding

/**
 * A simple [Fragment] subclass.
 */
class EnrolledClassesFragment : Fragment() {
    private var mBinding: FragmentEnrolledClassesBinding? = null

    private val tutorsClickListener = ItemClickListener<String> {

    }
    private var adaptor: EnrolledClassesAdaptor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enrolled_classes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentEnrolledClassesBinding.bind(view)

        mBinding!!.enrolledrecycleview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adaptor = EnrolledClassesAdaptor(requireContext(), tutorsClickListener)
        mBinding!!.enrolledrecycleview.adapter = adaptor


    }

}

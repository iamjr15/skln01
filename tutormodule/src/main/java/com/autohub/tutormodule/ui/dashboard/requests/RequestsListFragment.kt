package com.autohub.tutormodule.ui.dashboard.requests


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.autohub.skln.fragment.BaseFragment
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.FragmentRequestsListBinding

/**
 * A simple [Fragment] subclass.
 */
class RequestsListFragment : BaseFragment() {

    private lateinit var mAdapter: RequestsAdaptor


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requests_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentRequestsListBinding.bind(view)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setEmptyView(binding.rrempty)
        mAdapter = RequestsAdaptor(requireContext())
        binding.recyclerView.adapter = mAdapter
    }

}

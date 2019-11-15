package com.autohub.tutormodule.ui.dashboard.requests


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.autohub.skln.fragment.BaseFragment
import com.autohub.tutormodule.R

/**
 * A simple [Fragment] subclass.
 */
class PendingRequestFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending_request, container, false)
    }


}

package com.netzwelt.tutormodule.ui.dashboard.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autohub.skln.fragment.BaseFragment
import com.netzwelt.tutormodule.R

class RequestsFragment : BaseFragment() {
    internal var view: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_pending_request, container, false)

    companion object {
        fun newInstance(): RequestsFragment = RequestsFragment()
    }
}
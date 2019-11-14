package com.netzwelt.tutormodule.ui.dashboard.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.autohub.skln.fragment.BaseFragment
import com.netzwelt.tutormodule.R


class ClassRequestBaseFragment : BaseFragment() {
    internal var view: View? = null
    var fragmentRequests: RequestsFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_class_request_base, container, false)
        showRequestFragmentClass()
        return view
    }

    private fun showRequestFragmentClass() {
        fragmentRequests = RequestsFragment()
        childFragmentManager
                .beginTransaction()
                .replace(R.id.requestcontainer, fragmentRequests!!).commit()
    }


    fun showRequestDetailFragment(bundle: Bundle) {
        /*fragmentClassRequests = null

        val fragment = FragmentRequestDetail()
        fragment.arguments = bundle

        childFragmentManager
                .beginTransaction()
                .replace(R.id.requestcontainer, fragment).commit()*/

    }
}





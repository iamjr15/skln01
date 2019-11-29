package com.autohub.tutormodule.ui.dashboard.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autohub.skln.fragment.BaseFragment
import com.autohub.tutormodule.R


class RequestBaseFragment : BaseFragment() {
    internal var view: View? = null
    var fragmentRequests: RequestsFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_class_request_base, container, false)
        showRequestFragmentClass()
        return view
    }

    fun showRequestFragmentClass() {
        fragmentRequests = RequestsFragment()
        childFragmentManager
                .beginTransaction().addToBackStack(RequestsFragment::class.java.name)
                .replace(R.id.request_container, fragmentRequests!!).commit()
    }

    fun showPendingRequestScreen(studentId: String, documentId: String) {
        fragmentRequests = null
        val bundle = Bundle()
        bundle.putString("studentId", studentId)
        bundle.putString("documentId", documentId)
        val pendingRequestFragment = PendingRequestFragment()
        pendingRequestFragment.arguments = bundle
        childFragmentManager
                .beginTransaction().addToBackStack(PendingRequestFragment::class.java.name)
                .replace(R.id.request_container, pendingRequestFragment).commit()
    }

    fun backPressed() {
        if (childFragmentManager.backStackEntryCount > 1) {
            childFragmentManager.popBackStack()

        } else {
            activity?.finish()
        }
    }
}





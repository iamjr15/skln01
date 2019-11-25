package com.autohub.studentmodule.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.autohub.studentmodule.R

/**
 * Created by Vt Netzwelt
 */
class MyRequestBaseFragment : Fragment() {
    internal var view: View? = null
    var fragmentClassRequests: FragmentClassRequests? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fragment_my_request_base, container, false)
        showRequestFragmentClass()
        return view
    }

    fun showRequestFragmentClass() {
        fragmentClassRequests = FragmentClassRequests()
        childFragmentManager
                .beginTransaction()
                .replace(R.id.explorecontainer, fragmentClassRequests!!).commit()
    }


    fun showRequestDetailFragment(bundle: Bundle) {
        fragmentClassRequests = null
        bundle.putBoolean("isMyRequest", true)

        val fragment = FragmentRequestDetail()
        fragment.arguments = bundle

        childFragmentManager
                .beginTransaction()
                .replace(R.id.explorecontainer, fragment).commit()


    }
}

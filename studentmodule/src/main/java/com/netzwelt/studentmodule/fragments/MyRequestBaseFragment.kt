package com.netzwelt.studentmodule.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.netzwelt.studentmodule.R


class MyRequestBaseFragment : Fragment() {
    internal var view: View? = null
    public var fragmentClassRequests: FragmentClassRequests? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_request_base, container, false)
        showRequestFragmentClass()
        return view
    }

    public fun showRequestFragmentClass() {
        fragmentClassRequests = FragmentClassRequests()


        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.explorecontainer, fragmentClassRequests!!).commit()
    }


    public fun showRequestDetailFragment(bundle: Bundle) {
        fragmentClassRequests = null

        var fragment = FragmentRequestDetail()
        fragment.arguments = bundle

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.explorecontainer, fragment).commit()


        /*    getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.explorecontainer, exploreTutorsFragment!!).commit()*/
    }
}

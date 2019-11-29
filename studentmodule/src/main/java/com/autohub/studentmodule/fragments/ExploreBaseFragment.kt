package com.autohub.studentmodule.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


/**
 * Created by Vt Netzwelt
 */
class ExploreBaseFragment : Fragment() {

    internal var view: View? = null
    var exploreTutorsFragment: ExploreTutorsFragment? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        view = inflater.inflate(com.autohub.studentmodule.R.layout.fragment_explore_base, container, false)

        return view
    }
/*
* Navigate user to Explore Screen
* */

    fun showExploreFragment(subjectName: String = "") {
        exploreTutorsFragment = ExploreTutorsFragment()

        if (subjectName != "") {
            val bundle = Bundle()
            bundle.putString("data_key", subjectName)
            exploreTutorsFragment!!.arguments = bundle

        }

        childFragmentManager
                .beginTransaction()
                .replace(com.autohub.studentmodule.R.id.explorecontainer, exploreTutorsFragment!!).commit()
    }

    /*
    *Navigate user to Request Detail Screen
    * */
    fun showRequestDetailFragment(bundle: Bundle) {
        exploreTutorsFragment = null
        bundle.putBoolean("isMyRequest", false)
        val fragment = FragmentRequestDetail()
        fragment.arguments = bundle

        childFragmentManager
                .beginTransaction()
                .replace(com.autohub.studentmodule.R.id.explorecontainer, fragment).commit()


    }


}

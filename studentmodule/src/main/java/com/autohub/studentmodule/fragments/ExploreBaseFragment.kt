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
class ExploreBaseFragment : Fragment() {

    internal var view: View? = null
    var exploreTutorsFragment: ExploreTutorsFragment? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        view = inflater.inflate(R.layout.fragment_explore_base, container, false)
        showExploreFragment()

        return view
    }

    fun showExploreFragment() {

        /*  exploreTutorsFragment = ExploreTutorsFragment()


          childFragmentManager
                  .beginTransaction()
                  .replace(R.id.explorecontainer, exploreTutorsFragment!!).commit()*/
    }

    fun showRequestDetailFragment(bundle: Bundle) {
        exploreTutorsFragment = null

        val fragment = FragmentRequestDetail()
        fragment.arguments = bundle

        childFragmentManager
                .beginTransaction()
                .replace(R.id.explorecontainer, fragment).commit()


        /*    getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.explorecontainer, exploreTutorsFragment!!).commit()*/
    }


}

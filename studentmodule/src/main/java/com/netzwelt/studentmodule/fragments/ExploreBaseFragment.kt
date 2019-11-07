package com.netzwelt.studentmodule.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.netzwelt.studentmodule.R

/**
 * A simple [Fragment] subclass.
 */
class ExploreBaseFragment : Fragment() {

    internal var view: View? = null
    public var exploreTutorsFragment: ExploreTutorsFragment? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        view = inflater.inflate(R.layout.fragment_explore_base, container, false)
        showExploreFragment()

        return view
    }

    public fun showExploreFragment() {

        exploreTutorsFragment = ExploreTutorsFragment()


        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.explorecontainer, exploreTutorsFragment!!).commit()
    }

    public fun showRequestDetailFragment(bundle: Bundle) {
        exploreTutorsFragment = null

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

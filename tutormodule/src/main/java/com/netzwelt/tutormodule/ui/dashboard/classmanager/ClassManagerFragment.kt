package com.netzwelt.tutormodule.ui.dashboard.classmanager


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.autohub.skln.utills.ViewPagerAdapter
import com.netzwelt.tutormodule.R
import com.netzwelt.tutormodule.databinding.FragmentClassManagerBinding
import com.netzwelt.tutormodule.ui.manageClasses.AddBatchFragment

/**
 * A simple [Fragment] subclass.
 */
class ClassManagerFragment : Fragment() {

    private lateinit var mBinding: FragmentClassManagerBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_manager, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding = FragmentClassManagerBinding.bind(view)
        mBinding.callback = this

        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addData(getFragmentClassRequests(ClassManagerListFragment()), "Classes Today")
        adapter.addData(getFragmentClassRequests(ClassManagerListFragment()), "All classes")

        mBinding.tabs.setupWithViewPager(mBinding.viewpager)
        mBinding.viewpager.adapter = adapter
    }

    private fun getFragmentClassRequests(fragment: Fragment/*, user: User?*/): Fragment {
        return fragment
    }

    fun openAddBatchScreen() {
        val bundle = Bundle()
        bundle.putBoolean("type", true)
        val addBatchFragment = AddBatchFragment()
        addBatchFragment.arguments = bundle
        childFragmentManager
                .beginTransaction()
                .replace(R.id.classManagerContainer, addBatchFragment).commit()
    }

}

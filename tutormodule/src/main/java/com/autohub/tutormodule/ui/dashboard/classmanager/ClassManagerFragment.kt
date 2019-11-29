package com.autohub.tutormodule.ui.dashboard.classmanager


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.models.batches.BatchesModel
import com.autohub.skln.utills.ViewPagerAdapter
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.FragmentClassManagerBinding
import com.autohub.tutormodule.ui.dashboard.listener.HomeListener

/**
 * A simple [Fragment] subclass.
 */
class ClassManagerFragment : BaseFragment() {

    private lateinit var mBinding: FragmentClassManagerBinding
    private lateinit var homeListener: HomeListener


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_class_manager, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeListener = context as HomeListener

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding = FragmentClassManagerBinding.bind(view)
        mBinding.callback = this

        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addData(getFragmentClassRequests(ClassManagerListFragment("Today")), "Classes Today")
        adapter.addData(getFragmentClassRequests(ClassManagerListFragment("All")), "All classes")

        mBinding.tabs.setupWithViewPager(mBinding.viewpager)
        mBinding.viewpager.adapter = adapter
    }


    private fun getFragmentClassRequests(fragment: Fragment/*, user: User?*/): Fragment {
        return fragment
    }

    fun openAddBatchScreen() {
        homeListener.showAddBatchFragment(true, BatchesModel())
    }

}

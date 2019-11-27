package com.autohub.tutormodule.ui.dashboard.home


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.models.batches.BatchesModel
import com.autohub.tutormodule.R
import com.autohub.tutormodule.ui.dashboard.classmanager.ClassManagerFragment
import com.autohub.tutormodule.ui.dashboard.classmanager.StudentsListFragment
import com.autohub.tutormodule.ui.dashboard.listener.HomeListener
import com.autohub.tutormodule.ui.manageClasses.AddBatchFragment
import com.autohub.tutormodule.ui.manageClasses.BatchOptionsFragment


/**
 * A simple [Fragment] subclass.
 */
class HomeBaseFragment : BaseFragment() {

    internal var view: View? = null
    var homeFragment: HomeFragment? = null
    private lateinit var homeListener: HomeListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_base, container, false)
        showHomeFragment()
        return view

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeListener = context as HomeListener
    }

    /*Open home screen
    * */
    fun showHomeFragment() {
        homeFragment = HomeFragment()
        childFragmentManager
                .beginTransaction().addToBackStack(HomeFragment::class.java.name)
                .replace(R.id.homecontainer, homeFragment!!).commit()
    }

    /*Open Class Manager screen
   * */
    fun showManagerFragment() {
        homeFragment = null
        childFragmentManager
                .beginTransaction().addToBackStack(ClassManagerFragment::class.java.name)
                .replace(R.id.homecontainer, ClassManagerFragment()).commit()
    }

    /*Open Add Batch screen
       * */
    fun showAddBatchFragment(showAddBatch: Boolean, batch: BatchesModel) {
        val bundle = Bundle()
        bundle.putBoolean("showAddBatch", showAddBatch)
        bundle.putParcelable("batch", batch)
        val addBatchFragment = AddBatchFragment()
        addBatchFragment.arguments = bundle
        childFragmentManager
                .beginTransaction().addToBackStack(AddBatchFragment::class.java.name)
                .replace(R.id.homecontainer, addBatchFragment).commit()
    }

    /*Open Batch options screen
          * */
    fun showBatchOptionsFragment(batch: BatchesModel) {
        val batchOptionsFragment = BatchOptionsFragment()
        val bundle = Bundle()
        bundle.putParcelable("batch", batch)
        batchOptionsFragment.arguments = bundle
        childFragmentManager
                .beginTransaction().addToBackStack(BatchOptionsFragment::class.java.name)
                .replace(R.id.homecontainer, batchOptionsFragment).commit()
    }

    /*Open screen showing students list
          * */
    fun showStudentsListFragment() {
        val studentsListFragment = StudentsListFragment()
        childFragmentManager
                .beginTransaction().addToBackStack(StudentsListFragment::class.java.name)
                .replace(R.id.homecontainer, studentsListFragment).commit()
    }

    /*Called on back button pressed.Check number of fragments in stack and perform action.
    * */
    fun backPressed() {
         if (childFragmentManager.backStackEntryCount > 1) {
            childFragmentManager.popBackStack()

        } else {
            activity?.finish()
        }
    }

}

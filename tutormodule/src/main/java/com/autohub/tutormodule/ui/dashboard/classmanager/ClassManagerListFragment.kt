package com.autohub.tutormodule.ui.dashboard.classmanager


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.models.batches.BatchesModel
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.FragmentClassManagerListBinding

/**
 * A simple [Fragment] subclass.
 */

class ClassManagerListFragment : BaseFragment() {
    private var mBinding: FragmentClassManagerListBinding? = null
    private var adaptor: ClassesAdaptor? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_manager_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentClassManagerListBinding.bind(view)
        mBinding!!.classesrecycleview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adaptor = ClassesAdaptor(requireContext())
        mBinding!!.classesrecycleview.adapter = adaptor
        fetchBatches()

    }

    private fun fetchBatches() {
        firebaseStore.collection(getString(R.string.db_root_batches))
                .get().addOnSuccessListener { documentSnapshot ->
                    val data = documentSnapshot.toObjects(BatchesModel::class.java)
                    adaptor?.setData(data)
                }
                .addOnFailureListener { e ->
                    showSnackError(e.message)
                }
    }

}

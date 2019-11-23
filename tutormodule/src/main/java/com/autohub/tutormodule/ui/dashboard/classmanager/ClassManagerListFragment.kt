package com.autohub.tutormodule.ui.dashboard.classmanager


import android.content.Context
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
import com.autohub.tutormodule.ui.dashboard.listner.HomeListener

/**
 * A simple [Fragment] subclass.
 */

class ClassManagerListFragment : BaseFragment(), Listener {
    private var mBinding: FragmentClassManagerListBinding? = null
    private var adaptor: ClassesAdaptor? = null
    private lateinit var homeListener: HomeListener
    private lateinit var recyclerViewData: MutableList<BatchesModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_manager_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentClassManagerListBinding.bind(view)
        mBinding!!.classesrecycleview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adaptor = ClassesAdaptor(requireContext(), this)
        mBinding!!.classesrecycleview.adapter = adaptor
        fetchBatches()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeListener = context as HomeListener
    }

    private fun fetchBatches() {
        firebaseStore.collection(getString(R.string.db_root_batches))
                .get().addOnSuccessListener { documentSnapshot ->
                    recyclerViewData = ArrayList()
                    recyclerViewData = documentSnapshot.toObjects(BatchesModel::class.java)
                    for (i in 0 until recyclerViewData.size) {
                        recyclerViewData[i].documentId = documentSnapshot.documents[i].id
                    }
                    adaptor?.setData(recyclerViewData)
                }
                .addOnFailureListener { e ->
                    showSnackError(e.message)
                }
    }

    override fun openEditSchedule(batch: BatchesModel) {
        homeListener.showBatchOptionsFragment(batch)
    }

    override fun updateStatusOfBatches(status: String, batchesModel: BatchesModel, position: Int) {
        showLoading()
        firebaseStore.collection(getString(R.string.db_root_batches)).document(batchesModel.documentId!!)
                .update("status", status).addOnSuccessListener { documentSnapshot ->
                    hideLoading()
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }
    }

}

package com.autohub.tutormodule.ui.dashboard.requests


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.models.batchRequests.BatchRequestData
import com.autohub.skln.models.tutor.TutorData
import com.autohub.skln.utills.AppConstants
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.FragmentRequestsListBinding
import com.autohub.tutormodule.ui.dashboard.listner.ClassRequestListener

/**
 * A simple [Fragment] subclass.
 */
class RequestsListFragment : BaseFragment(), Listener {
    private lateinit var mAdapter: RequestsAdaptor
    private lateinit var listener: ClassRequestListener
    private lateinit var binding: FragmentRequestsListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requests_list, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as ClassRequestListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRequestsListBinding.bind(view)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setEmptyView(binding.emptyView)
        mAdapter = RequestsAdaptor(requireContext(), this)

        binding.swipeRefresh.setOnRefreshListener {
            fillData( true)
        }
        binding.recyclerView.adapter = mAdapter
        fillData(false)

    }

    /*Fetch batch data on the basis of status of status of requests
    * */
    private fun fillData(isRefresh: Boolean) {

        if (!arguments?.isEmpty!!) {
            if (arguments?.getString(AppConstants.KEY_TYPE) == "Latest") {
                fetchData(AppConstants.STATUS_PENDING,isRefresh)
            } else {
                fetchData("",isRefresh)
            }
        }
    }

    private fun fetchData(status: String, isRefresh: Boolean) {

        firebaseStore.collection(getString(R.string.db_root_tutors)).document(appPreferenceHelper.getuserID())
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if(isRefresh){
                        binding.swipeRefresh.isRefreshing = false
                    }
                    val tutorData = documentSnapshot.toObject(TutorData::class.java)!!
                    fetchDetails(tutorData.id, status, documentSnapshot.id)

                }
                .addOnFailureListener { e ->
                    if(isRefresh){
                        binding.swipeRefresh.isRefreshing = false
                    }
                    showSnackError(e.message)
                }

    }

    private fun fetchDetails(id: String?, status: String, documentId: String) {
        firebaseStore.collection(getString(R.string.db_root_batch_requests)).whereEqualTo("teacher.id", id).get()
                .addOnSuccessListener { documentSnapshot ->
                    val batchRequestData = documentSnapshot.toObjects(BatchRequestData::class.java)
                    for (i in 0 until documentSnapshot.size()) {
                        batchRequestData[i].documentId = documentSnapshot.documents[i].id
                    }

                    if (status == AppConstants.STATUS_PENDING) {

                        mAdapter.setData(batchRequestData.filter {
                            it.status.equals(AppConstants.STATUS_PENDING)
                        })
                    } else {
                        mAdapter.setData(batchRequestData)
                    }
                    if (mAdapter.itemCount > 0) {
                        binding.divider.visibility = View.VISIBLE
                    } else {
                        binding.divider.visibility = View.GONE
                    }

                }
                .addOnFailureListener { e ->
                    showSnackError(e.message)
                }
    }

    override fun showPendingRequestFragment(studentId: String, documentId: String) {
        listener.showPendingRequestScreen(studentId, documentId)
    }
}

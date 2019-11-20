package com.autohub.studentmodule.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.listeners.ItemClickListener
import com.autohub.skln.models.Request
import com.autohub.skln.models.UserModel
import com.autohub.skln.models.batches.BatchRequestModel
import com.autohub.skln.models.batches.BatchRequestViewModel
import com.autohub.skln.utills.AppConstants
import com.autohub.studentmodule.adaptors.RequestAdapter
import com.autohub.studentmodule.databinding.FragmentStudentRequestsBinding
import com.autohub.studentmodule.listners.HomeListners
import java.util.*


/**
 * Created by Vt Netzwelt
 */

class FragmentRequests : BaseFragment() {
    private var mType: String? = null
    private var mUserType: String? = null
    private var mUser: UserModel? = null
    private var mAdapter: RequestAdapter? = null
    private lateinit var homeListner: HomeListners
    private var binding: FragmentStudentRequestsBinding? = null


    private val mItemClickListener = ItemClickListener<BatchRequestViewModel> { requestViewModel ->
        if (!requestViewModel.batchRequestModel!!.status.equals(Request.STATUS.CANCELED.value)) {
            homeListner.onClassRequestSelectListner(requestViewModel = requestViewModel)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeListner = context as HomeListners
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUserType = arguments!!.getString("_user_type", "Student")
        mType = arguments!!.getString(AppConstants.KEY_TYPE, "Latest")
        mUser = arguments!!.getParcelable(AppConstants.KEY_DATA)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.autohub.studentmodule.R.layout.fragment_student_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStudentRequestsBinding.bind(view)
        binding!!.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding!!.recyclerView.setEmptyView(binding!!.rrempty)
        mAdapter = RequestAdapter(requireContext(), mItemClickListener)
        binding!!.recyclerView.adapter = mAdapter
        getRequests()

        binding!!.swiperefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            getRequests()
        })

    }

    private fun getRequests() {
        if (mUser == null || mUser!!.id == null) {
            Log.e(">>>>Nulll", (mUser == null).toString() + "")
            binding!!.swiperefresh.isRefreshing = false

            return
        }
        val dbRoot = getString(com.autohub.studentmodule.R.string.db_root_batchRequests)
        var query = firebaseStore.collection(dbRoot).whereEqualTo("student.id", mUser!!.id)

        if (mType!!.equals("Latest", ignoreCase = true)) {
            query = query.whereEqualTo("status", Request.STATUS.PENDING.value)
        }
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val requests = ArrayList<BatchRequestViewModel>()
                for (document in task.result!!) {
                    val request = document.toObject(BatchRequestModel::class.java)
                    requests.add(BatchRequestViewModel(request, mUserType, document.id))
                    Log.d(">>>Explore", "Data Is " + request.subject)
                }
                mAdapter!!.setData(requests)
            } else {
                Log.d(">>>Explore", "Error getting documents: ", task.exception)
            }

            binding!!.swiperefresh.isRefreshing = false


        }.addOnFailureListener { e ->
            binding!!.swiperefresh.isRefreshing = false

            Log.e(">>>Explore", "Error getting documents: ", e)
        }
    }
}

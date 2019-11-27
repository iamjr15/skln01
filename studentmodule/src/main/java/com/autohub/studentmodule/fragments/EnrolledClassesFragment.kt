package com.autohub.studentmodule.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.listeners.ItemClickListener
import com.autohub.studentmodule.adaptors.EnrolledClassesAdaptor
import com.autohub.studentmodule.databinding.FragmentEnrolledClassesBinding
import com.autohub.studentmodule.listners.HomeListners
import com.autohub.studentmodule.models.BatchesModel
import com.autohub.studentmodule.utils.AppUtil.uTCToLocal
import com.google.firebase.firestore.SetOptions


/**
 * Created by Vt Netzwelt
 */
class EnrolledClassesFragment : BaseFragment() {
    private var mBinding: FragmentEnrolledClassesBinding? = null

    private lateinit var enrolledClassesList: MutableList<BatchesModel>
    private lateinit var homeListner: HomeListners

    private val erolldClassDeleteClickListener = ItemClickListener<BatchesModel> {

        deleteBatch(it)

    }


    /*
    * Delete the student from perticular Batch
    * */
    private fun deleteBatch(it: BatchesModel?) {
        removeBatchFromProfile(it)
    }


    /*
   * Delete the Batch code from Student profile
   * */
    private fun removeBatchFromProfile(batchmodel: BatchesModel?) {
        showLoading()


        firebaseStore.collection("students").document(appPreferenceHelper.getuserID()).get().addOnSuccessListener {
            hideLoading()
            if (it["batchCodes"] != null) {
                val batches: ArrayList<String> = it["batchCodes"] as ArrayList<String>
                if (batches.contains(batchmodel!!.batchCode)) {
                    batches.removeAt(batches.indexOf(batchmodel.batchCode))
                    firebaseStore.collection("students").document(appPreferenceHelper.getuserID()).set(
                            mapOf("batchCodes" to batches), SetOptions.merge()
                    ).addOnCompleteListener {
                        removeUserIdFromBatch(batchmodel)
                    }
                }


            }
            // user!!.batchCodes


        }.addOnFailureListener {

            hideLoading()
        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeListner = context as HomeListners


    }

    /*
    * Delete the studentId from enrolledStudentsId of perticular Batch
    * */
    private fun removeUserIdFromBatch(batchmodel: BatchesModel) {


        firebaseStore.collection("batches").whereEqualTo("batchCode", batchmodel.batchCode)
                .get().addOnSuccessListener {

                    it.forEach {
                        if (it["enrolledStudentsId"] != null) {
                            val enrollstudentIds: ArrayList<String> = it["enrolledStudentsId"] as ArrayList<String>
                            if (enrollstudentIds.contains(firebaseAuth.currentUser!!.uid)) {
                                enrollstudentIds.removeAt(enrollstudentIds.indexOf(firebaseAuth.currentUser!!.uid))
                                firebaseStore.collection("batches").document(batchmodel.documentId!!).set(
                                        mapOf("enrolledStudentsId" to enrollstudentIds), SetOptions.merge()
                                ).addOnCompleteListener {
                                    fetchEnrolledClasses()
                                    homeListner.updateScheduleFragment()

                                }


                            }

                        }
                    }


                }
    }


    private var adaptor: EnrolledClassesAdaptor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.autohub.studentmodule.R.layout.fragment_enrolled_classes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentEnrolledClassesBinding.bind(view)
        mBinding!!.enrolledrecycleview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adaptor = EnrolledClassesAdaptor(requireContext(), erolldClassDeleteClickListener)
        mBinding!!.enrolledrecycleview.adapter = adaptor
        enrolledClassesList = ArrayList()
        fetchEnrolledClasses()


        mBinding!!.swiperefresh.setOnRefreshListener {
            fetchEnrolledClasses()
        }

    }

    /*
    * Fetch all Batches in which user is enrolled
    *
    *
    * */
    private fun fetchEnrolledClasses() {

        firebaseStore.collection("students").document(appPreferenceHelper.getuserID()).get().addOnSuccessListener {
            hideLoading()
            if (it["batchCodes"] != null && (it["batchCodes"] as ArrayList<String>).size > 0) {
                val userBatchesCode: ArrayList<String> = it["batchCodes"] as ArrayList<String>

                firebaseStore.collection("batches").whereArrayContains("enrolledStudentsId", firebaseAuth.currentUser!!.uid)
                        .get().addOnCompleteListener { task ->

                            mBinding!!.swiperefresh.isRefreshing = false

                            if (task.isSuccessful) {
                                enrolledClassesList.clear()
                                for (document in task.result!!) {
                                    val batchesModel = document.toObject(BatchesModel::class.java)
                                    batchesModel.documentId = document.id



                                    try {
                                        val endTime = uTCToLocal("EEE MMM dd HH:mm:ss z yyyy",
                                                "EEE, d MMM yyyy HH:mm:ss z", batchesModel.timing!!.endTime!!.toDate().toString()
                                        )
                                        val startTime = uTCToLocal("EEE MMM dd HH:mm:ss z yyyy",
                                                "EEE, d MMM yyyy HH:mm:ss z", batchesModel.timing!!.startTime!!.toDate().toString()
                                        ).toString()
                                        batchesModel.batchTiming =
                                                "$startTime - $endTime"
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }

                                    if (userBatchesCode.contains(batchesModel.batchCode)) {
                                        enrolledClassesList.add(batchesModel)
                                    }
                                }
                                updateEmptyview()
                                adaptor!!.setData(enrolledClassesList)

                            }
                        }.addOnFailureListener {
                            updateEmptyview()

                            mBinding!!.swiperefresh.isRefreshing = false

                        }

            } else {
                enrolledClassesList.clear()
                adaptor!!.setData(enrolledClassesList)
                mBinding!!.swiperefresh.isRefreshing = false
                updateEmptyview()


            }
        }.addOnFailureListener()
        {
            mBinding!!.swiperefresh.isRefreshing = false
        }


    }

    private fun updateEmptyview() {
        if (enrolledClassesList.size > 0) {
            mBinding!!.rrempty.visibility = View.GONE
        } else {
            mBinding!!.rrempty.visibility = View.VISIBLE
        }
    }


}


package com.autohub.studentmodule.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.listeners.ItemClickListener
import com.autohub.studentmodule.adaptors.EnrolledClassesAdaptor
import com.autohub.studentmodule.databinding.FragmentEnrolledClassesBinding
import com.autohub.studentmodule.models.BatchesModel
import com.google.firebase.firestore.SetOptions
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Vt Netzwelt
 */
class EnrolledClassesFragment : BaseFragment() {
    private var mBinding: FragmentEnrolledClassesBinding? = null

    private lateinit var enrolledClassesList: MutableList<BatchesModel>

    private val erolldClassDeleteClickListener = ItemClickListener<BatchesModel> {

        deleteBatch(it)

    }

    private fun deleteBatch(it: BatchesModel?) {
        // remove batch from Student profile and remove from enrolled classes
        removeBatchFromProfile(it)
    }

    private fun removeBatchFromProfile(batchmodel: BatchesModel?) {
        showLoading()


        firebaseStore.collection("students").document(appPreferenceHelper.getuserID()).get().addOnSuccessListener {
            hideLoading()
            if (it["batchCodes"] != null) {
                var batches: ArrayList<String> = it["batchCodes"] as ArrayList<String>
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

    private fun removeUserIdFromBatch(batchmodel: BatchesModel) {


        firebaseStore.collection("batches").whereEqualTo("batchCode", batchmodel.batchCode)
                .get().addOnSuccessListener {

                    it.forEach {
                        if (it["enrolledStudentsId"] != null) {
                            var enrollstudentIds: ArrayList<String> = it["enrolledStudentsId"] as ArrayList<String>
                            if (enrollstudentIds.contains(firebaseAuth.currentUser!!.uid)) {
                                enrollstudentIds.removeAt(enrollstudentIds.indexOf(firebaseAuth.currentUser!!.uid))
                                firebaseStore.collection("batches").document(batchmodel.documentId!!).set(
                                        mapOf("enrolledStudentsId" to enrollstudentIds), SetOptions.merge()
                                ).addOnCompleteListener {
                                    fetchEnrolledClasses()

                                }


                            }

                        }
                    }


                }
    }
    // user!!.batchCodes


    private var adaptor: EnrolledClassesAdaptor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
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


        mBinding!!.swiperefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            fetchEnrolledClasses()
        })

    }


    fun fetchEnrolledClasses() {

        firebaseStore.collection("students").document(appPreferenceHelper.getuserID()).get().addOnSuccessListener {
            hideLoading()
            if (it["batchCodes"] != null && (it["batchCodes"] as ArrayList<String>).size > 0) {
                var userBatchesCode: ArrayList<String> = it["batchCodes"] as ArrayList<String>

                firebaseStore.collection("batches").whereArrayContains("enrolledStudentsId", firebaseAuth.currentUser!!.uid)
                        .get().addOnCompleteListener { task ->

                            mBinding!!.swiperefresh.isRefreshing = false

                            if (task.isSuccessful) {
                                enrolledClassesList.clear()
                                for (document in task.result!!) {
                                    val batchesModel = document.toObject(BatchesModel::class.java)
                                    batchesModel.documentId = document.id



                                    try {
                                        var endTime = uTCToLocal("EEE MMM dd HH:mm:ss z YYYY",
                                                "EEE, d MMM yyyy HH:mm:ss z", batchesModel.timing.endTime!!.toDate().toString()
                                        )
                                        var startTime = uTCToLocal("EEE MMM dd HH:mm:ss z YYYY",
                                                "EEE, d MMM yyyy HH:mm:ss z", batchesModel.timing.startTime!!.toDate().toString()
                                        ).toString()
                                        batchesModel.batchTiming =
                                                startTime + " - " + endTime
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
                updateEmptyview()
                enrolledClassesList.clear()
                adaptor!!.setData(enrolledClassesList)


                mBinding!!.swiperefresh.isRefreshing = false

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

/*uTCToLocal("EEE, d MMM yyyy HH:mm:ss z",
                "EEE, d MMM yyyy HH:mm:ss z", remainderTime).toString())*/

    fun uTCToLocal(dateFormatInPut: String, dateFomratOutPut: String, datesToConvert: String): String? {


        val dateToReturn = datesToConvert

        val sdf = SimpleDateFormat(dateFormatInPut)

        var gmt: Date? = null

        val sdfOutPutToSend = SimpleDateFormat(dateFomratOutPut)
        sdfOutPutToSend.timeZone = TimeZone.getDefault()
        try {
            gmt = sdf.parse(datesToConvert)

        } catch (e: ParseException) {
            e.printStackTrace()
        }


        val originalFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z YYYY", Locale.ENGLISH)
        val targetFormat = SimpleDateFormat("h:mm a")
        val date = originalFormat.parse(gmt.toString())
        val formattedDate = targetFormat.format(date)






        return formattedDate
    }


}


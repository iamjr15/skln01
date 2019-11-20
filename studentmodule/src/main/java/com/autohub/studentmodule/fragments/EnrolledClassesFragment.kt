package com.autohub.studentmodule.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.listeners.ItemClickListener
import com.autohub.studentmodule.adaptors.EnrolledClassesAdaptor
import com.autohub.studentmodule.databinding.FragmentEnrolledClassesBinding
import com.autohub.studentmodule.models.BatchesModel
import com.google.firebase.firestore.FieldValue
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


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

    private fun removeBatchFromProfile(it: BatchesModel?) {
        firebaseStore.collection("students").document(appPreferenceHelper.getuserID()).update("batchCodes",
                FieldValue.arrayRemove(it!!.batchCode)).addOnSuccessListener {
        }
        firebaseStore.collection("batches").document(it.documentId!!).update("enrolledStudentId",
                FieldValue.arrayRemove(firebaseAuth.currentUser!!.uid)).addOnSuccessListener {
            Toast.makeText(context,
                    "Batch code deleted", Toast.LENGTH_SHORT).show()
            fetchEnrolledClasses()
        }
    }

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

        var studentsEnrollesIdMap: HashMap<String, ArrayList<String>> = HashMap()

        firebaseStore.collection("batches").whereArrayContains("enrolledStudentsId", firebaseAuth.currentUser!!.uid)
                .get().addOnCompleteListener { task ->

                    mBinding!!.swiperefresh.isRefreshing = false

                    if (task.isSuccessful) {
                        enrolledClassesList.clear()
                        for (document in task.result!!) {
                            val batchesModel = document.toObject(BatchesModel::class.java)




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




                            enrolledClassesList.add(batchesModel)

                        }
                        if (enrolledClassesList.size > 0) {
                            mBinding!!.rrempty.visibility = View.GONE
                        } else {
                            mBinding!!.rrempty.visibility = View.VISIBLE
                        }
                        adaptor!!.setData(enrolledClassesList)

                    }
                }.addOnFailureListener {

                    mBinding!!.swiperefresh.isRefreshing = false

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


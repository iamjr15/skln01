package com.autohub.studentmodule.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.listeners.ItemClickListener
import com.autohub.studentmodule.R
import com.autohub.studentmodule.adaptors.EnrolledClassesAdaptor
import com.autohub.studentmodule.databinding.FragmentEnrolledClassesBinding
import com.autohub.studentmodule.models.BatchesModel
import com.google.firebase.firestore.FieldValue

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
        showSnackError("Deletes")
    }

    private fun removeBatchFromProfile(it: BatchesModel?) {
        val updates = hashMapOf<String, Any>(
                "capital" to FieldValue.delete()
        )

        firebaseStore.collection("batches").document(it!!.documentId!!).get().addOnCompleteListener {


        }
        //.delete().


    }

    private var adaptor: EnrolledClassesAdaptor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enrolled_classes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentEnrolledClassesBinding.bind(view)
        mBinding!!.enrolledrecycleview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adaptor = EnrolledClassesAdaptor(requireContext(), erolldClassDeleteClickListener)
        mBinding!!.enrolledrecycleview.adapter = adaptor
        enrolledClassesList = ArrayList()
        fetchEnrolledClasses()

    }


    fun fetchEnrolledClasses() {

        var studentsEnrollesIdMap: HashMap<String, ArrayList<String>> = HashMap()

        firebaseStore.collection("batches").whereArrayContains("enrolledStudentsId", firebaseAuth.currentUser!!.uid)
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        enrolledClassesList.clear()
                        for (document in task.result!!) {
                            val batchesModel = document.toObject(BatchesModel::class.java)

                            /*  var studentsIds: ArrayList<String> = ArrayList()
                            if (batchesModel.studentsEnrolled.student.size > 0) {
                                for (student in batchesModel!!.studentsEnrolled.student) {
                                    studentsIds.add(student.id)
                                }
                            }
                            studentsEnrollesIdMap[batchesModel.id!!] = studentsIds*/
                            enrolledClassesList.add(batchesModel)

                        }
                        adaptor!!.setData(enrolledClassesList)

                        fetchUserImages(studentsEnrollesIdMap)
                    }
                }

    }

    fun fetchUserImages(studentsEnrollesIdMap: HashMap<String, ArrayList<String>>) {


    }

}


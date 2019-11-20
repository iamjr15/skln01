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
import com.autohub.skln.models.batches.BatchesModel

/**
 * Created by Vt Netzwelt
 */
class EnrolledClassesFragment : BaseFragment() {
    private var mBinding: FragmentEnrolledClassesBinding? = null

    private lateinit var enrolledClassesList: MutableList<BatchesModel>

    private val tutorsClickListener = ItemClickListener<String> {

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
        adaptor = EnrolledClassesAdaptor(requireContext(), tutorsClickListener)
        mBinding!!.enrolledrecycleview.adapter = adaptor
        enrolledClassesList = ArrayList()
        fetchEnrolledClasses()

    }


    fun fetchEnrolledClasses() {


        firebaseStore.collection("batches").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                enrolledClassesList.clear()
                for (document in task.result!!) {
                    val batchesModel = document.toObject(BatchesModel::class.java)
                    enrolledClassesList.add(batchesModel)

                }

                adaptor!!.setData(enrolledClassesList)
            }
        }

    }


}

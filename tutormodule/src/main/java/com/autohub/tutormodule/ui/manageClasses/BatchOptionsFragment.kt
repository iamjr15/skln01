package com.autohub.tutormodule.ui.manageClasses

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.models.batches.BatchesModel
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.FragmentTutorBatchOptionsBinding
import com.autohub.tutormodule.ui.dashboard.listener.HomeListener


class BatchOptionsFragment : BaseFragment() {
    private lateinit var mBinding: FragmentTutorBatchOptionsBinding
    private lateinit var homeListener: HomeListener
    private lateinit var batchData: BatchesModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_tutor_batch_options, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentTutorBatchOptionsBinding.bind(view)
        mBinding.callback = this

        batchData = arguments?.getParcelable("batch")!!

        mBinding.batchCode.text = getString(R.string.batch_code) + batchData.batchCode
        mBinding.batchName.text = batchData.title
        mBinding.className.text = batchData.grade?.name?.replace("_", " ") + " | " + batchData.subject?.name

        mBinding.batchCode.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Hi ,Below is your Batch Code for " +
                        batchData.title.toString().toUpperCase() + " Batch.\n\n" + batchData.batchCode)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "Share Batch Code.")
            startActivity(shareIntent)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeListener = context as HomeListener
    }

    fun openAttendanceScreen() {
    }

    fun openManageStudentsScreen() {
        homeListener.showStudentsListFragment()

    }

    fun openEditScheduleScreen() {
        homeListener.showAddBatchFragment(false, batchData)

    }
}
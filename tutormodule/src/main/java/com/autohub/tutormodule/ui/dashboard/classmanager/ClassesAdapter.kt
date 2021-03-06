package com.autohub.tutormodule.ui.dashboard.classmanager

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.autohub.skln.models.batches.BatchesModel
import com.autohub.skln.utills.AppConstants
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.ItemTutorManageClassesBinding
import com.autohub.tutormodule.ui.utils.AppConstants.DateFormatInput
import com.autohub.tutormodule.ui.utils.AppConstants.DateFormatOutput
import com.autohub.tutormodule.ui.utils.AppUtils

class ClassesAdapter(var context: Context, var listener: Listener) : RecyclerView.Adapter<ClassesAdapter.Holder>() {

    private var batchesList: List<BatchesModel> = ArrayList()
    lateinit var mBinding: ItemTutorManageClassesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        mBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_tutor_manage_classes, parent, false
        )
        return Holder(mBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        mBinding.batchName.text = batchesList[position].title
        mBinding.className.text = batchesList[position].grade?.name?.replace("_", " ") + " | " + batchesList[position].subject?.name
        mBinding.studentsCount.text = batchesList[position].enrolledStudentsId.size.toString() + " students"

        mBinding.time.text = AppUtils.uTCToLocal(DateFormatInput, DateFormatOutput,
                batchesList[position].timing?.startTime!!.toDate().toString()).toString() + " - " +
                AppUtils.uTCToLocal(DateFormatInput, DateFormatOutput,
                        batchesList[position].timing?.endTime!!.toDate().toString()).toString() + " | " +
                batchesList[position].selectedDays.joinToString(" , ")

        mBinding.toggleButton.isChecked = batchesList[position].status.equals(AppConstants.STATUS_ACTIVE)


        /* Update status in database on toggle button state changes
        * */
        mBinding.toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                listener.updateStatusOfBatches(AppConstants.STATUS_ACTIVE, batchesList[position], position)

            } else {
                listener.updateStatusOfBatches(AppConstants.STATUS_CANCELLED, batchesList[position], position)

            }
        }

       /*Open batch options fragment
       * */
        holder.itemView.setOnClickListener {
            listener.openBatchOptionsFragment(batchesList[position])
        }

    }

    override fun getItemCount(): Int {
        return batchesList.size
    }

    fun setData(batchesList: MutableList<BatchesModel>) {
        this.batchesList = batchesList
        notifyDataSetChanged()
    }

    inner class Holder(exploreRowBinding: ItemTutorManageClassesBinding) :
            RecyclerView.ViewHolder(exploreRowBinding.root) {
        fun bind() {
        }
    }

}

interface Listener {
    fun openBatchOptionsFragment(batch: BatchesModel)
    fun updateStatusOfBatches(status: String, batchesModel: BatchesModel, position: Int)

}
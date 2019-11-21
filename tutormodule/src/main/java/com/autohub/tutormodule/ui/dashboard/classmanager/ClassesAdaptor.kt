package com.autohub.tutormodule.ui.dashboard.classmanager

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.autohub.skln.models.batches.BatchesModel
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.ItemTutorManageClassesBinding
import com.autohub.tutormodule.ui.utils.AppUtils

class ClassesAdaptor(var context: Context) : RecyclerView.Adapter<ClassesAdaptor.Holder>() {

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

        mBinding.time.text = AppUtils.uTCToLocal("EEE MMM dd HH:mm:ss z YYYY",
                "EEE, d MMM yyyy HH:mm:ss z",
                batchesList[position].timing?.startTime!!.toDate().toString()).toString() + " - " +
                AppUtils.uTCToLocal("EEE MMM dd HH:mm:ss z YYYY",
                        "EEE, d MMM yyyy HH:mm:ss z",
                        batchesList[position].timing?.endTime!!.toDate().toString()).toString()
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
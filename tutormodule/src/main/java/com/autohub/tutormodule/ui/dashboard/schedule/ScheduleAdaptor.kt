package com.autohub.tutormodule.ui.dashboard.schedule

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.autohub.skln.models.batches.BatchesModel
import com.autohub.skln.utills.AppConstants
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.ScheduleRowBinding
import com.autohub.tutormodule.ui.utils.AppUtils

class ScheduleAdaptor(var context: Context)
    : RecyclerView.Adapter<ScheduleAdaptor.Holder>() {

    private var batchList: List<BatchesModel> = ArrayList()
    lateinit var exploreRowBinding: ScheduleRowBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        exploreRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.schedule_row, parent, false)

        return Holder(exploreRowBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        exploreRowBinding.batchName.text = batchList[position].title
        exploreRowBinding.time.text = AppUtils.uTCToLocal(
                "EEE MMM dd HH:mm:ss z YYYY",
                "EEE, d MMM yyyy HH:mm:ss z",
                batchList[position].timing?.startTime!!.toDate().toString()).toString() + " - " +
                AppUtils.uTCToLocal("EEE MMM dd HH:mm:ss z YYYY",
                        "EEE, d MMM yyyy HH:mm:ss z",
                        batchList[position].timing?.endTime!!.toDate().toString()).toString()

        exploreRowBinding.activeButton.text = batchList[position].status

        if (batchList[position].status == AppConstants.STATUS_ACTIVE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                exploreRowBinding.cardView.setBackgroundColor(context.resources.getColor(R.color.skyblue, null))
            } else {
                exploreRowBinding.cardView.setBackgroundColor(context.resources.getColor(R.color.skyblue))
            }
            exploreRowBinding.activeButton.background = context.resources.getDrawable(com.autohub.skln.R.drawable.selector_green_round, null)
        }

        if (batchList[position].status == AppConstants.STATUS_CANCELLED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                exploreRowBinding.cardView.setBackgroundColor(context.resources.getColor(R.color.yellow, null))
            } else {
                exploreRowBinding.cardView.setBackgroundColor(context.resources.getColor(R.color.yellow))
            }
            exploreRowBinding.activeButton.background = context.resources.getDrawable(com.autohub.skln.R.drawable.selector_black_round, null)
        }
    }

    override fun getItemCount(): Int {
        return batchList.size
    }

    fun setData(batchList: List<BatchesModel>) {
        this.batchList = batchList
        notifyDataSetChanged()
    }

    inner class Holder(exploreRowBinding: ScheduleRowBinding) :
            RecyclerView.ViewHolder(exploreRowBinding.root) {
        fun bind() {
        }
    }


}
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
import com.autohub.tutormodule.databinding.ItemScheduleViewBinding
import com.autohub.tutormodule.ui.utils.AppConstants.DateFormatInput
import com.autohub.tutormodule.ui.utils.AppConstants.DateFormatOutput
import com.autohub.tutormodule.ui.utils.AppUtils

class ScheduleAdapter(var context: Context)
    : RecyclerView.Adapter<ScheduleAdapter.Holder>() {

    private var batchList: List<BatchesModel> = ArrayList()
    lateinit var mBinding: ItemScheduleViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.item_schedule_view, parent, false)

        return Holder(mBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        mBinding.batchName.text = batchList[position].title + " - " + AppUtils.getClassName(batchList[position].grade?.name?.split("_")?.get(1)?.trim()!!)

        mBinding.time.text = AppUtils.uTCToLocal(
                DateFormatInput, DateFormatOutput,
                batchList[position].timing?.startTime!!.toDate().toString()).toString() + " - " +
                AppUtils.uTCToLocal(DateFormatInput, DateFormatOutput,
                        batchList[position].timing?.endTime!!.toDate().toString()).toString()

        mBinding.activeButton.text = batchList[position].status

        if (batchList[position].status == AppConstants.STATUS_ACTIVE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mBinding.cardView.setCardBackgroundColor(context.resources.getColor(R.color.skyBlue, null))
            } else {
                mBinding.cardView.setCardBackgroundColor(context.resources.getColor(R.color.skyBlue))
            }
            mBinding.activeButton.background = context.resources.getDrawable(com.autohub.skln.R.drawable.selector_green_round, null)
        }

        if (batchList[position].status == AppConstants.STATUS_CANCELLED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mBinding.cardView.setCardBackgroundColor(context.resources.getColor(R.color.yellow, null))
            } else {
                mBinding.cardView.setCardBackgroundColor(context.resources.getColor(R.color.yellow))
            }
            mBinding.activeButton.background = context.resources.getDrawable(com.autohub.skln.R.drawable.selector_black_round, null)
        }
    }

    override fun getItemCount(): Int {
        return batchList.size
    }

    fun setData(batchList: List<BatchesModel>) {
        this.batchList = batchList
        notifyDataSetChanged()
    }

    inner class Holder(mBinding: ItemScheduleViewBinding) :
            RecyclerView.ViewHolder(mBinding.root) {
        fun bind() {
        }
    }


}
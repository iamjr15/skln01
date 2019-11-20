package com.autohub.studentmodule.adaptors

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.autohub.skln.utills.AppConstants
import com.autohub.studentmodule.R
import com.autohub.studentmodule.databinding.SchedulesRowBinding
import com.autohub.studentmodule.models.BatchesModel

/**
 * Created by Vt Netzwelt
 */
class SchedulesAdaptor(var context: Context)
    : RecyclerView.Adapter<SchedulesAdaptor.Holder>() {

    private var batchList: List<BatchesModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val exploreRowBinding: SchedulesRowBinding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.schedules_row, parent, false
                )

        return Holder(exploreRowBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        batchList[position].let {
            with(holder.exploreRowBinding)
            {
                batchName.text = it.title
                time.text = it.batchTiming
                if (it.status == AppConstants.STATUS_ACTIVE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cardView.setBackgroundColor(context.resources.getColor(R.color.skyblue, null))
                    } else {
                        cardView.setBackgroundColor(context.resources.getColor(R.color.skyblue))
                    }
                    activeButton.background = context.resources.getDrawable(com.autohub.skln.R.drawable.selector_green_round, null)
                }

                if (it.status == AppConstants.STATUS_ACTIVE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cardView.setBackgroundColor(context.resources.getColor(R.color.skyblue, null))
                    } else {
                        cardView.setBackgroundColor(context.resources.getColor(R.color.skyblue))
                    }
                    activeButton.background = context.resources.getDrawable(com.autohub.skln.R.drawable.selector_green_round, null)
                }

                if (it.status == AppConstants.STATUS_CANCELLED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cardView.setBackgroundColor(context.resources.getColor(R.color.yellow, null))
                    } else {
                        cardView.setBackgroundColor(context.resources.getColor(R.color.yellow))
                    }
                    activeButton.background = context.resources.getDrawable(com.autohub.skln.R.drawable.selector_black_round, null)
                }
            }


        }

    }


    override fun getItemCount(): Int {
        return batchList.size
    }

    fun setData(userList: List<BatchesModel>) {
        this.batchList = userList
        notifyDataSetChanged()
    }

    inner class Holder(var exploreRowBinding: SchedulesRowBinding) :
            RecyclerView.ViewHolder(exploreRowBinding.root) {
        fun bind() {
        }
    }


}
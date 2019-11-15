package com.autohub.tutormodule.ui.dashboard.schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.ScheduleRowBinding

class ScheduleAdaptor(var context: Context)
    : RecyclerView.Adapter<ScheduleAdaptor.Holder>() {

    private var userList: List<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val exploreRowBinding: ScheduleRowBinding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.schedule_row, parent, false
                )

        return Holder(exploreRowBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {


    }

    override fun getItemCount(): Int {
        return 10/*userList.size*/
    }

    fun setData(userList: List<String>) {
        this.userList = userList
        notifyDataSetChanged()
    }

    inner class Holder(exploreRowBinding: ScheduleRowBinding) :
            RecyclerView.ViewHolder(exploreRowBinding.root) {
        fun bind() {
        }
    }


}
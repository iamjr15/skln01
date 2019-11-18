package com.autohub.tutormodule.ui.dashboard.requests

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.autohub.skln.models.batchRequests.BatchRequestData
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.ItemTutorRequestBinding
import com.autohub.tutormodule.ui.utils.AppUtils

class RequestsAdaptor(var context: Context)
    : RecyclerView.Adapter<RequestsAdaptor.Holder>() {

    private var requestsList: List<BatchRequestData> = ArrayList()
    lateinit var itemRequestBinding: ItemTutorRequestBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        itemRequestBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_tutor_request, parent, false
        )

        return Holder(itemRequestBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        itemRequestBinding.name.text = requestsList[position].teacher?.name
        itemRequestBinding.className.text = AppUtils.getClassName(requestsList[position].grade?.name?.split("_")?.get(1)!!)
        itemRequestBinding.subject.text = requestsList[position].subject?.name

    }

    override fun getItemCount(): Int {
        return requestsList.size
    }

    fun setData(requestsList: List<BatchRequestData>) {
        this.requestsList = requestsList
        notifyDataSetChanged()
    }

    inner class Holder(itemRequestBinding: ItemTutorRequestBinding) :
            RecyclerView.ViewHolder(itemRequestBinding.root) {
        fun bind() {
        }
    }


}
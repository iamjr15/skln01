package com.autohub.tutormodule.ui.dashboard.requests

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.ItemTutorRequestBinding

class RequestsAdaptor(var context: Context)
    : RecyclerView.Adapter<RequestsAdaptor.Holder>() {

    private var userList: List<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemRequestbinding: ItemTutorRequestBinding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_tutor_request, parent, false
                )

        return Holder(itemRequestbinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {


    }

    override fun getItemCount(): Int {
        return 0/*userList.size*/
    }

    fun setData(userList: List<String>) {
        this.userList = userList
        notifyDataSetChanged()
    }

    inner class Holder(itemRequestbinding: ItemTutorRequestBinding) :
            RecyclerView.ViewHolder(itemRequestbinding.root) {
        fun bind() {
        }
    }


}
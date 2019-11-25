package com.autohub.studentmodule.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.autohub.skln.listeners.ItemClickListener
import com.autohub.studentmodule.R
import com.autohub.studentmodule.databinding.EnrolledclassesRowBinding
import com.autohub.studentmodule.models.BatchesModel

/**
 * Created by Vt Netzwelt
 */

class EnrolledClassesAdaptor(var context: Context, var erolldClassDeleteClickListener: ItemClickListener<BatchesModel>)
    : RecyclerView.Adapter<EnrolledClassesAdaptor.Holder>() {

    private var userList: List<BatchesModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val enrolledClasesBinding: EnrolledclassesRowBinding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.enrolledclasses_row, parent, false
                )

        return Holder(enrolledClasesBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.enrolledClasesBinding.delete.setOnClickListener {
            erolldClassDeleteClickListener.onClick(userList[position])
        }

        userList[position].let {
            with(holder.enrolledClasesBinding)
            {
                batchName.text = it.title
                className.text = it.grade.name + " | " + it.subject!!.name
                studentsCount.text = it.enrolledStudentsId!!.size.toString() + " students"
                time.text = it.batchTiming


            }

        }



    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(userList: List<BatchesModel>) {
        this.userList = userList
        notifyDataSetChanged()
    }

    inner class Holder(var enrolledClasesBinding: EnrolledclassesRowBinding) :
            RecyclerView.ViewHolder(enrolledClasesBinding.root) {
        fun bind() {
        }
    }


}
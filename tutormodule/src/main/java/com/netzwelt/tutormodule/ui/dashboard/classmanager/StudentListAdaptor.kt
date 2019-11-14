package com.netzwelt.tutormodule.ui.dashboard.classmanager

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.netzwelt.tutormodule.R
import com.netzwelt.tutormodule.databinding.ItemStudentBinding

class StudentListAdaptor(var context: Context)
    : RecyclerView.Adapter<StudentListAdaptor.Holder>() {

    private var userList: List<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val mBinding: ItemStudentBinding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_student, parent, false
                )

        return Holder(mBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {


    }

    override fun getItemCount(): Int {
        return 3/*userList.size*/
    }

    fun setData(userList: List<String>) {
        this.userList = userList
        notifyDataSetChanged()
    }

    inner class Holder(mBinding: ItemStudentBinding) :
            RecyclerView.ViewHolder(mBinding.root) {
        fun bind() {
        }
    }


}
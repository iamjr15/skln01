package com.autohub.tutormodule.ui.dashboard.classmanager

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.autohub.skln.models.UserModel
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.ItemStudentBinding
import com.autohub.tutormodule.ui.utils.AppUtils

class StudentListAdaptor(var context: Context)
    : RecyclerView.Adapter<StudentListAdaptor.Holder>() {

    private var studentList: List<UserModel> = ArrayList()
    lateinit var mBinding: ItemStudentBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        mBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_student, parent, false
                )

        return Holder(mBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        mBinding.name.text = studentList[position].personInfo?.firstName + " " + studentList[position].personInfo?.lastName
        mBinding.className.text = AppUtils.getClassName(studentList[position].className!!)

    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    fun setData(studentList: List<UserModel>) {
        this.studentList = studentList
        notifyDataSetChanged()
    }

    inner class Holder(mBinding: ItemStudentBinding) :
            RecyclerView.ViewHolder(mBinding.root) {
        fun bind() {
        }
    }


}
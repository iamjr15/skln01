package com.autohub.studentmodule.adaptors

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.autohub.skln.listeners.ItemClickListener
import com.autohub.skln.models.AcademicData
import com.autohub.studentmodule.R
import com.autohub.studentmodule.databinding.AcadmisRowBinding

/**
 * Created by Vt Netzwelt
 */


class AcademicAdaptor(var context: Context, private var mItemClickListener: ItemClickListener<AcademicData>)
    : RecyclerView.Adapter<AcademicAdaptor.Holder>() {

    private var academicDataList: List<AcademicData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val academicrowbinding: AcadmisRowBinding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.acadmis_row, parent, false
                )


        return Holder(academicrowbinding)
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.countriesRowBinding.itemClickListener = mItemClickListener
        academicDataList[position].let {
            with(holder.countriesRowBinding)
            {
                academicdata = it
                img.setImageResource(it.icon)
                txt.text = it.classname + "."

                val unwrappedDrawable = ContextCompat.getDrawable(context, com.autohub.skln.R.drawable.academic_rowbg_drawable)
                val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
                DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, it.color))
                rr.background = wrappedDrawable
            }
        }

    }

    override fun getItemCount(): Int {
        return academicDataList.size
    }

    fun setData(academicDataList: List<AcademicData>) {
        this.academicDataList = academicDataList
        notifyDataSetChanged()
    }

    inner class Holder(var countriesRowBinding: AcadmisRowBinding) :
            RecyclerView.ViewHolder(countriesRowBinding.root) {
        fun bind() {
        }
    }


}
package com.example.androidapp.ui.home.fragments.country

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.autohub.skln.R
import com.autohub.skln.listeners.ItemClickListener
import com.autohub.skln.models.AcadmicsData


class AcadmicsAdaptor(var context: Context, var mItemClickListener: ItemClickListener<AcadmicsData>)
    : RecyclerView.Adapter<AcadmicsAdaptor.Holder>() {

    private var acadmicDataList: List<AcadmicsData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val acadmicrowbinding: com.autohub.skln.databinding.AcadmisRowBinding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.acadmis_row, parent, false
                )


        return Holder(acadmicrowbinding)
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.countriesRowBinding.setItemClickListener(mItemClickListener)
        acadmicDataList[position].let {
            with(holder.countriesRowBinding)
            {
                acadmicdata = it
                img.setImageResource(it.icon)
                txt.setText(it.classname)

                val unwrappedDrawable = ContextCompat.getDrawable(context, R.drawable.acadmic_rowbg_drawable)
                val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
                DrawableCompat.setTint(wrappedDrawable, context.resources.getColor(it.color)/* it.color*/)
                rr.setBackground(wrappedDrawable)
            }
        }

    }

    override fun getItemCount(): Int {
        return acadmicDataList.size
    }

    fun setData(acadmicDataList: List<AcadmicsData>) {
        this.acadmicDataList = acadmicDataList
        notifyDataSetChanged()
    }

    inner class Holder(var countriesRowBinding: com.autohub.skln.databinding.AcadmisRowBinding) :
            RecyclerView.ViewHolder(countriesRowBinding.root) {
        fun bind(obj: Any) {
            with(countriesRowBinding)
            {

            }
        }
    }


}
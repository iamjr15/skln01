package com.autohub.studentmodule.adaptors

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.autohub.skln.listeners.ItemClickListener
import com.autohub.skln.models.tutor.TutorData
import com.autohub.skln.utills.CommonUtils
import com.autohub.skln.utills.RoundedCornersTransformation
import com.autohub.studentmodule.R
import com.autohub.studentmodule.databinding.ExploreRowBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage

/**
 * Created by Vt Netzwelt
 */

class ExploreAdaptor(var context: Context, var mItemClickListener: ItemClickListener<TutorData>)
    : RecyclerView.Adapter<ExploreAdaptor.Holder>() {

    private var userList: List<TutorData> = ArrayList()
    private var mCurrentLocation: Location? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val exploreRowBinding: ExploreRowBinding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.explore_row, parent, false
                )

        return Holder(exploreRowBinding)
    }


    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.exploreRowBinding.itemClickListener = mItemClickListener
        userList[position].let {
            with(holder.exploreRowBinding)
            {

                txtdistance.text = "${it.distance} Km"

                user = it
                tutorname.text = """${it.personInfo!!.firstName} ${it.personInfo!!.lastName}"""

                // $price/for 3 class per Month

                txtclassprice.text = """$ ${it.packageInfo!!.price} / ${it.packageInfo!!.occurances} CLASSES / PER ${it.packageInfo!!.rateOption}"""


                txtgrades.text = "pending"
                val splitarray = it.classToTeach!!.split(",")
                if (splitarray.isNotEmpty()) {
                    val stringBuilder = StringBuilder(splitarray.size)
                    for (i in splitarray.indices) {
                        if (i == splitarray.size - 1) {
                            stringBuilder.append(splitarray[i] + CommonUtils.getClassSuffix(splitarray[i].toInt()))

                        } else {
                            stringBuilder.append(splitarray[i] + CommonUtils.getClassSuffix(splitarray[i].toInt()) + " - ")
                        }
                    }
                    txtgrades.text = stringBuilder.toString()


                } else {
                    txtgrades.text = it.classToTeach

                }


                txtclasstype.text = it.qualification!!.classType
                txtsubjects.text = it.subjectsToTeach!!.replace(",", " | ")

                if (!TextUtils.isEmpty(it.personInfo!!.accountPicture)) {
                    val pathReference1 = FirebaseStorage.getInstance().reference.child(it.personInfo!!.accountPicture!!)
                    val options = RequestOptions()
                    options.transforms(MultiTransformation(CenterCrop(), RoundedCornersTransformation(context, CommonUtils.convertDpToPixel(6f, context).toInt(), 0)))
                    options.placeholder(R.drawable.dummyexploreimage)
                    Glide.with(context).load(pathReference1)
                            .apply(options).into(imgprofile)

                }
            }
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(userList: List<TutorData>, mCurrentLocation: Location?) {
        this.userList = userList
        this.mCurrentLocation = mCurrentLocation
        notifyDataSetChanged()
    }

    inner class Holder(var exploreRowBinding: ExploreRowBinding) :
            RecyclerView.ViewHolder(exploreRowBinding.root) {
        fun bind() {
        }
    }


}
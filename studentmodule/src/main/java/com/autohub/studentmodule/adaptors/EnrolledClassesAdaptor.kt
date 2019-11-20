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

class EnrolledClassesAdaptor(var context: Context, var mItemClickListener: ItemClickListener<String>)
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

        userList[position].let {
            with(holder.enrolledClasesBinding)
            {
                batchName.text = it.title
                className.text = it.grade.name + " | " + it.subject.name

            }


        }


        /*  holder.exploreRowBinding.setItemClickListener(mItemClickListener)
          userList[position].let {
              with(holder.exploreRowBinding)
              {

                  if (mCurrentLocation != null) {
                      txtdistance.setText("${it.distance.toString()} Km")
                  }

                  user = it
                  tutorname.setText(it.firstName + " " + it.lastName)
                  txtclassprice.setText(it.rate + " / " + it.noOfClasses + " PER " + it.paymentDuration)


                  var splitarray = it.classesToTeach.split(",")
                  if (splitarray.size > 0) {
                      var stringBuilder = StringBuilder(splitarray.size)
                      for (i in splitarray.indices) {
                          if (i == splitarray.size - 1) {
                              stringBuilder.append(splitarray.get(i) + CommonUtils.getClassSuffix(splitarray.get(i).toInt()))

                          } else {
                              stringBuilder.append(splitarray.get(i) + CommonUtils.getClassSuffix(splitarray.get(i).toInt()) + " - ")
                          }
                      }
                      txtgrades.setText(stringBuilder.toString())


                  } else {
                      txtgrades.setText(it.classesToTeach)

                  }



                  txtclasstype.setText(it.classType)
                  txtsubjects.setText(it.subjectsToTeach.replace(",", " | "))

                  if (!TextUtils.isEmpty(it.pictureUrl)) {
                      val pathReference1 = FirebaseStorage.getInstance().reference.child(it.pictureUrl)
                      val options = RequestOptions()
                      options.transforms(MultiTransformation(CenterCrop(), RoundedCornersTransformation(context, CommonUtils.convertDpToPixel(6f, context).toInt(), 0)))
                      options.placeholder(R.drawable.dummyexploreimage)
                      Glide.with(context).load(pathReference1)
                              .apply(options).into(imgprofile)

                  }

              }
          }*/

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
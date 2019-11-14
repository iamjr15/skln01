package com.netzwelt.tutormodule.ui.manageClasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.autohub.skln.fragment.BaseFragment
import com.netzwelt.tutormodule.R
import com.netzwelt.tutormodule.databinding.FragmentTutorAddBatchBinding


class AddBatchFragment : BaseFragment() {
    private lateinit var mBinding: FragmentTutorAddBatchBinding
    private var isAddBatch: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_tutor_add_batch, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentTutorAddBatchBinding.bind(view)
        mBinding.callback = this

        if (!this.arguments?.isEmpty!!) {
            isAddBatch = this.arguments!!.getBoolean("type")
        }

        if (!isAddBatch) {
            mBinding.textHeading.text = resources.getString(R.string.edit_schedule)
        }
    }

    fun openBatchOptions() {
        val batchOptionsFragment = BatchOptionsFragment()
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.addBatchContainer, batchOptionsFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun onDaySelected(view: View) {
        if ((view as TextView).currentTextColor == resources.getColor(R.color.black)) {
            view.background = resources.getDrawable(R.drawable.bg_round_blue, null)
            view.setTextColor(resources.getColor(com.autohub.skln.R.color.white))
        } else {
            view.background = resources.getDrawable(R.drawable.bg_round_black, null)
            view.setTextColor(resources.getColor(R.color.black))
        }
    }


    companion object {
        fun newInstance(): AddBatchFragment = AddBatchFragment()
    }
}
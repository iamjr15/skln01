package com.netzwelt.tutormodule.ui.manageClasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autohub.skln.fragment.BaseFragment
import com.netzwelt.tutormodule.R
import com.netzwelt.tutormodule.databinding.FragmentTutorAddBatchBinding


class AddBatchFragment : BaseFragment() {
    private lateinit var mBinding: FragmentTutorAddBatchBinding
    private var  isAddBatch : Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_tutor_add_batch, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentTutorAddBatchBinding.bind(view)
        mBinding.callback = this

        if (!this.arguments?.isEmpty!!){
         isAddBatch = this.arguments!!.getBoolean("type")
        }

        if(!isAddBatch){
            mBinding.textHeading.text = resources.getString(R.string.edit_schedule)
        }
    }

    fun openBatchOptions(){
        val batchOptionsFragment = BatchOptionsFragment()
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.addBatchContainer, batchOptionsFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    companion object {
        fun newInstance(): AddBatchFragment = AddBatchFragment()
    }
}
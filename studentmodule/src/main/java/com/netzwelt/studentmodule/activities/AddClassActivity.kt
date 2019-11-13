package com.netzwelt.studentmodule.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.netzwelt.studentmodule.R
import com.netzwelt.studentmodule.databinding.ActivityAddClassBinding

/**
 * Created by Vt Netzwelt
 */

class AddClassActivity : AppCompatActivity() {
    private var mBinding: ActivityAddClassBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_class)
        mBinding!!.callback = this
    }


    fun onAddclick() {
        mBinding!!.lladdclass.visibility = View.GONE

        mBinding!!.lladdclasssucess.visibility = View.VISIBLE
    }

    fun onOkClick() {
        finish()

    }


}

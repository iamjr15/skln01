package com.netzwelt.tutormodule.ui.dashboard.profile

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.autohub.skln.BaseActivity
import com.netzwelt.tutormodule.R
import com.netzwelt.tutormodule.databinding.ActivityTutorEditProfileBinding

class EditProfileActivity :BaseActivity() {

    private lateinit var mBinding : ActivityTutorEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_edit_profile)


    }
}
package com.autohub.tutormodule.ui.dashboard.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autohub.skln.fragment.BaseFragment
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.FragmentTutorProfileBinding

class ProfileFragment : BaseFragment() {
    private lateinit var mBinding: FragmentTutorProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_tutor_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentTutorProfileBinding.bind(view)

        mBinding.editProfile.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }
}
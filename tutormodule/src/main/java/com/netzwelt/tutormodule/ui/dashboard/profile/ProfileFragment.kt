package com.netzwelt.tutormodule.ui.dashboard.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autohub.skln.fragment.BaseFragment
import com.netzwelt.tutormodule.R
import com.netzwelt.tutormodule.databinding.FragmentTutorProfileBinding
import android.content.Intent

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
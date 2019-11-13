package com.netzwelt.studentmodule.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autohub.skln.activities.OnBoardActivity
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.utills.ActivityUtils
import com.autohub.skln.utills.AppConstants
import com.autohub.skln.utills.CommonUtils
import com.autohub.skln.utills.GlideApp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.netzwelt.studentmodule.R
import com.netzwelt.studentmodule.activities.EditStudentProfileActivity
import com.netzwelt.studentmodule.databinding.FragmentStudentProfileBinding

/**
 * Created by Vt Netzwelt
 */
class StudentProfileFragment : BaseFragment() {

    private var mBinding: FragmentStudentProfileBinding? = null
    private var mProfileType = "tutor"
    private var mStorageReference: StorageReference? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            mProfileType = arguments!!.getString(AppConstants.KEY_TYPE, "student")
        }
        mBinding = FragmentStudentProfileBinding.bind(view)
        mBinding!!.calback = this
        mStorageReference = FirebaseStorage.getInstance().reference
        setupProfile()
        mBinding!!.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            appPreferenceHelper.setSignupComplete(false)

            ActivityUtils.launchActivity(requireContext(), OnBoardActivity::class.java)
            requireActivity().finishAffinity()
        }
        // mBinding!!.profilePicture.setOnClickListener { onAddPicture() }
    }

    private fun setupProfile() {
        var path = "tutor/"
        if (mProfileType.equals("student", ignoreCase = true)) {
            path = "student/"
        }
        val ref = FirebaseStorage.getInstance().reference.child(path +
                firebaseAuth.currentUser!!.uid + ".jpg")
        GlideApp.with(this)
                .load(ref)
                .placeholder(R.drawable.default_pic)
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // disable caching of glide
                .skipMemoryCache(true)
                .into(mBinding!!.profilePicture)

        var root = getString(R.string.db_root_tutors)
        if (mProfileType.equals("student", ignoreCase = true)) {
            root = getString(R.string.db_root_students)
        }
        firebaseStore.collection(root).document(firebaseAuth.currentUser!!.uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    val firstName = documentSnapshot.getString(AppConstants.KEY_FIRST_NAME)
                    val lastName = documentSnapshot.getString(AppConstants.KEY_LAST_NAME)
                    if (!TextUtils.isEmpty(lastName)) {
                        mBinding!!.name.text = String.format("%s %s.", firstName,
                                lastName!!.substring(0, 1))
                    }

                    val studentClass = documentSnapshot.getString(AppConstants.KEY_STDT_CLASS)

                    mBinding!!.grade.text = "grade ${studentClass}${CommonUtils.getClassSuffix(studentClass!!.toInt())} "

                }
                .addOnFailureListener { e -> showSnackError(e.message) }
    }

    fun onEditProfileClick() {


        ActivityUtils.launchActivity(requireContext(), EditStudentProfileActivity::class.java)

        /*if (mProfileType.equals("student", ignoreCase = true)) {
            ActivityUtils.launchFragment(requireContext(), com.autohub.skln.student.EditProfileFragment::class.java.name)
            return
        }
        ActivityUtils.launchFragment(requireContext(), EditProfileFragment::class.java.name)*/
    }

    fun onEditPersonalDetailClick() {

    }


}

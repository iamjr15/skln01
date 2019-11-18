package com.autohub.studentmodule.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autohub.skln.activities.OnBoardActivity
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.models.UserModel
import com.autohub.skln.utills.ActivityUtils
import com.autohub.skln.utills.AppConstants
import com.autohub.skln.utills.CommonUtils
import com.autohub.skln.utills.GlideApp
import com.autohub.studentmodule.R
import com.autohub.studentmodule.activities.EditStudentProfileActivity
import com.autohub.studentmodule.databinding.FragmentStudentProfileBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

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
            appPreferenceHelper.setStudentSignupComplete(false)

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
                .placeholder(com.autohub.skln.R.drawable.default_pic)
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // disable caching of glide
                .skipMemoryCache(true)
                .into(mBinding!!.profilePicture)

        var root = getString(R.string.db_root_tutors)
        if (mProfileType.equals("student", ignoreCase = true)) {
            root = getString(R.string.db_root_students)
        }
        firebaseStore.collection(root).document(firebaseAuth.currentUser!!.uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    var user: UserModel = documentSnapshot.toObject(UserModel::class.java)!!



                    if (!TextUtils.isEmpty(user.personInfo?.lastName)) {
                        mBinding!!.name.text = String.format("%s %s.", user.personInfo?.firstName,
                                user.personInfo?.lastName!!.substring(0, 1))
                    }

                    val studentClass = user.academicInfo!!.selectedClass
                    firebaseStore.collection("grades").whereEqualTo("id", studentClass).get().addOnSuccessListener {

                        it.forEach {
                            mBinding!!.grade.text = "grade ${it.getString("grade")!!}${CommonUtils.getClassSuffix(it.getString("grade")!!.toInt())} "
                        }
                    }


                }
                .addOnFailureListener { e -> showSnackError(e.message) }
    }

    fun onEditProfileClick() {


        ActivityUtils.launchActivity(requireContext(), EditStudentProfileActivity::class.java)

    }

    fun onEditPersonalDetailClick() {

    }


}

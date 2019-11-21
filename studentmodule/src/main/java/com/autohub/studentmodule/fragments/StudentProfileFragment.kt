package com.autohub.studentmodule.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autohub.skln.activities.OnBoardActivity
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.models.UserModel
import com.autohub.skln.utills.ActivityUtils
import com.autohub.skln.utills.CommonUtils
import com.autohub.skln.utills.GlideApp
import com.autohub.studentmodule.R
import com.autohub.studentmodule.activities.EditStudentProfileActivity
import com.autohub.studentmodule.activities.StudentHomeActivity
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
    private var mStorageReference: StorageReference? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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


    fun setProfileImage() {
        var userimagePath = (context as StudentHomeActivity).userimagePath!!

        if (userimagePath != null && !userimagePath.equals("")) {

            val ref = FirebaseStorage.getInstance().reference.child(userimagePath)
            GlideApp.with(this)
                    .load(ref)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)  // disable caching of glide
                    .skipMemoryCache(true)
                    .placeholder(com.autohub.skln.R.drawable.default_pic)
                    .into(mBinding!!.profilePicture)
        }
    }


    fun setupProfile() {

        firebaseStore.collection(getString(R.string.db_root_students)).document(appPreferenceHelper.getuserID()).get()
                .addOnSuccessListener { documentSnapshot ->
                    var user: UserModel = documentSnapshot.toObject(UserModel::class.java)!!
                    (context as StudentHomeActivity).userimagePath = user.personInfo!!.accountPicture

                    setProfileImage()

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

        val intent = Intent(context, EditStudentProfileActivity::class.java)
        activity!!.startActivityForResult(intent, StudentHomeActivity.EDITPROFILE_REQUEST)

//        ActivityUtils.launchActivity(requireContext(), EditStudentProfileActivity::class.java)

    }

    fun onEditPersonalDetailClick() {

    }


}

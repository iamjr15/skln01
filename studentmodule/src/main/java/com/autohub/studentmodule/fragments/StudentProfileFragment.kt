package com.autohub.studentmodule.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
        return inflater.inflate(com.autohub.studentmodule.R.layout.fragment_student_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding = FragmentStudentProfileBinding.bind(view)
        mBinding!!.calback = this
        mStorageReference = FirebaseStorage.getInstance().reference
        setupProfile()
        mBinding!!.logout.setOnClickListener {
            showLogoutPopup()
        }
    }

    /*
    * Show User profile Image
    *
    * */

    fun setProfileImage() {
        val userimagePath: String? = (context as StudentHomeActivity).userimagePath!!

        if (userimagePath != null && userimagePath != "") {

            GlideApp.with(this)
                    .load(userimagePath)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)  // disable caching of glide
                    .skipMemoryCache(true)
                    .placeholder(com.autohub.skln.R.drawable.default_pic)
                    .into(mBinding!!.profilePicture)
        }
    }

    /*
    * Logout confirmation dialog
    * */
    private fun showLogoutPopup() {
        val alert = AlertDialog.Builder(activity!!)
        alert.setMessage(getString(R.string.areyousure_msg))
                .setPositiveButton("Logout", DialogInterface.OnClickListener { dialog, which ->
                    logout() // Last step. Logout function
                }).setNegativeButton("Cancel", null)

        val alert1 = alert.create()
        alert1.show()
    }

    /*
    * Logout user and signout him from firebase too
    *
    * */
    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        appPreferenceHelper.setStudentSignupComplete(false)

        ActivityUtils.launchActivity(requireContext(), OnBoardActivity::class.java)
        requireActivity().finishAffinity()
    }


    private fun setupProfile() {

        firebaseStore.collection(getString(com.autohub.studentmodule.R.string.db_root_students)).document(appPreferenceHelper.getuserID()).get()
                .addOnSuccessListener { documentSnapshot ->
                    val user: UserModel = documentSnapshot.toObject(UserModel::class.java)!!
                    (context as StudentHomeActivity).userimagePath = user.personInfo!!.accountPicture

                    setProfileImage()

                    if (!TextUtils.isEmpty(user.personInfo?.lastName)) {
                        mBinding!!.name.text = String.format("%s %s.", user.personInfo?.firstName,
                                user.personInfo?.lastName!!.substring(0, 1))
                    }

                    val studentClass = user.academicInfo!!.selectedGrad
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
    }

    fun onEditPersonalDetailClick() {

    }


}

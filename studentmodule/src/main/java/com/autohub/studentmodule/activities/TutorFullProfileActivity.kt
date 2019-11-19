package com.autohub.studentmodule.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.autohub.skln.BaseActivity
import com.autohub.skln.models.UserModel
import com.autohub.skln.models.batches.BatchRequestModel
import com.autohub.skln.models.batches.BatchRequestViewModel
import com.autohub.skln.models.tutor.TutorData
import com.autohub.skln.utills.AppConstants
import com.autohub.skln.utills.GlideApp
import com.autohub.studentmodule.R
import com.autohub.studentmodule.databinding.ActivityTutorFullProfileBinding
import com.autohub.studentmodule.models.TutorViewModel
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import java.util.*

/**
 * Created by Vt Netzwelt
 */

class TutorFullProfileActivity : BaseActivity() {

    private var mBinding: ActivityTutorFullProfileBinding? = null
    private var mUserViewModel: TutorViewModel? = null
    private var mCurrentUser: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_full_profile)
        mBinding!!.callback = this


        getIntentData()

        mBinding!!.model = mUserViewModel
        setUpView()
        getCurrentUser()
        mBinding!!.bio.setShowingLine(2)
        mBinding!!.bio.addShowMoreText(getString(R.string.readmore))
        mBinding!!.bio.addShowLessText(getString(R.string.readless))
        mBinding!!.bio.text = mUserViewModel!!.bioData
        mBinding!!.bio.setShowLessTextColor(ContextCompat.getColor(this, R.color.readmorecolor))
        mBinding!!.bio.setShowMoreColor(ContextCompat.getColor(this, R.color.readmorecolor))

    }

    private fun getIntentData() {
        if (intent.extras != null) {
            val bundle = intent.extras
            val user = bundle!!.getParcelable<TutorData>(AppConstants.KEY_DATA)
            mUserViewModel = TutorViewModel(user!!)
        }
    }


    private fun getCurrentUser() {
        firebaseStore.collection(getString(com.autohub.skln.R.string.db_root_students)).document(appPreferenceHelper.getuserID()).get()
                .addOnSuccessListener { documentSnapshot -> mCurrentUser = documentSnapshot.toObject(UserModel::class.java) }
                .addOnFailureListener { e -> showSnackError(e.message) }
    }

    private fun setUpView() {
        addSubjectRadioButtons(mUserViewModel!!.user.subjectsToTeach!!.split(","))
//        mUserViewModel!!.user.academicInfo!!.classType?.split(",")?.let { }

        addClassTypeRadioButtons(mUserViewModel!!.user.qualification!!.classType)


        mBinding!!.txtdistence.text = mUserViewModel!!.user.distance.toString()

        try {
            if (mUserViewModel!!.user.personInfo!!.accountPicture != null && !mUserViewModel!!.user.personInfo!!.accountPicture.equals("")) {
                val pathReference1 = FirebaseStorage.getInstance().reference.child(mUserViewModel!!.user.personInfo!!.accountPicture!!)
                GlideApp.with(this)
                        .load(pathReference1)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(com.autohub.skln.R.drawable.default_pic)
                        .fallback(com.autohub.skln.R.drawable.default_pic)
                        .into(mBinding!!.profilePicture)
            }
        } catch (e: Exception) {
        }


    }

    private fun makeRequest(subject: String, classtype: String) {
        showLoading()
        val studentId = firebaseAuth.currentUser!!.uid
        val tutorId = mUserViewModel!!.userId
        val gradeId = mCurrentUser!!.academicInfo!!.selectedClass
        var grade = ""
        var subjectId = ""
        firebaseStore.collection("grades")
                .whereEqualTo("id", gradeId).get().addOnSuccessListener {

                    it.forEach {
                        grade = "class_${it.getString("grade")!!} "

                    }

                    firebaseStore.collection("subjects")
                            .whereEqualTo("name", subject).get().addOnSuccessListener {

                                it.forEach {
                                    subjectId = "${it.getString("id")!!} "

                                }


                                var batchRequest = BatchRequestModel(id = "", status = "pending",
                                        teacher = BatchRequestModel.Teacher(tutorId, mUserViewModel!!.fullName),
                                        student = BatchRequestModel.Student(studentId, mCurrentUser!!.personInfo!!.firstName!! + " " +
                                                mCurrentUser!!.personInfo!!.lastName!!),
                                        grade = BatchRequestModel.Grade(id = gradeId

                                                , name = grade),
                                        subject = BatchRequestModel.Subject(id = subjectId,
                                                name = subject)
                                )


                                makeBatchRequest(batchRequest)
                            }
                }
                .addOnFailureListener { e -> showSnackError(e.message) }


    }

    private fun makeBatchRequest(batchRequest: BatchRequestModel) {


        /*       val request = Request(batchRequest.student!!.id,
                       batchRequest.teacher!!.id, batchRequest., mUserViewModel!!.firstName, mCurrentUser!!.personInfo!!.firstName,
                       mCurrentUser!!.academicInfo!!.selectedClass, classtype)*////* mUserViewModel!!.classType*/
        val dbRoot = getString(R.string.db_root_batchRequests)
        firebaseStore.collection(dbRoot).add(batchRequest).addOnSuccessListener { documentReference ->
            Log.d(">>>>Request", "DocumentSnapshot added with ID: " + documentReference.id)
            hideLoading()
            val intent = Intent()
            val bundle = Bundle()
            bundle.putParcelable(AppConstants.KEY_DATA, BatchRequestViewModel(batchRequest, "Student", documentReference.id))
            intent.putExtra(AppConstants.KEY_DATA, bundle)
            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }.addOnFailureListener { hideLoading() }
    }

    private fun addSubjectRadioButtons(subjects: List<String>) {
        if (subjects.isEmpty()) {
            val rdbtn = RadioButton(this)
            rdbtn.id = View.generateViewId()
            //rdbtn.text = "Radio " + rdbtn.id
            rdbtn.text = mUserViewModel!!.user.subjectsToTeach
            mBinding!!.classtyperadio.addView(rdbtn)
        }

        for (element in subjects) {
            val rdbtn = RadioButton(this)
            rdbtn.id = View.generateViewId()
            //rdbtn.text = "Radio " + rdbtn.id
            rdbtn.text = element
            mBinding!!.subjectradio.addView(rdbtn)
        }
    }

    private fun addClassTypeRadioButtons(classtype: ArrayList<String>?) {
        if (classtype!!.size < 0) {
            /*   val rdbtn = RadioButton(this)
               rdbtn.id = View.generateViewId()
               //rdbtn.text = "Radio " + rdbtn.id
               rdbtn.text = mUserViewModel!!.user.academicInfo!!.classType
               mBinding!!.classtyperadio.addView(rdbtn)*/
        }

        for (element in classtype) {
            val rdbtn = RadioButton(this)
            rdbtn.id = View.generateViewId()
            //rdbtn.text = "Radio " + rdbtn.id
            rdbtn.text = element
            mBinding!!.classtyperadio.addView(rdbtn)
        }
    }

    fun onBackClick() {
        finish()
    }

    fun onRequestClick() {

        when {
            mBinding!!.subjectradio.checkedRadioButtonId == -1 -> showSnackError("Please select a subject")
            mBinding!!.classtyperadio.checkedRadioButtonId == -1 -> showSnackError("Please select a class type")
            else -> {
                var selectedsubjectRadio = (findViewById(mBinding!!.subjectradio.checkedRadioButtonId)) as RadioButton
                println("==================== Selected subject" + selectedsubjectRadio.text.toString())

                var selectedclasstypeRadio = (findViewById(mBinding!!.classtyperadio.checkedRadioButtonId)) as RadioButton
                println("==================== Selected subject" + selectedclasstypeRadio.text.toString())
                makeRequest(selectedsubjectRadio.text.toString(), selectedclasstypeRadio.text.toString())


            }
        }
    }
}

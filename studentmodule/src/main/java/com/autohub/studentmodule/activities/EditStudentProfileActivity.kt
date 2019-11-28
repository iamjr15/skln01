package com.autohub.studentmodule.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.OpenableColumns
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.autohub.skln.BaseActivity
import com.autohub.skln.models.UserModel
import com.autohub.skln.models.batchRequests.SubjectData
import com.autohub.skln.utills.ActivityUtils
import com.autohub.skln.utills.AppConstants.*
import com.autohub.skln.utills.CommonUtils
import com.autohub.skln.utills.GlideApp
import com.autohub.studentmodule.R
import com.autohub.studentmodule.databinding.ActivityEditStudentProfileBinding
import com.autohub.studentmodule.models.StudentSubjectsModel
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.storage.FirebaseStorage
import com.kbeanie.multipicker.api.CacheLocation
import com.kbeanie.multipicker.api.CameraImagePicker
import com.kbeanie.multipicker.api.ImagePicker
import com.kbeanie.multipicker.api.Picker
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback
import com.kbeanie.multipicker.api.entity.ChosenImage
import io.fabric.sdk.android.services.common.CommonUtils
import java.io.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Vt Netzwelt
 */

class EditStudentProfileActivity : BaseActivity(), ImagePickerCallback {


    private lateinit var cameraPicker: CameraImagePicker
    private lateinit var imagePicker: ImagePicker
    private var isTakePicture: Boolean = false
    private var isContent: Boolean = false

    private lateinit var subjectDataList: ArrayList<SubjectData>
    private lateinit var studentSubjectsDataList: ArrayList<StudentSubjectsModel>
    private var favleastsubjectsDataList: ArrayList<SubjectData> = ArrayList()
    private var favtselectedId = ""
    private var leastselectedId = ""
    private var selectedHobbiesid = ""
    private var isSeniorSelected = true
    private var PermissionsRequest: Int = 12

    private var imageuri: Uri? = null

    private var imageURL = ""

    private var mBinding: ActivityEditStudentProfileBinding? = null

    private var user: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_student_profile)
        mBinding!!.callback = this

        fetchSubjects()
    }

    /*
    * fetch all subjects (1-10, 11-12 and hobbies)
    *
    * */
    private fun fetchSubjects() {
        showLoading()

        subjectDataList = arrayListOf()
        firebaseStore.collection("subjects").get().addOnCompleteListener {

            if (it.isSuccessful) {
                for (document in it.result!!) {
                    val user = document.toObject(SubjectData::class.java)
                    subjectDataList.add(user)
                }
            } else {
                hideLoading()
            }

            fetchStudentSubjects()


            setleastFavSubjects(seniorClass = true, isFirst = true)


        }.addOnFailureListener {
            showSnackError(it.message)
            hideLoading()
        }
    }


    /*
    * Fetch all Student Selected Subjects(Favt, LeastFavt, Hobbies)
    * Filter All Subjects with Student Selected subjects and showing on UI
    *
    * */
    private fun fetchStudentSubjects() {


        studentSubjectsDataList = arrayListOf()
        firebaseStore.collection("studentSubjects").whereEqualTo("studentId", firebaseAuth.currentUser!!.uid)
                .get().addOnCompleteListener {

                    hideLoading()

                    if (it.isSuccessful) {

                        val idsofSubjectsList = subjectDataList.map { it.id }
                        val idsofFavtSubjectsList = favleastsubjectsDataList.map { it.id }
                        var idsofLestFavtSubjectsList = favleastsubjectsDataList.map { it.id }


                        val hobbiesBuilder = StringBuilder()
                        val hobbiesIdBuilder = StringBuilder()

                        val favsujectsbuilder = StringBuilder()
                        val favsujectsidsbuilder = StringBuilder()

                        val leastsujectsbuilder = StringBuilder()
                        val leastsujectsidsbuilder = StringBuilder()

                        for (document in it.result!!) {
                            val studentSubjectsModel = document.toObject(StudentSubjectsModel::class.java)
                            studentSubjectsDataList.add(studentSubjectsModel)

                            var subjectId = studentSubjectsModel.subjectId



                            if (studentSubjectsModel.isHobby!!) {

                                if (idsofSubjectsList.contains(subjectId!!.trim())) {
                                    val index = idsofSubjectsList.indexOf(subjectId.trim())
                                    subjectDataList[index].isHobbySelected = true
                                    hobbiesBuilder.append(", " + subjectDataList[index].name)
                                    hobbiesIdBuilder.append("," + subjectDataList[index].id)
                                }


                            } else if (studentSubjectsModel.isFavourite!!) {

                                if (idsofFavtSubjectsList.contains(subjectId!!.trim())) {
                                    val index = idsofFavtSubjectsList.indexOf(subjectId.trim())
                                    favleastsubjectsDataList[index].isFavSelected = true
                                    favsujectsbuilder.append(", " + favleastsubjectsDataList[index].name)
                                    favsujectsidsbuilder.append("," + favleastsubjectsDataList[index].id)
                                }


                            } else if (studentSubjectsModel.isLeastFavourite!!) {
                                if (idsofLestFavtSubjectsList.contains(subjectId!!.trim())) {
                                    val index = idsofLestFavtSubjectsList.indexOf(subjectId.trim())
                                    favleastsubjectsDataList[index].isleastelected = true
                                    leastsujectsbuilder.append(", " + favleastsubjectsDataList[index].name)
                                    leastsujectsidsbuilder.append("," + favleastsubjectsDataList[index].id)
                                }


                            }


                        }

                        if (hobbiesIdBuilder.isNotBlank()) {
                            selectedHobbiesid = hobbiesIdBuilder.toString().removeRange(0..0)

                        }
                        if (hobbiesBuilder.isNotBlank()) {
                            mBinding!!.favHobby.text = hobbiesBuilder.toString().removeRange(0..0)

                        }


                        if (favsujectsidsbuilder.isNotBlank()) {
                            favtselectedId = favsujectsidsbuilder.toString().removeRange(0..0)

                        }
                        if (favsujectsbuilder.isNotBlank()) {
                            mBinding!!.favoriteSubj.text = favsujectsbuilder.toString().removeRange(0..0)

                        }


                        if (leastsujectsidsbuilder.isNotBlank()) {
                            leastselectedId = leastsujectsidsbuilder.toString().removeRange(0..0)

                        }
                        if (leastsujectsbuilder.isNotBlank()) {
                            mBinding!!.leastFavuSubj.text = leastsujectsbuilder.toString().removeRange(0..0)

                        }
                    }



                    setUpUserInfo()


                }.addOnFailureListener {
                    showSnackError(it.message)
                    hideLoading()

                }

    }

    fun onBackClick() {
        val intent = Intent()
        val bundle = Bundle()
        intent.putExtra("imageUrl", imageURL)
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


    fun showHidePass() {
        if (mBinding!!.showHidePassword.text.toString().equals("show", ignoreCase = true)) {
            mBinding!!.password.transformationMethod = null
            mBinding!!.showHidePassword.text = getString(R.string.hide)
        } else {
            mBinding!!.password.transformationMethod = PasswordTransformationMethod()
            mBinding!!.showHidePassword.text = getString(R.string.showpas)
        }
    }


    /*
    * show hobbies Dialog with already selected hobbies
    *
    * */

    fun showHobby() {
        val items = ArrayList<String>()


        for (subjecdata in subjectDataList) {
            if (subjecdata.grades!!.hobbies) {
                items.add(subjecdata.name!!)
            }

        }

        val namesArr = items.toTypedArray()
        val booleans = BooleanArray(items.size)
        val selectedItems: MutableList<String> = ArrayList()

        if (selectedHobbiesid != "") {

            for (subjectData in subjectDataList) {
                if (subjectData.isHobbySelected!!) {
                    subjectData.name.let { selectedItems.add(it) }
                }
            }
        }


        val selectedHobbies = ArrayList<String>()
        for (i in selectedItems.indices) {
            if (items.contains(selectedItems[i])) {
                booleans[items.indexOf(selectedItems[i])] = true
                selectedHobbies.add(selectedItems[i])
            }
        }


        AlertDialog.Builder(this)
                .setTitle(getString(R.string.chossesyourHobby_title))
                .setMultiChoiceItems(namesArr, booleans) { _, i, b ->
                    if (b) {
                        selectedHobbies.add(items[i])
                    } else {
                        selectedHobbies.remove(items[i])
                    }
                }
                .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                    dialog.dismiss()

                    for (i in subjectDataList.indices) {
                        subjectDataList[i].isHobbySelected = false
                    }

                    val hobbiesBuilder = StringBuilder()
                    val hobbiesIdBuilder = StringBuilder()
                    for (i in selectedHobbies) {
                        val idsList = subjectDataList.map { it.name }
                        val index = idsList.indexOf(i.trim())
                        subjectDataList[index].isHobbySelected = true
                        hobbiesBuilder.append(", " + subjectDataList[index].name)
                        hobbiesIdBuilder.append("," + subjectDataList[index].id)
                    }

                    if (selectedHobbies.size > 0) {
                        selectedHobbiesid = hobbiesIdBuilder.toString().removeRange(0..0)
                        mBinding!!.favHobby.text = hobbiesBuilder.toString().removeRange(0..0)
                    } else {
                        selectedHobbiesid = ""
                        mBinding!!.favHobby.text = ""
                    }


                }
                .show()
    }

    /*
       * show grades Dialog with already selected grade
       *
       * */
    fun showGrade() {
        val namesArr = arrayOfNulls<String>(12)
        var indexSelected = -1
        for (i in 0..11) {
            namesArr[i] = (i + 1).toString() + CommonUtils.getClassSuffix(i + 1) + " Grade"
            if (user!!.academicInfo!!.selectedGrad.equals((i + 1).toString(), ignoreCase = true)) {
                indexSelected = i
            }
        }

        AlertDialog.Builder(this)
                .setSingleChoiceItems(namesArr, indexSelected, null)
                .setTitle(getString(R.string.selectGrade_title))
                .setPositiveButton(R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                    var selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                    if (selectedPosition < 0) {
                        selectedPosition = 0
                    }
                    mBinding!!.grade.text = namesArr[selectedPosition]
                    val grade = (selectedPosition + 1).toString()
                    user!!.academicInfo!!.selectedGrad = grade
                    val isSeniorClass = grade.equals(CLASS_11, ignoreCase = true)
                            || grade.equals(CLASS_12, ignoreCase = true)
                    if (isSeniorSelected.compareTo(isSeniorClass) != 0) {
                        favtselectedId = ""
                        leastselectedId = ""
                        mBinding!!.leastFavuSubj.text = ""
                        mBinding!!.favoriteSubj.text = ""
                        setleastFavSubjects(isSeniorClass)
                    }

                }
                .show()
    }

    /*
       * show least/fav subject Dialog with already selected subjects
       *
       * */
    fun showSub(isLeastFav: Boolean) {
        val items = ArrayList<String>()
        for (i in favleastsubjectsDataList) {
            items.add(i.name!!)
        }

        val namesArr = items.toTypedArray()
        val booleans = BooleanArray(items.size)
        val selectedItems: MutableList<String> = ArrayList()
        if (favtselectedId != "" && !isLeastFav) {
            for (favtdata in favleastsubjectsDataList) {
                if (favtdata.isFavSelected!!) {
                    favtdata.name.let { selectedItems.add(it) }
                }
            }
        }


        if (leastselectedId != "" && isLeastFav) {
            for (leastfav in favleastsubjectsDataList) {
                if (leastfav.isleastelected!!) {
                    leastfav.name.let { selectedItems.add(it) }
                }
            }
        }
        val selectedSub = ArrayList<String>()

        for (i in selectedItems.indices) {
            if (items.contains(selectedItems[i])) {
                booleans[items.indexOf(selectedItems[i])] = true
                selectedSub.add(selectedItems[i])
            }
        }


        val title: String = if (!isLeastFav) {
            getString(R.string.chooseFavtsubj_title)
        } else {
            getString(R.string.chossleastfavtsub_title)
        }
        AlertDialog.Builder(this)
                .setMultiChoiceItems(namesArr, booleans
                ) { _, i, b ->
                    if (b) {
                        selectedSub.add(items[i])

                    } else {
                        selectedSub.remove(items[i])
                    }
                }
                .setTitle(title)
                .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                    dialog.dismiss()
                    if (isLeastFav) {

                        for (i in favleastsubjectsDataList.indices) {
                            favleastsubjectsDataList[i].isleastelected = false
                        }
                        val leastsujectsbuilder = StringBuilder()
                        val leastsujectsidsbuilder = StringBuilder()

                        for (i in selectedSub) {
                            val idsList = favleastsubjectsDataList.map { it.name }
                            val index = idsList.indexOf(i.trim())
                            favleastsubjectsDataList[index].isleastelected = true
                            leastsujectsbuilder.append(", " + favleastsubjectsDataList[index].name)
                            leastsujectsidsbuilder.append("," + favleastsubjectsDataList[index].id)
                        }
                        if (selectedSub.size > 0) {
                            leastselectedId = leastsujectsidsbuilder.toString().removeRange(0..0)
                            mBinding!!.leastFavuSubj.text = leastsujectsbuilder.toString().removeRange(0..0)
                        } else {
                            leastselectedId = ""
                            mBinding!!.leastFavuSubj.text = ""
                        }


                    } else {
                        for (i in favleastsubjectsDataList.indices) {
                            favleastsubjectsDataList[i].isFavSelected = false
                        }
                        val favsujectsbuilder = StringBuilder()
                        val favsujectsidsbuilder = StringBuilder()
                        for (i in selectedSub) {
                            val idsList = favleastsubjectsDataList.map { it.name }
                            val index = idsList.indexOf(i.trim())
                            favleastsubjectsDataList[index].isFavSelected = true
                            favsujectsbuilder.append(", " + favleastsubjectsDataList[index].name)
                            favsujectsidsbuilder.append("," + favleastsubjectsDataList[index].id)
                        }


                        if (selectedSub.size > 0) {
                            favtselectedId = favsujectsidsbuilder.toString().removeRange(0..0)
                            mBinding!!.favoriteSubj.text = favsujectsbuilder.toString().removeRange(0..0)
                        } else {
                            favtselectedId = ""
                            mBinding!!.favoriteSubj.text = ""
                        }


                    }
                }
                .show()
    }


    /*
    *
    * update favleastsubjectsDataList as per grade selection (1-10 or 11-12)
    * */

    private fun setleastFavSubjects(seniorClass: Boolean, isFirst: Boolean = false) {
        isSeniorSelected = seniorClass
        favleastsubjectsDataList.clear()
        val items = ArrayList<String>()
        items.add(SUBJECT_ENGLISH)//
        items.add(SUBJECT_MATHS)//
        items.add(SUBJECT_COMPUTER_SCIENCE)
        if (seniorClass) {
            items.add(SUBJECT_ACCOUNTANCY)
            items.add(SUBJECT_BIOLOGY)
            items.add(SUBJECT_BUSINESS)
            items.add(SUBJECT_CHEMISTRY)
            items.add(SUBJECT_ECONOMICS)
            items.add(SUBJECT_PHYSICS)
        } else {
            items.add(SUBJECT_SOCIAL_STUDIES)
            items.add(SUBJECT_LANGUAGES)
            items.add(SUBJECT_SCIENCE)
        }

        if (isFirst) {
            items.add(SUBJECT_SOCIAL_STUDIES)
            items.add(SUBJECT_LANGUAGES)
            items.add(SUBJECT_SCIENCE)
        }

        for (i in subjectDataList.indices) {
            if (items.contains(subjectDataList[i].name)) {
                favleastsubjectsDataList.add(subjectDataList[i])
            }
        }

    }

    /*
    * show userProfile pic
    * */
    private fun setUserProfileImage(user: String?) {
        if (user != null && user != "") {
            try {
                GlideApp.with(this)
                        .load(user)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .placeholder(com.autohub.skln.R.drawable.default_pic)
                        .into(mBinding!!.profilePicture)

            } catch (e: Exception) {
            }


        }

    }

    /*
    * show all user deatils on Ui
    *
    * */
    private fun setUpUserInfo() {

        firebaseStore.collection(getString(com.autohub.skln.R.string.db_root_students)).document(appPreferenceHelper.getuserID()).get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(UserModel::class.java)
                    this.user = user
                    user!!.id = documentSnapshot.id
                    mBinding!!.edtFirstName.setText(user.personInfo!!.firstName)
                    mBinding!!.etPhoneNumber.setText(user.personInfo!!.phoneNumber)

                    setUserProfileImage(user.personInfo!!.accountPicture)


                    firebaseStore.collection(getString(R.string.db_root_grades)).whereEqualTo(KEY_ID, user.academicInfo!!.selectedGrad!!).get().addOnSuccessListener {

                        it.forEach {
                            mBinding!!.grade.text = "grade ${it.getString(KEY_GRADE)!!}${CommonUtils.getClassSuffix(it.getString(KEY_GRADE)!!.toInt())} "
                            mBinding!!.grade.text = CommonUtils.getGrade(Integer.parseInt(it.getString("grade")!!.trim { it <= ' ' }))
                            this.user!!.academicInfo!!.selectedGrad = it.getString("grade")
                            val isSeniorClass = it.getString(KEY_GRADE)!!.equals(CLASS_11, ignoreCase = true) || it.getString(KEY_GRADE)!!.equals(CLASS_12, ignoreCase = true)
                            if (isSeniorSelected.compareTo(isSeniorClass) != 0) setleastFavSubjects(isSeniorClass)

                        }
                    }

                    mBinding!!.codePicker.isClickable = false
                    mBinding!!.codePicker.isFocusable = false
                    mBinding!!.codePicker.isEnabled = false
                    mBinding!!.codePicker.registerCarrierNumberEditText(mBinding!!.etPhoneNumber)
                    mBinding!!.codePicker.fullNumber = user.personInfo!!.phoneNumber
                }
                .addOnFailureListener { e -> showSnackError(e.message) }
    }

    fun makeSaveRequest() {
        if (isVerified()) {
            showLoading()

            if (imageuri != null) {
                if (isTakePicture || !isContent) {
                    uploadImage(imageuri!!)
                } else {
                    uploadGalleryImage(imageuri!!)


                }
            } else {
                updateContentData()
            }
        }
    }


    private fun updateContentData() {

        if (mBinding!!.password.text.toString() != "") {
            firebaseAuth.currentUser!!.updatePassword(mBinding!!.password.text.toString()).addOnCompleteListener {
                Toast.makeText(this,
                        getString(R.string.passwordchangemessage), Toast.LENGTH_SHORT).show()
            }
        }

        saveDetailsinFireBase()

    }

    /*
    *
    * 0 favt, 1 lestfav, 2 hobbies
    *
    * */
    private fun saveDetailsinFireBase() {

        deleteOldStudentSubject()
        Handler().postDelayed({
            createBatchesForSubject(favtselectedId.split(","), 0)
            createBatchesForSubject(leastselectedId.split(","), 1)
            createBatchesForSubject(selectedHobbiesid.split(","), 2)

            firebaseStore.collection(getString(R.string.db_root_grades)).whereEqualTo(KEY_GRADE, user!!.academicInfo!!.selectedGrad).get().addOnSuccessListener {
                it.forEach {
                    saveData(it.getString(KEY_ID)!!)
                }
            }
        }, 1000)


    }

    /*
    * Delete all studentSubjects
    * * */
    private fun deleteOldStudentSubject() {
        firebaseStore.collection(getString(R.string.db_root_studentSubjects))
                .whereEqualTo(KEY_STUDENTID, firebaseAuth.currentUser!!.uid).get()
                .addOnSuccessListener {
                    it.forEach {
                        firebaseStore.collection(getString(R.string.db_root_studentSubjects)).document(it.id).delete()
                    }
                }
                .addOnFailureListener {

                }
    }


    /*
    *
    * save user personal and Academics info in DataBase
    *
    * */
    private fun saveData(selectedGradeId: String) {

        val userpersonalinfo = HashMap<String, Any>()
        /* if (mBinding!!.edtFirstName.text.toString().split(" ").size > 1) {
             userpersonalinfo[KEY_FIRST_NAME] = mBinding!!.edtFirstName.text.toString().split(" ")[0]
             userpersonalinfo[KEY_LAST_NAME] = mBinding!!.edtFirstName.text.toString().split(" ")[1]
         } else {*/
        userpersonalinfo[KEY_FIRST_NAME] = mBinding!!.edtFirstName.text.toString().trim()
        // }
/*
* Need to create seprate module for Phone number editing.
* And also need to link this changed values with old firebase User Id
*
* */


        // user.put(AppConstants.KEY_PASSWORD, encryptedPassword())
        // user[AppConstants.KEY_PHONE_NUMBER] = mBinding!!.codePicker.fullNumberWithPlus

        val useracadmicinfo = HashMap<String, Any>()

        if (mBinding!!.password.text.toString() != "") {

            useracadmicinfo[KEY_PASSWORD] = getString(mBinding!!.password.text)

        }

//        useracadmicinfo[KEY_STDT_LEAST_FAV_CLASSES] = leastselectedId.split(",")


//        useracadmicinfo[KEY_STDT_FAVORITE_CLASSES] = favtselectedId.split(",")
        useracadmicinfo[KEY_SELECTED_CLASS] = selectedGradeId
//        useracadmicinfo[KEY_STDT_HOBBIES] = selectedHobbiesid.split(",")
        val dbRoot = getString(com.autohub.skln.R.string.db_root_students)





        firebaseStore.collection(dbRoot).document(appPreferenceHelper.getuserID()).set(
                mapOf(
                        KEY_PERSONALINFO to userpersonalinfo,
                        KEY_ACADEMICINFO to useracadmicinfo
                )
                , SetOptions.merge())
                .addOnSuccessListener {
                    hideLoading()
                    Toast.makeText(this,
                            getString(R.string.profileupdatesuccessfully_msg), Toast.LENGTH_SHORT).show()

                    ActivityUtils.launchActivity(this, StudentHomeActivity::class.java)
                    finishAffinity()

                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }

    }
/*
* value = 0 favt, value = 1 lestfav, value = 2 hobbies
* Save all selected subjects in studentSubject Collection
*
* */

    private fun createBatchesForSubject(selectedSubjects: List<String>, value: Int) {


        var batch: WriteBatch = firebaseStore.batch()
        for (i in selectedSubjects) {
            var name = ""
            var idsList = subjectDataList.map { it.id }
            if (idsList.contains(i)) {
                val index = idsList.indexOf(i)
                name = subjectDataList[index].name!!
            }


            var map: HashMap<String, Any> = HashMap()


            when (value) {
                0 -> {
                    map["category"] = "favorite"
                    map["isFavourite"] = true
                    map["isLeastFavourite"] = false
                    map["isHobby"] = false
                }
                1 -> {
                    map["category"] = "leastfavorite"
                    map["isFavourite"] = false
                    map["isLeastFavourite"] = true
                    map["isHobby"] = false
                }
                else -> {
                    map["category"] = "hobby"
                    map["isFavourite"] = false
                    map["isLeastFavourite"] = false
                    map["isHobby"] = true

                }
            }

            map["id"] = firebaseAuth.currentUser!!.uid + "_" + i
            map["studentId"] = firebaseAuth.currentUser!!.uid
            map["subjectId"] = i
            map["subjectName"] = name
            map["subjectQuestion"] = ""
            map["answer"] = ""

            val nycRef = firebaseStore.collection("studentSubjects").document()
            batch.set(nycRef, map)

        }

        batch.commit().addOnCompleteListener {


        }.addOnFailureListener {

            showSnackError(it.message)
        }
    }

    /*
    * Fields verification
    *
    * */
    private fun isVerified(): Boolean {
        if (mBinding!!.edtFirstName.text.isEmpty() || mBinding!!.edtFirstName.text.toString().trim().length < 2) {

            showSnackError(resources.getString(R.string.enter_name))
            mBinding!!.edtFirstName.requestFocus()
            return false
        } /*else if (password == null || password.isEmpty()) {
            showSnackError(resources.getString(R.string.enter_password))
            mBinding!!.password.requestFocus()
            return false

        }*/
        else if (mBinding!!.password.text.toString() != "") {
            if (mBinding!!.password.text.toString().length < 6) {
                showSnackError(getString(R.string.weakPass))
                return false

            }
        } else if (!mBinding!!.codePicker.isValidFullNumber) {
            showSnackError(resources.getString(R.string.enter_valid_number))
            return false

        } else if (mBinding!!.leastFavuSubj.text.toString() == "") {
            showSnackError(getString(R.string.leastfavt_validation))

            return false

        } else if (mBinding!!.favoriteSubj.text.toString() == "") {
            showSnackError(getString(R.string.favt_validation))

            return false

        } else if (mBinding!!.favHobby.text.toString() == "") {
            showSnackError(getString(R.string.selecthobbie_validation))
            return false
        }
        return true

    }

    fun onAddPicture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                        PermissionsRequest)
            }
        } else {
//            addPicture()

            onpenImagePickerDialog()

        }
    }

    private fun onpenImagePickerDialog() {
        val mBuilder = androidx.appcompat.app.AlertDialog.Builder(this)

        val options = arrayOf("Gallery", "Camera")
        mBuilder.setSingleChoiceItems(options, 0, null)
        mBuilder.setTitle("Choose from")
                .setPositiveButton("Ok") { dialog, which ->

                    if ((dialog as AlertDialog).listView.checkedItemPosition == 0) {
                        if (ActivityCompat.checkSelfPermission(this,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 200)
                        } else {
                            pickImageSingle()
                        }
                    } else {

                        if (ActivityCompat.checkSelfPermission(this,
                                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this,
                                    arrayOf(Manifest.permission.CAMERA), 100)

                        } else {
                            takePicture()
                        }

                    }

                }

        val mDialog = mBuilder.create()
        mDialog.show()

    }

    private fun takePicture() {
        isTakePicture = true
        cameraPicker = CameraImagePicker(this)
        cameraPicker.setDebugglable(true)
        cameraPicker.setCacheLocation(CacheLocation.EXTERNAL_STORAGE_APP_DIR)
        cameraPicker.setImagePickerCallback(this)
        cameraPicker.shouldGenerateMetadata(true)
        cameraPicker.shouldGenerateThumbnails(true)
        cameraPicker.pickImage()
    }

    private fun pickImageSingle() {
        isTakePicture = false
        imagePicker = ImagePicker(this)
        imagePicker.setFolderName("Random")
        imagePicker.setRequestId(1234)
        //imagePicker.ensureMaxSize(500, 500)
        imagePicker.shouldGenerateMetadata(true)
        imagePicker.shouldGenerateThumbnails(true)
        imagePicker.setImagePickerCallback(this)
        val bundle = Bundle()
        bundle.putInt("android.intent.extras.CAMERA_FACING", 1)
        imagePicker.pickImage()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Picker.PICK_IMAGE_DEVICE) run {
            if (imagePicker == null) {
                imagePicker = ImagePicker(this)
                imagePicker.setImagePickerCallback(this)
            }
            imagePicker.submit(data)
            if (data == null) {

            }

        } else if (requestCode == Picker.PICK_IMAGE_CAMERA) run {
            if (cameraPicker == null) {
                cameraPicker = CameraImagePicker(this)
                cameraPicker.setImagePickerCallback(this)
            }
            cameraPicker.submit(data)
        }

    }


    override fun onImagesChosen(list: MutableList<ChosenImage>?) {
        val chosenImage = list!![0]

        if (chosenImage.originalPath.contains("content:")) {
            isContent = true

            if (isTakePicture) {
                imageuri = Uri.fromFile(File(chosenImage.originalPath))

            } else {
                imageuri = Uri.parse(chosenImage.originalPath)

            }
        } else {
            isContent = false
            isTakePicture = true
            imageuri = Uri.fromFile(File(chosenImage.originalPath))

        }
        GlideApp.with(this)
                .load(imageuri)
                .placeholder(com.autohub.skln.R.drawable.default_pic)
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // disable caching of glide
                .skipMemoryCache(true)
                .into(mBinding!!.profilePicture)


    }


    override fun onError(p0: String?) {
    }


    /*
    * Show Dialog for adding pic from Camera/Gallery
    * */
    private fun addPicture() {
        /* TedBottomPicker.with(this)
                 .show { uri ->
                     GlideApp.with(this)
                             .load(uri)
                             .placeholder(com.autohub.skln.R.drawable.default_pic)
                             .diskCacheStrategy(DiskCacheStrategy.NONE)  // disable caching of glide
                             .skipMemoryCache(true)
                             .into(mBinding!!.profilePicture)

                     imageuri = uri

                 }*/

    }


    /*
    * Upload user image on Firebase Storage and get the Download URL
    * */
    private fun uploadImage(uri: Uri) {
        println("====================== URI" + uri.path)
        val file = File(uri.path!!)
        val size = file.length().toInt()
        val bytes = ByteArray(size)
        try {
            val buf = BufferedInputStream(FileInputStream(file))
            buf.read(bytes, 0, bytes.size)
            buf.close()
            val path = "student/"
            val pathString = path + firebaseAuth.currentUser!!.uid + ".jpg"
            val picRef = FirebaseStorage.getInstance().reference.child(pathString)
            val uploadTask = picRef.putBytes(bytes)
            uploadTask.addOnSuccessListener {
                picRef.downloadUrl.addOnSuccessListener {
                    var profilePictureUri = it.toString()
                    saveUserPhotoOnFirestore(profilePictureUri)
                }

            }.addOnFailureListener { e ->
                hideLoading()
                Toast.makeText(this, "Upload Failed -> $e", Toast.LENGTH_LONG).show()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            hideLoading()
        } catch (e: IOException) {
            e.printStackTrace()
            hideLoading()
        }

    }


    /*
    *
    * Save userImage download URL in user personal info
    *
    * */
    private fun saveUserPhotoOnFirestore(pathString: String) {

        val root = getString(R.string.db_root_students)
        imageURL = pathString



        firebaseStore.collection(root).document(appPreferenceHelper.getuserID()).set(
                mapOf(
                        KEY_PERSONALINFO to mapOf(KEY_ACCOUNT_PICTURE to pathString)
                )
                , SetOptions.merge())
                .addOnSuccessListener {
                    updateContentData()
                }
                .addOnFailureListener { hideLoading() }
    }


    private fun uploadGalleryImage(uri: Uri) {
        var size = 0

        uri.let { returnUri ->
            contentResolver.query(returnUri, null, null, null, null)
        }.use { cursor ->
            size = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()
        }

        try {
            var inputStream = contentResolver.openInputStream(uri)
            val path = "student/"
            val pathString = path + firebaseAuth.currentUser!!.uid + ".jpg"
            val picRef = FirebaseStorage.getInstance().reference.child(pathString)
            val uploadTask = picRef.putStream(inputStream!!)
            uploadTask.addOnSuccessListener {

                picRef.downloadUrl.addOnSuccessListener {
                    var profilePictureUri = it.toString()
                    saveUserPhotoOnFirestore(profilePictureUri)
                }
            }.addOnFailureListener { e ->
                hideLoading()
                Toast.makeText(this, "Upload Failed -> $e", Toast.LENGTH_LONG).show()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            hideLoading()
        } catch (e: IOException) {
            e.printStackTrace()
            hideLoading()
        }

    }

}

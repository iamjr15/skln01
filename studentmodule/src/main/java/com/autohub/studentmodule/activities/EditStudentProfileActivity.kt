package com.autohub.studentmodule.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.autohub.skln.BaseActivity
import com.autohub.skln.CropActivity
import com.autohub.skln.models.UserModel
import com.autohub.skln.models.batchRequests.SubjectData
import com.autohub.skln.utills.ActivityUtils
import com.autohub.skln.utills.AppConstants
import com.autohub.skln.utills.AppConstants.*
import com.autohub.skln.utills.CommonUtils
import com.autohub.skln.utills.GlideApp
import com.autohub.studentmodule.R
import com.autohub.studentmodule.databinding.ActivityEditStudentProfileBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.storage.FirebaseStorage
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import java.io.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Vt Netzwelt
 */

class EditStudentProfileActivity : BaseActivity() {
    lateinit var subjectDataList: ArrayList<SubjectData>
    private var favleastsubjectsDataList: ArrayList<SubjectData> = ArrayList()
    private var favtselectedId = ""
    private var leastselectedId = ""
    private var selectedHobbiesid = ""
    private var isSeniorSelected = true

    var imageURL = ""

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
        subjectDataList = arrayListOf()
        firebaseStore.collection(getString(R.string.db_root_subjects)).get().addOnCompleteListener {

            if (it.isSuccessful) {
                for (document in it.result!!) {
                    val user = document.toObject(SubjectData::class.java)
                    subjectDataList.add(user)
                }
            }
            setleastFavSubjects(true, true)
            setUpUserInfo()


        }.addOnFailureListener {
            showSnackError(it.message)
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
        var selectedItems: MutableList<String> = ArrayList()

        if (user!!.academicInfo!!.hobbies != null && user!!.academicInfo!!.hobbies!!.isNotEmpty()) {

            for (subjectData in subjectDataList) {
                if (subjectData.isHobbySelected!!) {
                    subjectData.name?.let { selectedItems.add(it) }
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

                    var hobbiesBuilder = StringBuilder()
                    var hobbiesIdBuilder = StringBuilder()
                    for (i in selectedHobbies) {
                        var idsList = subjectDataList.map { it.name }
                        val index = idsList.indexOf(i.trim())
                        subjectDataList[index].isHobbySelected = true
                        hobbiesBuilder.append(", " + subjectDataList[index].name)
                        hobbiesIdBuilder.append("," + subjectDataList[index].id)
                    }

                    selectedHobbiesid = hobbiesIdBuilder.toString().removeRange(0..0)
                    mBinding!!.favHobby.text = hobbiesBuilder.toString().removeRange(0..0)


                    // Do something useful withe the position of the selected radio button
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
            if (user!!.academicInfo!!.selectedClass.equals((i + 1).toString(), ignoreCase = true)) {
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
                    var grade = (selectedPosition + 1).toString()
                    user!!.academicInfo!!.selectedClass = grade
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
        var selectedItems: MutableList<String> = ArrayList()
        if (user!!.academicInfo!!.favoriteSubjects != null && user!!.academicInfo!!.leastFavoriteSubjects!!.isNotEmpty() && !isLeastFav) {
            for (favtdata in favleastsubjectsDataList) {
                if (favtdata.isFavSelected!!) {
                    favtdata.name?.let { selectedItems.add(it) }
                }
            }
        }


        if (user!!.academicInfo!!.leastFavoriteSubjects != null && user!!.academicInfo!!.leastFavoriteSubjects!!.isNotEmpty() && isLeastFav) {
            for (leastfav in favleastsubjectsDataList) {
                if (leastfav.isleastelected!!) {
                    leastfav.name?.let { selectedItems.add(it) }
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


        val title: String
        title = if (!isLeastFav) {
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
                        var leastsujectsbuilder = StringBuilder()
                        var leastsujectsidsbuilder = StringBuilder()

                        for (i in selectedSub) {
                            var idsList = favleastsubjectsDataList.map { it.name }
                            val index = idsList.indexOf(i.trim())
                            favleastsubjectsDataList[index].isleastelected = true
                            leastsujectsbuilder.append(", " + favleastsubjectsDataList[index].name)
                            leastsujectsidsbuilder.append("," + favleastsubjectsDataList[index].id)
                        }
                        leastselectedId = leastsujectsidsbuilder.toString().removeRange(0..0)
                        mBinding!!.leastFavuSubj.text = leastsujectsbuilder.toString().removeRange(0..0)

                    } else {
                        for (i in favleastsubjectsDataList.indices) {
                            favleastsubjectsDataList[i].isFavSelected = false
                        }
                        var favsujectsbuilder = StringBuilder()
                        var favsujectsidsbuilder = StringBuilder()
                        for (i in selectedSub) {
                            var idsList = favleastsubjectsDataList.map { it.name }
                            val index = idsList.indexOf(i.trim())
                            favleastsubjectsDataList[index].isFavSelected = true
                            favsujectsbuilder.append(", " + favleastsubjectsDataList[index].name)
                            favsujectsidsbuilder.append("," + favleastsubjectsDataList[index].id)
                        }
                        favtselectedId = favsujectsidsbuilder.toString().removeRange(0..0)
                        mBinding!!.favoriteSubj.text = favsujectsbuilder.toString().removeRange(0..0)
                    }
                }
                .show()
    }


    /*
    *
    * update favleastsubjectsDataList as per grade selection (1-10 or 11-12)
    * */

    fun setleastFavSubjects(seniorClass: Boolean, isFirst: Boolean = false) {
        isSeniorSelected = seniorClass
        favleastsubjectsDataList.clear()
        val items = ArrayList<String>()
        items.add(SUBJECT_ENGLISH)//
        items.add(SUBJECT_MATHS)//
        items.add(SUBJECT_COMPUTER_SCIENCE)//
        if (seniorClass) {
            items.add(SUBJECT_ACCOUNTANCY)//
            items.add(SUBJECT_BIOLOGY)//
            items.add(SUBJECT_BUSINESS)//
            items.add(SUBJECT_CHEMISTRY)//
            items.add(SUBJECT_ECONOMICS)//
            items.add(SUBJECT_PHYSICS)//
        } else {
            items.add(SUBJECT_SOCIAL_STUDIES)//
            items.add(SUBJECT_LANGUAGES)//
            items.add(SUBJECT_SCIENCE)//
        }

        if (isFirst) {
            items.add(SUBJECT_SOCIAL_STUDIES)//
            items.add(SUBJECT_LANGUAGES)//
            items.add(SUBJECT_SCIENCE)//
        }

        for (i in subjectDataList.indices) {
            if (items.contains(subjectDataList[i].name)) {
                favleastsubjectsDataList.add(subjectDataList[i])
            }
        }

    }


    fun setUserProfileImage(user: String?) {
        if (user != null && !user.equals("")) {

            val ref = FirebaseStorage.getInstance().reference.child(user)

            try {
                GlideApp.with(this)
                        .load(ref)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)  // disable caching of glide
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

                    var hobbiesBuilder = StringBuilder()
                    var hobbiesIdBuilder = StringBuilder()
                    for (i in user.academicInfo!!.hobbies!!) {
                        var idsList = subjectDataList.map { it.id }
                        if (idsList.contains(i.trim())) {
                            val index = idsList.indexOf(i.trim())
                            val items = ArrayList<String>()
                            subjectDataList[index].isHobbySelected = true
                            hobbiesBuilder.append(", " + subjectDataList[index].name)
                            hobbiesIdBuilder.append("," + subjectDataList[index].id)
                        }

                    }

                    selectedHobbiesid = hobbiesIdBuilder.toString().removeRange(0..0)
                    mBinding!!.favHobby.text = hobbiesBuilder.toString().removeRange(0..0)



                    setUserProfileImage(user.personInfo!!.accountPicture)

                    var favsujectsbuilder = StringBuilder()
                    var favsujectsidsbuilder = StringBuilder()

                    for (i in user.academicInfo!!.favoriteSubjects!!) {
                        var idsList = favleastsubjectsDataList.map { it.id }
                        if (idsList.contains(i.trim())) {
                            val index = idsList.indexOf(i.trim())
                            favleastsubjectsDataList[index].isFavSelected = true
                            favsujectsbuilder.append(", " + favleastsubjectsDataList[index].name)
                            favsujectsidsbuilder.append("," + favleastsubjectsDataList[index].id)
                        }


                    }

                    favtselectedId = favsujectsidsbuilder.toString().removeRange(0..0)
                    mBinding!!.favoriteSubj.text = favsujectsbuilder.toString().removeRange(0..0)

                    var leastsujectsbuilder = StringBuilder()
                    var leastsujectsidsbuilder = StringBuilder()

                    for (i in user.academicInfo!!.leastFavoriteSubjects!!) {
                        var idsList = favleastsubjectsDataList.map { it.id }
                        if (idsList.contains(i.trim())) {
                            val index = idsList.indexOf(i.trim())
                            favleastsubjectsDataList[index].isleastelected = true
                            leastsujectsbuilder.append(", " + favleastsubjectsDataList[index].name)
                            leastsujectsidsbuilder.append("," + favleastsubjectsDataList[index].id)
                        }
                    }
                    leastselectedId = leastsujectsidsbuilder.toString().removeRange(0..0)
                    mBinding!!.leastFavuSubj.text = leastsujectsbuilder.toString().removeRange(0..0)


                    firebaseStore.collection(getString(R.string.db_root_grades)).whereEqualTo(KEY_ID, user.academicInfo!!.selectedClass!!).get().addOnSuccessListener {

                        it.forEach {
                            mBinding!!.grade.text = "grade ${it.getString(KEY_GRADE)!!}${CommonUtils.getClassSuffix(it.getString(KEY_GRADE)!!.toInt())} "
                            mBinding!!.grade.text = CommonUtils.getGrade(Integer.parseInt(it.getString("grade")!!.trim { it <= ' ' }))
                            this.user!!.academicInfo!!.selectedClass = it.getString("grade")
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

            if (!mBinding!!.password.text.toString().equals("")) {
                firebaseAuth.currentUser!!.updatePassword(mBinding!!.password.text.toString()).addOnCompleteListener {
                    Toast.makeText(this,
                            getString(R.string.passwordchangemessage), Toast.LENGTH_SHORT).show()
                }
            }

            saveDetailsinFireBase()

        }
    }

    fun saveDetailsinFireBase() {
        showLoading()
/*
*  0 favt, 1 lestfav, 2 hobbies
* */
        deleteOldStudentSubject()
        Handler().postDelayed({
            createBatchesForSubject(favtselectedId.split(","), 0)
            createBatchesForSubject(leastselectedId.split(","), 1)
            createBatchesForSubject(selectedHobbiesid.split(","), 2)

            firebaseStore.collection(getString(R.string.db_root_grades)).whereEqualTo(KEY_GRADE, user!!.academicInfo!!.selectedClass).get().addOnSuccessListener {
                it.forEach {
                    saveData(it.getString(KEY_ID)!!)
                }
            }
        }, 1000)


    }

    private fun deleteOldStudentSubject() {
        firebaseStore.collection(getString(R.string.db_root_studentSubjects))
                .whereEqualTo(KEY_STUDENTID, firebaseAuth.currentUser!!.uid).get()
                .addOnSuccessListener {
                    it.forEach {
                        firebaseStore.collection(getString(R.string.db_root_studentSubjects)).document(it.id).delete()
                    }
                }
                .addOnFailureListener { e ->

                }
    }

    fun saveData(selectedGradeId: String) {

        val userpersonalinfo = HashMap<String, Any>()
        if (mBinding!!.edtFirstName.text.toString().split(" ").size > 1) {
            userpersonalinfo[KEY_FIRST_NAME] = mBinding!!.edtFirstName.text.toString().split(" ")[0]
            userpersonalinfo[KEY_LAST_NAME] = mBinding!!.edtFirstName.text.toString().split(" ")[1]
        } else {
            userpersonalinfo[KEY_FIRST_NAME] = mBinding!!.edtFirstName.text.toString()
        }
/*
* Need to create seprate module for Phone number editing.
* And also need to link this changed values with old firebase User Id
*
* */


        // user.put(AppConstants.KEY_PASSWORD, encryptedPassword())
        // user[AppConstants.KEY_PHONE_NUMBER] = mBinding!!.codePicker.fullNumberWithPlus

        val useracadmicinfo = HashMap<String, Any>()

        if (!mBinding!!.password.text.toString().equals("")) {
            useracadmicinfo[KEY_PASSWORD] = getString(mBinding!!.password.text)

        }

        useracadmicinfo[KEY_STDT_LEAST_FAV_CLASSES] = leastselectedId.split(",")
//hdsjhsa


        useracadmicinfo[KEY_STDT_FAVORITE_CLASSES] = favtselectedId.split(",")
        useracadmicinfo[KEY_SELECTED_CLASS] = selectedGradeId
        useracadmicinfo[KEY_STDT_HOBBIES] = selectedHobbiesid.split(",")
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


            var map: HashMap<String, String> = HashMap()


            if (value == 0) {
                map["category"] = "favorite"
                map["isFavourite"] = "true"
                map["isLeastFavourite"] = "false"
                map["isHobby"] = "false"
            } else if (value == 1) {
                map["category"] = "leastfavorite"
                map["isFavourite"] = "false"
                map["isLeastFavourite"] = "true"
                map["isHobby"] = "false"
            } else {
                map["category"] = "hobby"
                map["isFavourite"] = "false"
                map["isLeastFavourite"] = "false"
                map["isHobby"] = "true"

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
            if (it.isSuccessful) {
            }

        }.addOnFailureListener {

            showSnackError(it.message)
        }
    }


    private fun isVerified(): Boolean {
        if (mBinding!!.edtFirstName.text.isEmpty() || mBinding!!.edtFirstName.text.toString().length < 2) {

            showSnackError(resources.getString(R.string.enter_name))
            mBinding!!.edtFirstName.requestFocus()
            return false
        } /*else if (password == null || password.isEmpty()) {
            showSnackError(resources.getString(R.string.enter_password))
            mBinding!!.password.requestFocus()
            return false

        }*/ else if (!mBinding!!.codePicker.isValidFullNumber) {
            showSnackError(resources.getString(R.string.enter_valid_number))
            return false

        } else if (mBinding!!.leastFavuSubj.text.toString() == "") {
            return true

        } else if (mBinding!!.favoriteSubj.text.toString() == "") {
            return true

        } else if (mBinding!!.favHobby.text.toString() == "") {
            return true


        }
        return true

    }


    /*
    * Show Dialog for adding pic from Camera/Gallery
    * */
    fun onAddPicture() {
        val galleryPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (!checkIfAlreadyhavePermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return
            }
            ActivityCompat.requestPermissions(this, galleryPermissions, 0)
        } else {
            val setup = PickSetup()
            setup.cancelText = "Close"
            PickImageDialog.build(setup) { pickResult ->
                if (pickResult.error == null) {
                    val uri = pickResult.uri
                    mBinding!!.profilePicture.setImageURI(uri)
                    val file = File(pickResult.path)
                    val intent = Intent(this, CropActivity::class.java)
                    intent.putExtra(AppConstants.KEY_URI, Uri.fromFile(file))
                    startActivityForResult(intent, 1122)
                }
            }.show(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1122 && resultCode == Activity.RESULT_OK && data != null) {
            val croppedUri = data.getParcelableExtra<Uri>("_cropped_uri_")
            /* mBinding!!.profilePicture.setImageURI(croppedUri)
             mBinding!!.profilePicture.tag = croppedUri!!.path*/
            uploadImage(croppedUri)
        }
    }

    private fun uploadImage(uri: Uri) {
        showLoading()
        val file = File(uri.path!!)
        val size = file.length().toInt()
        val bytes = ByteArray(size)
        try {
            val buf = BufferedInputStream(FileInputStream(file))
            buf.read(bytes, 0, bytes.size)
            buf.close()

            val path = "student/"
            /* if (mProfileType.equals("student", ignoreCase = true)) {
                 path = "student/"
             }*/

            /* val ref = FirebaseStorage.getInstance().reference.child("tutor/" +
                firebaseAuth.currentUser!!.uid + ".jpg")*/
            val pathString = path + firebaseAuth.currentUser!!.uid + ".jpg"
            val picRef = FirebaseStorage.getInstance().reference.child(pathString)
            val uploadTask = picRef.putBytes(bytes)
            uploadTask.addOnSuccessListener {
                hideLoading()
                saveUserPhotoOnFirestore(pathString)
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

    private fun saveUserPhotoOnFirestore(pathString: String) {


        val root = getString(R.string.db_root_students)
        /*  if (mProfileType.equals("student", ignoreCase = true)) {
              root = getString(R.string.db_root_students)
          }*/
        imageURL = pathString

        /*accountPicture*/


        firebaseStore.collection(root).document(appPreferenceHelper.getuserID()).set(
                mapOf(
                        KEY_PERSONALINFO to mapOf(KEY_ACCOUNT_PICTURE to pathString)
                )
                , SetOptions.merge())
                .addOnSuccessListener {

                    setUserProfileImage(imageURL)
                }
                .addOnFailureListener { }
    }

}

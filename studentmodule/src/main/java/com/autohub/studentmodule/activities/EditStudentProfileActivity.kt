package com.autohub.studentmodule.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.autohub.skln.BaseActivity
import com.autohub.skln.CropActivity
import com.autohub.skln.models.UserModel
import com.autohub.skln.utills.ActivityUtils
import com.autohub.skln.utills.AppConstants
import com.autohub.skln.utills.AppConstants.*
import com.autohub.skln.utills.CommonUtils
import com.autohub.skln.utills.GlideApp
import com.autohub.studentmodule.R
import com.autohub.studentmodule.databinding.ActivityEditStudentProfileBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import java.io.*
import java.util.*


/**
 * Created by Vt Netzwelt
 */

class EditStudentProfileActivity : BaseActivity() {
    private var mBinding: ActivityEditStudentProfileBinding? = null

    private var user: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_student_profile)
        mBinding!!.callback = this

        setUpUserInfo()
    }

    fun onBackClick() {
        onBackPressed()
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


    fun showHobby() {
        val items = ArrayList<String>()
        items.add(AppConstants.HOBBY_DANCE)
        items.add(AppConstants.HOBBY_DRUM)
        items.add(AppConstants.HOBBY_GUITAR)
        items.add(AppConstants.HOBBY_KEYBOARD)
        items.add(AppConstants.HOBBY_MARTIAL)
        items.add(AppConstants.HOBBY_PAINT)
        val namesArr = items.toTypedArray()
        val booleans = BooleanArray(items.size)
        var selectedItems: List<String> = ArrayList()
        if (user!!.academicInfo!!.hobbiesToPursue != null && user!!.academicInfo!!.hobbiesToPursue!!.isNotEmpty()) {
            selectedItems = listOf(*user!!.academicInfo!!.hobbiesToPursue!!.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
        }
        val selectedHobbies = ArrayList<String>()
        for (i in selectedItems.indices) {
            if (items.contains(selectedItems[i])) {
                booleans[items.indexOf(selectedItems[i])] = true
                selectedHobbies.add(selectedItems[i])
            }
        }


        AlertDialog.Builder(this)
                .setTitle("Choose your favourite hobby")
                .setMultiChoiceItems(namesArr, booleans) { _, i, b ->
                    if (b) {
                        selectedHobbies.add(items[i])
                    } else {
                        selectedHobbies.remove(items[i])
                    }
                }
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    var selectedHobbyString = ""
                    for (i in selectedHobbies.indices) {
                        selectedHobbyString += if (i == selectedHobbies.size - 1) {
                            selectedHobbies[i]
                        } else {
                            selectedHobbies[i] + ","
                        }
                    }
                    mBinding!!.favHobby.text = selectedHobbyString
                    user!!.academicInfo!!.hobbiesToPursue = selectedHobbyString
                    // Do something useful withe the position of the selected radio button
                }
                .show()
    }

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
                .setTitle("Select Current Grade")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    var selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                    if (selectedPosition < 0) {
                        selectedPosition = 0
                    }
                    mBinding!!.grade.text = namesArr[selectedPosition]
                    user!!.academicInfo!!.selectedClass = (selectedPosition + 1).toString()
                }
                .show()
    }

    fun showSub(isLeastFav: Boolean) {
        val items = ArrayList<String>()
        items.add(AppConstants.SUBJECT_SCIENCE)
        items.add(AppConstants.SUBJECT_COMPUTER_SCIENCE)
        items.add(AppConstants.SUBJECT_ACCOUNTANCY)
        items.add(AppConstants.SUBJECT_BIOLOGY)
        items.add(AppConstants.SUBJECT_BUSINESS)
        items.add(AppConstants.SUBJECT_SOCIAL_STUDIES)
        items.add(AppConstants.SUBJECT_CHEMISTRY)
        items.add(AppConstants.SUBJECT_ECONOMICS)
        items.add(AppConstants.SUBJECT_LANGUAGES)
        items.add(AppConstants.SUBJECT_PHYSICS)
        items.add(AppConstants.SUBJECT_MATHS)
        items.add(AppConstants.SUBJECT_ENGLISH)
        val namesArr = items.toTypedArray()
        val booleans = BooleanArray(items.size)
        var selectedItems: List<String> = ArrayList()
        if (user!!.academicInfo!!.favoriteClasses != null && user!!.academicInfo!!.leastFavoriteClasses!!.isNotEmpty() && !isLeastFav) {
            //            if (!isLeastFav) {
            selectedItems = listOf(*user!!.academicInfo!!.favoriteClasses!!
                    .split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            //            } else {

            //            }
        }
        if (user!!.academicInfo!!.leastFavoriteClasses != null && user!!.academicInfo!!.leastFavoriteClasses!!.isNotEmpty() && isLeastFav) {
            //            if (!isLeastFav) {
            selectedItems = listOf(*user!!.academicInfo!!.leastFavoriteClasses!!.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            //            } else {

            //            }
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
            "Choose Favourite Subject"
        } else {
            "Choose Least Favourite Subject"
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
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    var selectedSubString = ""
                    for (i in selectedSub.indices) {
                        selectedSubString += if (i == selectedSub.size - 1) {
                            selectedSub[i]
                        } else {
                            selectedSub[i] + ","
                        }
                    }
                    if (isLeastFav) {
                        user!!.academicInfo!!.leastFavoriteClasses = selectedSubString
                        mBinding!!.leastFavuSubj.text = selectedSubString
                    } else {
                        user!!.academicInfo!!.favoriteClasses = selectedSubString
                        mBinding!!.favoriteSubj.text = selectedSubString
                    }
                }
                .show()
    }

    private fun setUpUserInfo() {


        val path = "student/"

        val ref = FirebaseStorage.getInstance().reference.child(path +
                firebaseAuth.currentUser!!.uid + ".jpg")
        GlideApp.with(this)
                .load(ref)
                .placeholder(com.autohub.skln.R.drawable.default_pic)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(mBinding!!.profilePicture)

        firebaseStore.collection(getString(com.autohub.skln.R.string.db_root_students)).document(firebaseAuth.currentUser!!.uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(UserModel::class.java)
                    this.user = user
                    user!!.id = documentSnapshot.id
                    mBinding!!.edtFirstName.setText(user.personInfo!!.firstName)
                    mBinding!!.etPhoneNumber.setText(user.personInfo!!.phoneNumber)
                    mBinding!!.favHobby.text = user.academicInfo!!.hobbiesToPursue
                    mBinding!!.favoriteSubj.text = user.academicInfo!!.favoriteClasses
                    mBinding!!.leastFavuSubj.text = user.academicInfo!!.leastFavoriteClasses
                    firebaseStore.collection("grades").whereEqualTo("id", user.academicInfo!!.selectedClass!!).get().addOnSuccessListener {

                        it.forEach {
                            mBinding!!.grade.text = "grade ${it.getString("grade")!!}${CommonUtils.getClassSuffix(it.getString("grade")!!.toInt())} "
                            mBinding!!.grade.text = CommonUtils.getGrade(Integer.parseInt(it.getString("grade")!!.trim { it <= ' ' }))
                            this.user!!.academicInfo!!.selectedClass = it.getString("grade")
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

            firebaseStore.collection("grades").whereEqualTo("grade", user!!.academicInfo!!.selectedClass).get().addOnSuccessListener {

                it.forEach {

                    saveData(it.getString("id")!!)
                }
            }

        }


    }

    fun saveData(selectedGradeId: String) {
        showLoading()
        val userpersonalinfo = HashMap<String, Any>()
        if (mBinding!!.edtFirstName.text.toString().split(" ").size > 1) {
            userpersonalinfo[KEY_FIRST_NAME] = mBinding!!.edtFirstName.text.toString().split(" ")[0]
            userpersonalinfo[KEY_LAST_NAME] = mBinding!!.edtFirstName.text.toString().split(" ")[1]
        } else {
            userpersonalinfo[KEY_FIRST_NAME] = mBinding!!.edtFirstName.text.toString()
        }
/*
* Need to create seprate module for Phone number and password editing.
* And also need to link this changed values with old firebase User Id
*
* */
        // user.put(AppConstants.KEY_PASSWORD, encryptedPassword())
        // user[AppConstants.KEY_PHONE_NUMBER] = mBinding!!.codePicker.fullNumberWithPlus

        val useracadmicinfo = HashMap<String, Any>()

        useracadmicinfo[KEY_STDT_LEAST_FAV_CLASSES] = mBinding!!.leastFavuSubj.text.toString()
        useracadmicinfo[KEY_STDT_FAVORITE_CLASSES] = mBinding!!.favoriteSubj.text.toString()
        useracadmicinfo[KEY_SELECTED_CLASS] = selectedGradeId
        useracadmicinfo[KEY_STDT_HOBBIES] = mBinding!!.favHobby.text.toString()
        val dbRoot = getString(com.autohub.skln.R.string.db_root_students)





        firebaseStore.collection(dbRoot).document(firebaseAuth
                .currentUser!!.uid).set(
                mapOf(
                        KEY_PERSONALINFO to userpersonalinfo,
                        KEY_ACADEMICINFO to useracadmicinfo
                )
                , SetOptions.merge())
                .addOnSuccessListener {
                    hideLoading()
                    Toast.makeText(this,
                            "Profile Updated.", Toast.LENGTH_SHORT).show()

                    ActivityUtils.launchActivity(this, StudentHomeActivity::class.java)
                    finishAffinity()

                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }

    }


    private fun isVerified(): Boolean {
        val password = mBinding!!.password.text
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
            mBinding!!.profilePicture.setImageURI(croppedUri)
            mBinding!!.profilePicture.tag = croppedUri!!.path
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
            val pathString = path + firebaseAuth.currentUser!!.uid + ".jpg"
            val picRef = FirebaseStorage.getInstance().reference.child(pathString)
            val uploadTask = picRef.putBytes(bytes)
            uploadTask.addOnSuccessListener {
                hideLoading()
                saveUserPhoneOnFirestore(pathString)
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

    private fun saveUserPhoneOnFirestore(pathString: String) {
        val user = HashMap<String, Any>()
        user[AppConstants.KEY_PROFILE_PICTURE] = pathString

        val root = getString(R.string.db_root_students)
        /*  if (mProfileType.equals("student", ignoreCase = true)) {
              root = getString(R.string.db_root_students)
          }*/


        /*accountPicture*/


        firebaseStore.collection(root).document(FirebaseAuth.getInstance().currentUser!!.uid).set(

                mapOf(
                        KEY_PERSONALINFO to mapOf(KEY_ACCOUNT_PICTURE to pathString)
                )
                , SetOptions.merge())
                .addOnSuccessListener { }
                .addOnFailureListener { }
    }

}

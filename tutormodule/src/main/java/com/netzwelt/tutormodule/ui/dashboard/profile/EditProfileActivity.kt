package com.netzwelt.tutormodule.ui.dashboard.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.autohub.skln.BaseActivity
import com.autohub.skln.CropActivity
import com.autohub.skln.models.User
import com.autohub.skln.models.UserViewModel
import com.autohub.skln.utills.ActivityUtils
import com.autohub.skln.utills.AppConstants
import com.autohub.skln.utills.CommonUtils
import com.autohub.skln.utills.GlideApp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.netzwelt.tutormodule.R
import com.netzwelt.tutormodule.databinding.ActivityTutorEditProfileBinding
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import kotlinx.android.synthetic.main.activity_tutor_edit_profile.*
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class EditProfileActivity : BaseActivity() {

    private lateinit var mBinding: ActivityTutorEditProfileBinding
    private val MAX_SIZE = 240
    private var mUserViewModel: UserViewModel? = null
    private var mStorageReference: StorageReference? = null

    private val mWatcherWrapper = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val remaining = MAX_SIZE - s?.length!!
            mBinding.count.text = String.format(Locale.getDefault(), "%d remaining", remaining)
            // editBio(s?.toString())
        }

    }

    val selectedClass = ArrayList<String>()
    val selectedExp = ArrayList<String>()
    val selectedQualification = ArrayList<String>()
    val selectedQualificationAreas = ArrayList<String>()
    val selectedTargetBoard = ArrayList<String>()
    val selectedSub = ArrayList<String>()
    val selectedOccupation = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_edit_profile)
        mBinding.callback = this
        mStorageReference = FirebaseStorage.getInstance().reference
        mUserViewModel = UserViewModel(User())
        mBinding.userViewModel = mUserViewModel
        mBinding.bio.addTextChangedListener(mWatcherWrapper)
        // setupProfile()
        //  setUpUserInfo()

    }

    fun onSubjectTaughtClick() {
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

        showMultiSelectionDialog(items, mBinding!!.subjectToTaught, getString(R.string.subject_to_taught), selectedSub)


    }

    fun onClassToTeach() {
//        showClassesToTeach()

        val items = ArrayList<String>()
        items.add("Class " + AppConstants.CLASS_1 + CommonUtils.getClassSuffix(AppConstants.CLASS_1.toInt()))
        items.add("Class " + AppConstants.CLASS_2 + CommonUtils.getClassSuffix(AppConstants.CLASS_2.toInt()))
        items.add("Class " + AppConstants.CLASS_3 + CommonUtils.getClassSuffix(AppConstants.CLASS_3.toInt()))
        items.add("Class " + AppConstants.CLASS_4 + CommonUtils.getClassSuffix(AppConstants.CLASS_4.toInt()))
        items.add("Class " + AppConstants.CLASS_5 + CommonUtils.getClassSuffix(AppConstants.CLASS_5.toInt()))
        items.add("Class " + AppConstants.CLASS_6 + CommonUtils.getClassSuffix(AppConstants.CLASS_6.toInt()))
        items.add("Class " + AppConstants.CLASS_7 + CommonUtils.getClassSuffix(AppConstants.CLASS_7.toInt()))
        items.add("Class " + AppConstants.CLASS_8 + CommonUtils.getClassSuffix(AppConstants.CLASS_8.toInt()))
        items.add("Class " + AppConstants.CLASS_9 + CommonUtils.getClassSuffix(AppConstants.CLASS_9.toInt()))
        items.add("Class " + AppConstants.CLASS_10 + CommonUtils.getClassSuffix(AppConstants.CLASS_10.toInt()))
        items.add("Class " + AppConstants.CLASS_11 + CommonUtils.getClassSuffix(AppConstants.CLASS_11.toInt()))
        items.add("Class " + AppConstants.CLASS_12 + CommonUtils.getClassSuffix(AppConstants.CLASS_12.toInt()))
        val namesArr = items.toTypedArray()

        showMultiSelectionDialog(items, mBinding!!.classToTeach, getString(R.string.class_to_teach), selectedClass)
    }

    fun onSelectOccupation() {
        var items = getResources().getStringArray(R.array.occupation_arrays).toList()

        showSingleSelectionDialog(items, mBinding!!.selectOccupation, getString(R.string.select_ocupation), selectedOccupation)

    }

    fun onSelectExperience() {

        var items = getResources().getStringArray(R.array.experience_arrays).toList()
        showSingleSelectionDialog(items, mBinding!!.teachingExperience, getString(R.string.select_treaching_epereience), selectedExp)
        //showExperience()
    }

    fun onSelectQualification() {
        selectedQualificationAreas.clear()
        mBinding.areaOfQualification.text = ""
        var items = getResources().getStringArray(R.array.qualification_arrays).toList()
        showSingleSelectionDialog(items, mBinding!!.qualification, getString(R.string.select_qualification), selectedQualification)
    }

    fun onSelectQualificationArea() {
        lateinit var items: List<String>
        if (selectedQualification.size > 0) {
            if (selectedQualification[0].equals("Graduate")) {
                items = getResources().getStringArray(R.array.area_qualifi_arrays_1).toList()

            } else if (selectedQualification[0].equals("Post-Graduate")) {
                items = getResources().getStringArray(R.array.area_qualifi_arrays_2).toList()

            } else {
                items = getResources().getStringArray(R.array.area_qualifi_arrays_1).toList()

            }

            showMultiSelectionDialog(items, mBinding!!.areaOfQualification, getString(R.string.select_area_of_qualification), selectedQualificationAreas)

        } else {
            showSnackError("Please select your qualification first.")

        }


    }

    fun onSelectTargetBoard() {
        val items = ArrayList<String>()
        items.add(AppConstants.BOARD_CBSE)
        items.add(AppConstants.BOARD_ICSE)
        items.add(AppConstants.BOARD_STATE)


        showMultiSelectionDialog(items, mBinding!!.targetedBoard, getString(R.string.select_targeted_board), selectedTargetBoard)
//        showTargetBoard()
    }

    fun showMultiSelectionDialog(items: List<String>, testview: TextView, title: String, selectedItems: ArrayList<String>) {
        val namesArr = items.toTypedArray()
        val booleans = BooleanArray(items.size)
        val selectedvalues = ArrayList<String>()

        for (i in selectedItems.indices) {
            if (items.contains(selectedItems[i])) {
                booleans[items.indexOf(selectedItems[i])] = true
                selectedvalues.add(selectedItems[i])
            }
        }

        AlertDialog.Builder(this)
                .setMultiChoiceItems(namesArr, booleans
                ) { _, i, b ->
                    if (b) {
                        selectedvalues.add(items[i])
                    } else {
                        selectedvalues.remove(items[i])
                    }
                }
                .setTitle(title)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    var selectedSubString = ""
                    for (i in selectedvalues.indices) {
                        selectedSubString += if (i == selectedvalues.size - 1) {
                            selectedvalues[i]
                        } else {
                            selectedvalues[i] + ","
                        }
                    }
                    testview.text = selectedSubString
                    selectedItems.clear()
                    selectedItems.addAll(selectedvalues)

                }
                .show()

    }

    fun showSingleSelectionDialog(items: List<String>, testview: TextView, title: String, selectedItems: ArrayList<String>) {
        val namesArr = items.toTypedArray()
        var indexSelected = -1
        if (selectedItems.size > 0) {
            for (i in namesArr.indices) {
                if (namesArr[i].equals(selectedItems[0])) {
                    indexSelected = i
                    break
                } else {
                    indexSelected = 0
                }
            }
        } else {
            indexSelected = 0

        }


        AlertDialog.Builder(this)
                .setSingleChoiceItems(namesArr, indexSelected, null)
                .setTitle(title)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    var selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                    if (selectedPosition < 0) {
                        selectedPosition = 0
                    }
                    testview.text = namesArr[selectedPosition]
                    selectedItems.clear()
                    selectedItems.add(namesArr[selectedPosition])
                    /*  mBinding!!.grade.text = namesArr[selectedPosition]
                      user!!.studentClass = (selectedPosition + 1).toString()*/
                }
                .show()
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
                    mBinding.profilePicture.setImageURI(uri)
                    mBinding.profilePicture.tag = pickResult.path

                    val file = File(pickResult.path)
                    val intent = Intent(this, CropActivity::class.java)
                    intent.putExtra(AppConstants.KEY_URI, Uri.fromFile(file))
                    startActivityForResult(intent, 1122)
                }
            }.show(this)
        }
    }


    /*  private fun showOccupation() {
          var items = getResources().getStringArray(R.array.occupation_arrays).toList()

          val namesArr = items.toTypedArray()
          val booleans = BooleanArray(items.size)
          val selectedOccupation = ArrayList<String>()

          val title: String
          title = "Choose Occupation"
          AlertDialog.Builder(this)
                  .setMultiChoiceItems(namesArr, booleans
                  ) { _, i, b ->
                      if (b) {
                          selectedOccupation.add(items[i])
                      } else {
                          selectedOccupation.remove(items[i])
                      }
                  }
                  .setTitle(title)
                  .setPositiveButton("OK") { dialog, _ ->
                      dialog.dismiss()
                      var selectedSubString = ""
                      for (i in selectedOccupation.indices) {
                          selectedSubString += if (i == selectedOccupation.size - 1) {
                              selectedOccupation[i]
                          } else {
                              selectedOccupation[i] + ","
                          }
                      }
                      mBinding!!.selectOccupation.text = selectedSubString

                  }
                  .show()

      }*/

    fun onBackClick() {
        onBackPressed()
    }

    private fun setupProfile() {
        val ref = FirebaseStorage.getInstance().reference.child("tutor/" +
                "j9MtRdT5L0g62QiQ7z514z0hQz52"/*firebaseAuth.currentUser!!.uid*/ + ".jpg")
        GlideApp.with(this)
                .load(ref)
                .placeholder(com.autohub.skln.R.drawable.default_pic)
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // disable caching of glide
                .skipMemoryCache(true)

                .into(mBinding.profilePicture)
    }

    private fun setUpUserInfo() {
        firebaseStore.collection(getString(R.string.db_root_tutors)).document("j9MtRdT5L0g62QiQ7z514z0hQz52"/*firebaseAuth.currentUser!!.uid*/).get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(User::class.java)
                    mUserViewModel!!.setUser(user)
                }
                .addOnFailureListener { e -> showSnackError(e.message) }
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
            val picRef = mStorageReference!!.child("tutor/" + firebaseAuth.currentUser!!.uid + ".jpg")
            val uploadTask = picRef.putBytes(bytes)
            uploadTask.addOnSuccessListener { hideLoading() }.addOnFailureListener { e ->
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

    private fun editBio(bio: String) {
        val user = HashMap<String, Any>()
        user[AppConstants.KEY_BIODATA] = bio

        FirebaseFirestore.getInstance().collection(getString(R.string.db_root_tutors)).document(FirebaseAuth.getInstance().currentUser!!.uid).set(user, SetOptions.merge())
                .addOnSuccessListener { mUserViewModel!!.setBioData(bio) }
                .addOnFailureListener { }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1122 && resultCode == Activity.RESULT_OK && data != null) {
            val croppedUri = data.getParcelableExtra<Uri>("_cropped_uri_")
            val originalUri = data.getParcelableExtra<Uri>("_original_uri_")
            Log.d(">>>RegisterAcRes", croppedUri!!.toString() + " , " + originalUri!!.toString())
            mBinding.profilePicture.setImageURI(croppedUri)
            mBinding.profilePicture.tag = croppedUri.path
            // uploadImage(croppedUri)
        }
    }

    fun makeSaveRequest() {
        if (isVerified()) {
            showSnackError("In progress")

        }


    }

    private fun isVerified(): Boolean {
        if (mBinding!!.classToTeach.text.isEmpty()) {
            showSnackError("Please select classes you teach.")
            return false
        } else if (subject_to_taught.text.isEmpty()) {
            showSnackError("Please select subjects you teach.")
            return false

        } else if (select_occupation.text.isEmpty()) {
            showSnackError("Please select your occupation.")
            return false

        } else if (teaching_experience.text.isEmpty()) {
            showSnackError("Please select your teaching experience.")
            return false

        } else if (qualification.text.isEmpty()) {
            showSnackError("Please select your qualification.")
            return false

        } else {
            return true

        }

    }


}
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
    val selectedSubject = ArrayList<String>()
    val selectedExp = ArrayList<String>()
    val selectedQualification = ArrayList<String>()
    val selectedQualificationAreas = ArrayList<String>()
    val selectedTargetBoard = ArrayList<String>()
    val selectedSub = ArrayList<String>()
    val selectedOccupation = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_edit_profile)
        mBinding!!.callback = this
        mStorageReference = FirebaseStorage.getInstance().reference
        mUserViewModel = UserViewModel(User())
        mBinding.userViewModel = mUserViewModel
        mBinding.bio.addTextChangedListener(mWatcherWrapper)
        setupProfile()
        setUpUserInfo()

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

        showDialog(items, mBinding!!.subjectToTaught, "Choose Subject", selectedSub)


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

        showDialog(items, mBinding!!.classToTeach, "Choose classes to teach", selectedClass)
    }

    fun onSelectOccupation() {
        var items = getResources().getStringArray(R.array.occupation_arrays).toList()

        showDialog(items, mBinding!!.selectOccupation, "Choose Occupation", selectedOccupation)

    }

    fun onSelectExperience() {

        var items = getResources().getStringArray(R.array.experience_arrays).toList()
        showDialog(items, mBinding!!.teachingExperience, "Choose experience", selectedExp)
        //showExperience()
    }

    fun onSelectQualification() {

        var items = getResources().getStringArray(R.array.qualification_arrays).toList()
        showDialog(items, mBinding!!.qualification, "Choose Qualification", selectedQualification)
    }

    fun onSelectQualificationArea() {

        var items = getResources().getStringArray(R.array.area_qualifi_arrays_1).toList()

        showDialog(items, mBinding!!.areaOfQualification, "Choose Qualification areas", selectedQualificationAreas)
    }

    fun onSelectTargetBoard() {
        val items = ArrayList<String>()
        items.add(AppConstants.BOARD_CBSE)
        items.add(AppConstants.BOARD_ICSE)
        items.add(AppConstants.BOARD_STATE)


        showDialog(items, mBinding!!.targetedBoard, "Choose Board", selectedTargetBoard)
//        showTargetBoard()
    }

    fun showDialog(items: List<String>, testview: TextView, title: String, selectedItems: ArrayList<String>) {
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
            uploadImage(croppedUri)
        }
    }


}
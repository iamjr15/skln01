package com.autohub.tutormodule.ui.dashboard.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.autohub.skln.BaseActivity
import com.autohub.skln.models.batchRequests.GradeData
import com.autohub.skln.models.batchRequests.SubjectData
import com.autohub.skln.models.tutor.TutorData
import com.autohub.skln.models.tutor.TutorGradesSubjects
import com.autohub.skln.utills.AppConstants
import com.autohub.skln.utills.CommonUtils
import com.autohub.skln.utills.GlideApp
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.ActivityTutorEditProfileBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kbeanie.multipicker.api.CacheLocation
import com.kbeanie.multipicker.api.CameraImagePicker
import com.kbeanie.multipicker.api.ImagePicker
import com.kbeanie.multipicker.api.Picker
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback
import com.kbeanie.multipicker.api.entity.ChosenImage
import kotlinx.android.synthetic.main.activity_tutor_edit_profile.*
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class EditProfileActivity : BaseActivity(), ImagePickerCallback {

    private lateinit var mBinding: ActivityTutorEditProfileBinding
    private val maxSize = 240
    private var mStorageReference: StorageReference? = null
    private var tutorData: TutorData? = null

    private var selectedSubjectsList = ArrayList<String>()
    private var subjectsList = ArrayList<SubjectData>()
    private var imageuri: Uri? = null


    private var selectedGradesList = ArrayList<String>()
    private var gradesList = ArrayList<GradeData>()
    private var profilePictureUri: String = ""
    private var permissionRequest: Int = 12

    private lateinit var cameraPicker: CameraImagePicker
    private lateinit var imagePicker: ImagePicker
    private var isTakePicture: Boolean = false

    private val mWatcherWrapper = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val remaining = maxSize - s?.length!!
            mBinding.count.text = String.format(Locale.getDefault(), "%d remaining", remaining)
            // editBio(s?.toString())
        }

    }

    private val selectedClass = ArrayList<String>()
    private val selectedExp = ArrayList<String>()
    private val selectedQualification = ArrayList<String>()
    private val selectedQualificationAreas = ArrayList<String>()
    private val selectedTargetBoard = ArrayList<String>()
    private val selectedSub = ArrayList<String>()
    private val selectedOccupation = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_edit_profile)
        mBinding.callback = this
        mStorageReference = FirebaseStorage.getInstance().reference
        mBinding.bio.addTextChangedListener(mWatcherWrapper)

        if (intent.hasExtra(getString(R.string.containsTutorData))) {
            setUpTutorInfo()
        }
    }


    private fun setUpTutorInfo() {
        showLoading()

        val tutor = intent.getParcelableExtra<TutorData>(getString(R.string.containsTutorData))

        this.tutorData = tutor
        mBinding.selectOccupation.text = tutor?.qualification?.currentOccupation
        mBinding.teachingExperience.text = tutor?.qualification?.experience
        mBinding.qualification.text = tutor?.qualification?.qualification
        mBinding.areaOfQualification.text = tutor?.qualification?.qualificationArea
        mBinding.targetedBoard.text = tutor?.qualification?.targetBoard
        mBinding.bio.setText(tutor?.personInfo?.biodata)

        GlideApp.with(this)
                .load(tutor?.personInfo?.accountPicture)
                .placeholder(com.autohub.skln.R.drawable.default_pic)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(mBinding.profilePicture)

        getTutorSubjects()
        getTutorGrades()

    }

    /*Fetch subjects on the basis of tutor id
    * */
    private fun getTutorSubjects() {
        firebaseStore.collection(getString(R.string.db_root_tutor_subjects)).whereEqualTo("teacherId", tutorData?.id).get()
                .addOnSuccessListener { documentSnapshot ->
                    val tutorSubjects = documentSnapshot.toObjects(TutorGradesSubjects::class.java)
                    getTutorSubjectsToTeach(tutorSubjects)
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }
    }

    /*Fetch subjects to teach on the basis of tutor id
   * */
    private fun getTutorSubjectsToTeach(tutorSubjects: List<TutorGradesSubjects>) {
        firebaseStore.collection(getString(R.string.db_root_subjects)).get()
                .addOnSuccessListener { documentSnapshot ->
                    hideLoading()
                    val subjects = documentSnapshot.toObjects(SubjectData::class.java)
                    subjectsList = subjects as ArrayList<SubjectData>
                    for (i in tutorSubjects.indices) {
                        for (j in 0 until subjectsList.size) {
                            if (subjectsList[j].id.equals(tutorSubjects[i].subjectId)) {
                                selectedSubjectsList.add(subjectsList[j].name!!)
                            }
                        }
                    }
                    mBinding.subjectToTaught.text = selectedSubjectsList.joinToString(" , ")

                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }
    }


    /*Fetch grades on the basis of tutor id
    * */
    private fun getTutorGrades() {
        firebaseStore.collection(getString(R.string.db_root_tutor_grades)).whereEqualTo("teacherId", tutorData?.id).get()
                .addOnSuccessListener { documentSnapshot ->
                    hideLoading()
                    val tutorGrades = documentSnapshot.toObjects(TutorGradesSubjects::class.java)
                    getTutorGradesToTeach(tutorGrades)
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }
    }

    /*Fetch grades to teach on the basis of tutor id
   * */
    private fun getTutorGradesToTeach(tutorGrades: List<TutorGradesSubjects>) {
        firebaseStore.collection(getString(R.string.db_root_grades)).get()
                .addOnSuccessListener { documentSnapshot ->
                    hideLoading()
                    val grades = documentSnapshot.toObjects(GradeData::class.java)
                    gradesList = grades as ArrayList<GradeData>
                    for (i in tutorGrades.indices) {
                        for (j in 0 until gradesList.size) {
                            if (gradesList[j].id.equals(tutorGrades[i].gradeId)) {
                                selectedGradesList.add(gradesList[j].grade!! + CommonUtils.getClassSuffix(gradesList[j].grade!!.toInt()))
                            }
                        }
                    }
                    mBinding.classToTeach.text = selectedGradesList.joinToString(", ")
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }


    }

    /*Open dialog showing subjects on Select subjects to teach click
    * */
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

        showMultiSelectionDialog(items, mBinding.subjectToTaught, getString(R.string.subject_to_taught), selectedSub)


    }

    /*Open dialog showing classes on Select classes to teach click
    * */
    fun onClassToTeach() {
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

        showMultiSelectionDialog(items, mBinding.classToTeach, getString(R.string.class_to_teach), selectedClass)
    }

    /*Open dialog showing occupations on Select Occupation
    * */
    fun onSelectOccupation() {
        val items = resources.getStringArray(R.array.occupation_arrays).toList()

        showSingleSelectionDialog(items, mBinding.selectOccupation, getString(R.string.select_occupation), selectedOccupation)

    }

    /*Open dialog showing years on Select Experience
   * */
    fun onSelectExperience() {

        val items = resources.getStringArray(R.array.experience_arrays).toList()
        showSingleSelectionDialog(items, mBinding.teachingExperience, getString(R.string.select_teaching_experience), selectedExp)
    }

    /*Open dialog showing Qualification on Select Qualification
   * */
    fun onSelectQualification() {
        selectedQualificationAreas.clear()
        mBinding.areaOfQualification.text = ""
        val items = resources.getStringArray(R.array.qualification_arrays).toList()
        showSingleSelectionDialog(items, mBinding.qualification, getString(R.string.select_qualification), selectedQualification)
    }

    /*Open dialog showing qualification areas on Select Qualification Area
   * */
    fun onSelectQualificationArea() {
        lateinit var items: List<String>
        if (selectedQualification.size > 0) {
            items = when {
                selectedQualification[0] == "Graduate" -> {
                    resources.getStringArray(R.array.area_qualification_array).toList()
                }
                selectedQualification[0] == "Post-Graduate" -> {
                    resources.getStringArray(R.array.area_qualifi_arrays_2).toList()
                }
                else -> {
                    resources.getStringArray(R.array.area_qualification_array).toList()
                }
            }

            showMultiSelectionDialog(items, mBinding.areaOfQualification, getString(R.string.select_area_of_qualification), selectedQualificationAreas)

        } else {
            showSnackError("Please select your qualification first.")

        }


    }

    /*Open dialog showing boards on Select Target Board
   * */
    fun onSelectTargetBoard() {
        val items = ArrayList<String>()
        items.add(AppConstants.BOARD_CBSE)
        items.add(AppConstants.BOARD_ICSE)
        items.add(AppConstants.BOARD_STATE)


        showMultiSelectionDialog(items, mBinding.targetedBoard, getString(R.string.select_targeted_board), selectedTargetBoard)
    }

    private fun showMultiSelectionDialog(items: List<String>, textView: TextView, title: String, selectedItems: ArrayList<String>) {
        val namesArr = items.toTypedArray()
        val booleans = BooleanArray(items.size)
        val selectedValues = ArrayList<String>()

        for (i in selectedItems.indices) {
            if (items.contains(selectedItems[i])) {
                booleans[items.indexOf(selectedItems[i])] = true
                selectedValues.add(selectedItems[i])
            }
        }

        AlertDialog.Builder(this)
                .setMultiChoiceItems(namesArr, booleans
                ) { _, i, b ->
                    if (b) {
                        selectedValues.add(items[i])
                    } else {
                        selectedValues.remove(items[i])
                    }
                }
                .setTitle(title)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    var selectedSubString = ""
                    for (i in selectedValues.indices) {
                        selectedSubString += if (i == selectedValues.size - 1) {
                            selectedValues[i]
                        } else {
                            selectedValues[i] + ","
                        }
                    }
                    textView.text = selectedSubString.replace("Class", "")
                    selectedItems.clear()
                    selectedItems.addAll(selectedValues)

                }
                .show()

    }

    private fun showSingleSelectionDialog(items: List<String>, textView: TextView, title: String, selectedItems: ArrayList<String>) {
        val namesArr = items.toTypedArray()
        var indexSelected = -1
        if (selectedItems.size > 0) {
            for (i in namesArr.indices) {
                if (namesArr[i] == selectedItems[0]) {
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
                    textView.text = namesArr[selectedPosition]
                    selectedItems.clear()
                    selectedItems.add(namesArr[selectedPosition])
                }
                .show()
    }

    /*Request for camera, write and read external storage
    * */
    fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showSnackError("You need to grant permissions from settings.")
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                        permissionRequest)
            }
        } else {
            onAddPicture()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            permissionRequest -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                                && grantResults[2] == PackageManager.PERMISSION_GRANTED)) {
                    onAddPicture()
                }
            }
        }
    }

    /*Choose picture to upload
    * */
    private fun onAddPicture() {

        /*   TedBottomPicker.with(this)
                   .show { uri ->
                       GlideApp.with(this)
                               .load(uri)
                               .placeholder(com.autohub.skln.R.drawable.default_pic)
                               .diskCacheStrategy(DiskCacheStrategy.NONE)  // disable caching of glide
                               .skipMemoryCache(true)

                               .into(mBinding.profilePicture)
                       uploadImage(uri)

                   }*/

        onpenImagePickerDialog()
    }


    private fun onpenImagePickerDialog() {
        val mBuilder = AlertDialog.Builder(this)

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
        } else if (requestCode == Picker.PICK_IMAGE_CAMERA) run {
            if (cameraPicker == null) {
                cameraPicker = CameraImagePicker(this)
                cameraPicker.setImagePickerCallback(this)
                //cameraPicker.reinitialize(pickerPath);
            }
            cameraPicker.submit(data)
        }

    }


    override fun onImagesChosen(list: MutableList<ChosenImage>?) {
        val chosenImage = list!!.get(0)

        if (chosenImage.originalPath.contains("content:")) {
            imageuri = if (isTakePicture) {
                Uri.fromFile(File(chosenImage.originalPath))

            } else {
                Uri.parse(chosenImage.originalPath)

            }
        } else {
            isTakePicture = true
            imageuri = Uri.fromFile(File(chosenImage.originalPath))

        }


        GlideApp.with(this)
                .load(imageuri)
                .placeholder(com.autohub.skln.R.drawable.default_pic)
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // disable caching of glide
                .skipMemoryCache(true)
                .into(mBinding.profilePicture)

        if (imageuri != null) {
            if (isTakePicture) {
                uploadImage(imageuri!!)
            } else {
                uploadGalleryImage(imageuri!!)


            }
        }

    }


    override fun onError(p0: String?) {
    }


    fun onBackClick() {
        onBackPressed()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

   /*Upload picture on fire store storage and get url
   * */
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
            uploadTask.addOnSuccessListener {
                picRef.downloadUrl.addOnSuccessListener {
                    profilePictureUri = it.toString()
                }
                Toast.makeText(this, "Profile Picture uploaded successfully!", Toast.LENGTH_LONG).show()
                hideLoading()
            }.addOnFailureListener { e ->
                hideLoading()
                Toast.makeText(this, "Upload Failed $e", Toast.LENGTH_LONG).show()
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            hideLoading()
        } catch (e: IOException) {
            e.printStackTrace()
            hideLoading()
        }
    }


    private fun uploadGalleryImage(uri: Uri) {
        var size = 0
        showLoading()

        uri.let { returnUri ->
            contentResolver.query(returnUri, null, null, null, null)
        }?.use { cursor ->
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
                    profilePictureUri = it.toString()
                }
                Toast.makeText(this, "Profile Picture uploaded successfully!", Toast.LENGTH_LONG).show()
                hideLoading()
            }.addOnFailureListener { e ->
                hideLoading()
                Toast.makeText(this, "Upload Failed $e", Toast.LENGTH_LONG).show()
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            hideLoading()
        } catch (e: IOException) {
            e.printStackTrace()
            hideLoading()
        }
    }

    fun makeSaveRequest() {
        if (isVerified()) {
            updateTutorData()
        }
    }

    /*Update tutor data on update button click
    * */
    private fun updateTutorData() {
        showLoading()

        val tutor = TutorData()

        tutor.qualification?.currentOccupation = mBinding.selectOccupation.text.toString()
        tutor.qualification?.experience = mBinding.teachingExperience.text.toString()
        tutor.qualification?.qualification = mBinding.qualification.text.toString()
        tutor.qualification?.qualificationArea = mBinding.areaOfQualification.text.toString()
        tutor.qualification?.targetBoard = mBinding.targetedBoard.text.toString()
        tutor.personInfo?.biodata = mBinding.bio.text.toString()
        if (profilePictureUri.trim().isNotEmpty()) {
            tutor.personInfo?.accountPicture = profilePictureUri
        } else {
            profilePictureUri = tutorData?.personInfo?.accountPicture!!
        }

        Log.e("tutor", tutor.toString())
        firebaseStore.collection(getString(R.string.db_root_tutors)).document(appPreferenceHelper.getuserID()).update(
                "qualification.currentOccupation", tutor.qualification?.currentOccupation,
                "qualification.experience", tutor.qualification?.experience,
                "qualification.qualification", tutor.qualification?.qualification,
                "qualification.qualificationArea", tutor.qualification?.qualificationArea,
                "qualification.targetBoard", tutor.qualification?.targetBoard,
                "personInfo.biodata", tutor.personInfo?.biodata,
                "personInfo.accountPicture", tutor.personInfo?.accountPicture).addOnSuccessListener {
            hideLoading()
            showSnackError("Your profile is updated successfully!!")
        }.addOnFailureListener { e ->
            hideLoading()
            showSnackError(e.toString())
        }

    }


    private fun isVerified(): Boolean {
        when {
            mBinding.classToTeach.text.isEmpty() -> {
                showSnackError("Please select classes you teach.")
                return false
            }
            subject_to_taught.text.isEmpty() -> {
                showSnackError("Please select subjects you teach.")
                return false

            }
            select_occupation.text.isEmpty() -> {
                showSnackError("Please select your occupation.")
                return false

            }
            teaching_experience.text.isEmpty() -> {
                showSnackError("Please select your teaching experience.")
                return false

            }
            qualification.text.isEmpty() -> {
                showSnackError("Please select your qualification.")
                return false

            }
            bio.text.isEmpty() -> {
                showSnackError("Please add Bio.")
                return false

            }
            else -> {
                return true
            }
        }
    }
}
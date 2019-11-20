package com.autohub.tutormodule.ui.manageClasses

import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.models.batchRequests.GradeData
import com.autohub.skln.models.batchRequests.SubjectData
import com.autohub.skln.models.batches.BatchesModel
import com.autohub.skln.models.tutor.TutorData
import com.autohub.skln.utills.AppConstants
import com.autohub.skln.utills.CommonUtils
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.FragmentTutorAddBatchBinding
import com.autohub.tutormodule.ui.dashboard.listner.HomeListener
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class AddBatchFragment : BaseFragment() {

    private lateinit var mBinding: FragmentTutorAddBatchBinding
    private lateinit var homeListener: HomeListener

    private var isAddBatch: Boolean = true
    private val selectedSub = ArrayList<String>()
    private val selectedClass = ArrayList<String>()

    private var gradesList = ArrayList<GradeData>()
    private var subjectList = ArrayList<SubjectData>()

    private var selectedSubPosition: Int = 0
    private var selectedClassPosition: Int = 0
    private var counter = 0
    lateinit var tutorData: TutorData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_tutor_add_batch, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentTutorAddBatchBinding.bind(view)
        mBinding.callback = this

        if (!this.arguments?.isEmpty!!) {
            isAddBatch = this.arguments!!.getBoolean("showAddBatch")
        }

        if (!isAddBatch) {
            mBinding.textHeading.text = resources.getString(R.string.edit_schedule)
        } else {
            mBinding.textHeading.text = resources.getString(R.string.add_batch)
        }
        fetchTutorData()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeListener = context as HomeListener
    }

    fun openTimePicker(view: View) {
        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            (view as TextView).text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(cal.time)
        }
        TimePickerDialog(context, R.style.DialogTheme, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }


    fun showClasses() {
        val items = ArrayList<String>()
//        items.add("Class " + AppConstants.CLASS_1 + CommonUtils.getClassSuffix(AppConstants.CLASS_1.toInt()))
//        items.add("Class " + AppConstants.CLASS_2 + CommonUtils.getClassSuffix(AppConstants.CLASS_2.toInt()))
//        items.add("Class " + AppConstants.CLASS_3 + CommonUtils.getClassSuffix(AppConstants.CLASS_3.toInt()))
//        items.add("Class " + AppConstants.CLASS_4 + CommonUtils.getClassSuffix(AppConstants.CLASS_4.toInt()))
//        items.add("Class " + AppConstants.CLASS_5 + CommonUtils.getClassSuffix(AppConstants.CLASS_5.toInt()))
//        items.add("Class " + AppConstants.CLASS_6 + CommonUtils.getClassSuffix(AppConstants.CLASS_6.toInt()))
//        items.add("Class " + AppConstants.CLASS_7 + CommonUtils.getClassSuffix(AppConstants.CLASS_7.toInt()))
//        items.add("Class " + AppConstants.CLASS_8 + CommonUtils.getClassSuffix(AppConstants.CLASS_8.toInt()))
//        items.add("Class " + AppConstants.CLASS_9 + CommonUtils.getClassSuffix(AppConstants.CLASS_9.toInt()))
//        items.add("Class " + AppConstants.CLASS_10 + CommonUtils.getClassSuffix(AppConstants.CLASS_10.toInt()))
//        items.add("Class " + AppConstants.CLASS_11 + CommonUtils.getClassSuffix(AppConstants.CLASS_11.toInt()))
//        items.add("Class " + AppConstants.CLASS_12 + CommonUtils.getClassSuffix(AppConstants.CLASS_12.toInt()))
        firebaseStore.collection(getString(R.string.db_root_grades)).get().addOnSuccessListener { documentSnapshot ->
            hideLoading()
            gradesList = documentSnapshot.toObjects(GradeData::class.java) as ArrayList<GradeData>
            for (i in 0 until gradesList.size) {
                items.add("Class" + gradesList[i]?.grade!!)
            }
            showDialog(items, mBinding.selectClass, "Select Class", selectedClass)

        }.addOnFailureListener { e ->
            hideLoading()
            showSnackError(e.message)
        }

    }

    fun showSubjects() {
        showLoading()
        val items = ArrayList<String>()
//        items.add(AppConstants.SUBJECT_SCIENCE)
//        items.add(AppConstants.SUBJECT_COMPUTER_SCIENCE)
//        items.add(AppConstants.SUBJECT_ACCOUNTANCY)
//        items.add(AppConstants.SUBJECT_BIOLOGY)
//        items.add(AppConstants.SUBJECT_BUSINESS)
//        items.add(AppConstants.SUBJECT_SOCIAL_STUDIES)
//        items.add(AppConstants.SUBJECT_CHEMISTRY)
//        items.add(AppConstants.SUBJECT_ECONOMICS)
//        items.add(AppConstants.SUBJECT_LANGUAGES)
//        items.add(AppConstants.SUBJECT_PHYSICS)
//        items.add(AppConstants.SUBJECT_MATHS)
//        items.add(AppConstants.SUBJECT_ENGLISH)

        firebaseStore.collection(getString(R.string.db_root_subjects)).get().addOnSuccessListener { documentSnapshot ->
            hideLoading()
            subjectList = documentSnapshot.toObjects(SubjectData::class.java) as ArrayList<SubjectData>
            for (i in 0 until subjectList.size) {
                items.add(subjectList[i]?.name!!)
            }
            showDialog(items, mBinding.selectSubject, "Select Subject", selectedSub)

        }.addOnFailureListener { e ->
            hideLoading()
            showSnackError(e.message)
        }
    }

    fun openBatchOptions() {
        if (isVerified()) {
            saveBatchData()

        }
    }

    private fun fetchTutorData() {
        firebaseStore.collection(getString(R.string.db_root_tutors)).document(appPreferenceHelper.getuserID()).get()
                .addOnSuccessListener { documentSnapshot ->
                    tutorData = documentSnapshot.toObject(TutorData::class.java)!!
                }
                .addOnFailureListener { e ->
                    showSnackError(e.message)
                }
    }

    private fun saveBatchData() {
        showLoading()
        val batchesModel = BatchesModel()

        batchesModel.title = mBinding.batchName.text.toString()

        val calendarStartDate = Calendar.getInstance()
        calendarStartDate.add(Calendar.HOUR, mBinding.startTime.text.toString().split(":")[0].toInt());
        calendarStartDate.add(Calendar.MINUTE, mBinding.startTime.text.toString().split(":")[1].toInt());

        val calendarEndDate = Calendar.getInstance()
        calendarEndDate.add(Calendar.HOUR, mBinding.startTime.text.toString().split(":")[0].toInt());
        calendarEndDate.add(Calendar.MINUTE, mBinding.startTime.text.toString().split(":")[1].toInt());

        batchesModel.timing?.startTime = Timestamp(calendarStartDate.time)

        batchesModel.timing?.endTime = Timestamp(calendarEndDate.time)

        batchesModel.subject?.name = mBinding.selectSubject.text.toString()
        batchesModel.subject?.id = subjectList[selectedSubPosition].id

        batchesModel.grade?.name = mBinding.selectClass.text.toString()
        batchesModel.grade?.id = gradesList[selectedClassPosition].id

        batchesModel.teacher?.id = tutorData.id
        batchesModel.teacher?.name = tutorData.personInfo?.firstName + " " + tutorData.personInfo?.lastName
        batchesModel.teacher?.instituteName = tutorData.academicInfo?.instituteNeme
        batchesModel.teacher?.accountPicture = tutorData.personInfo?.accountPicture
        batchesModel.teacher?.bioData = tutorData.personInfo?.biodata

        batchesModel.batchCode = tutorData.personInfo?.firstName.toString().toCharArray()[0].toString().toUpperCase() +
                tutorData.personInfo?.lastName.toString().toCharArray()[0].toString().toUpperCase() +
                mBinding.batchName.text.toString().toCharArray()[0].toString().toUpperCase() +
                mBinding.batchName.text.toString().toCharArray()[1].toString().toUpperCase() +
                mBinding.selectSubject.text.toString().toCharArray()[0].toString().toUpperCase() +
                mBinding.selectSubject.text.toString().toCharArray()[1].toString().toUpperCase() +
                (((Math.random() * 9000) + 1000).toInt())

        firebaseStore.collection(getString(R.string.db_root_batches)).add(batchesModel).addOnSuccessListener {
            hideLoading()
            showSnackError("Batch Added successfully!!")
            homeListener.showBatchOptionsFragment(batchesModel)
        }.addOnFailureListener { e ->
            hideLoading()
            showSnackError(e.toString())
        }
    }

    fun onDaySelected(view: View) {
        if ((view as TextView).currentTextColor == resources.getColor(R.color.black)) {
            view.background = resources.getDrawable(R.drawable.bg_round_blue, null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setTextColor(resources.getColor(com.autohub.skln.R.color.white, null))
            } else {
                view.setTextColor(resources.getColor(com.autohub.skln.R.color.white))
            }
            counter++
        } else {
            view.background = resources.getDrawable(R.drawable.bg_round_black, null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setTextColor(resources.getColor(R.color.black, null))
            } else {
                view.setTextColor(resources.getColor(R.color.black))
            }
            counter--
        }
    }

    private fun showDialog(items: ArrayList<String>, textView: TextView, title: String, selectedItems: ArrayList<String>) {
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


        AlertDialog.Builder(requireContext())
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
                    if (title.contains("Class")) {
                        selectedClassPosition = selectedPosition
                    } else {
                        selectedSubPosition = selectedPosition
                    }
                }
                .show()
    }

    private fun isVerified(): Boolean {
        if (mBinding.batchName.text.isEmpty()) {
            showSnackError("Please add name of batch.")
            return false
        } else if (mBinding.startTime.text.isEmpty()) {
            showSnackError("Please select Start Time.")
            return false

        } else if (mBinding.endTime.text.isEmpty()) {
            showSnackError("Please select End Time.")
            return false

        } else if (mBinding.selectClass.text.isEmpty()) {
            showSnackError("Please select Class.")
            return false

        } else if (mBinding.selectSubject.text.isEmpty()) {
            showSnackError("Please select Subject.")
            return false

        } else if (counter == 0) {
            showSnackError("Please select day(s).")
            return false
        } else {
            return true

        }

    }


    companion object {
        fun newInstance(): AddBatchFragment = AddBatchFragment()
    }
}
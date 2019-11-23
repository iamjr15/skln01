package com.autohub.tutormodule.ui.manageClasses

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
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
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.FragmentTutorAddBatchBinding
import com.autohub.tutormodule.ui.dashboard.listner.HomeListener
import com.autohub.tutormodule.ui.utils.AppUtils
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
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

    private var selectedDaysNames = ArrayList<String>()

    private var selectedSubPosition: Int = 0
    private var selectedClassPosition: Int = 0
    private var counter = 0
    lateinit var tutorData: TutorData
    lateinit var batch: BatchesModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_tutor_add_batch, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentTutorAddBatchBinding.bind(view)
        mBinding.callback = this

        fetchData()


        if (!this.arguments?.isEmpty!!) {
            isAddBatch = this.arguments!!.getBoolean("showAddBatch")
        }

        if (!isAddBatch) {
            mBinding.textHeading.text = resources.getString(R.string.edit_schedule)
            batch = this.arguments!!.getParcelable("batch")!!
            setData()
        } else {
            mBinding.textHeading.text = resources.getString(R.string.add_batch)
            batch = BatchesModel()
        }

        mBinding.done.setOnClickListener {
            if (isVerified()) {
                if (!isAddBatch) {
                    addBatch()
                } else {
                    addBatch()
                }
            }
        }
    }


    private fun setData() {
        mBinding.batchName.setText(batch.title.toString())

        mBinding.startTime.text = AppUtils.uTCToLocal("EEE MMM dd HH:mm:ss z yyyy",
                "EEE, d MMM yyyy kk:mm:ss z",
                batch.timing?.startTime!!.toDate().toString()).toString()


        mBinding.endTime.text = AppUtils.uTCToLocal("EEE MMM dd HH:mm:ss z yyyy",
                "EEE, d MMM yyyy kk:mm:ss z",
                batch.timing?.endTime!!.toDate().toString()).toString()

        mBinding.selectClass.text = batch.grade?.name?.replace("_", " ")
        mBinding.selectSubject.text = batch.subject?.name

        if (batch.selectedDays != null) {
            addSelectionToDays()
        }
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
            (view as TextView).text = SimpleDateFormat("HH:mm", Locale.US).format(cal.time)
        }
        TimePickerDialog(context, R.style.DialogTheme, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }


    fun showClasses() {
        val items = ArrayList<String>()

        for (i in 0 until gradesList.size) {
            items.add("Class " + gradesList[i].grade!!)
        }
        showDialog(items, mBinding.selectClass, "Select Class", selectedClass)
    }

    fun showSubjects() {
        val items = ArrayList<String>()

        for (i in 0 until subjectList.size) {
            items.add(subjectList[i].name!!)
        }
        showDialog(items, mBinding.selectSubject, "Select Subject", selectedSub)
    }


    private fun fetchData() {
        showLoading()
        firebaseStore.collection(getString(R.string.db_root_grades)).get().addOnSuccessListener { documentSnapshot ->
            val data = documentSnapshot.toObjects(GradeData::class.java)
            gradesList = ArrayList(data.sortedWith(compareBy { it.grade?.toInt() }))

        }.addOnFailureListener { e ->
            showSnackError(e.message)
        }


        firebaseStore.collection(getString(R.string.db_root_subjects)).get().addOnSuccessListener { documentSnapshot ->
            subjectList = documentSnapshot.toObjects(SubjectData::class.java) as ArrayList<SubjectData>

        }.addOnFailureListener { e ->
            showSnackError(e.message)
        }


        firebaseStore.collection(getString(R.string.db_root_tutors)).document(appPreferenceHelper.getuserID()).get()
                .addOnSuccessListener { documentSnapshot ->
                    hideLoading()
                    tutorData = documentSnapshot.toObject(TutorData::class.java)!!
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }
    }

    private fun addBatch() {
        showLoading()
        val batchesModel = BatchesModel()

        batchesModel.title = mBinding.batchName.text.toString()

        val calendarStartDate = Calendar.getInstance()
        calendarStartDate.add(Calendar.HOUR, mBinding.startTime.text.toString().split(":")[0].toInt())
        calendarStartDate.add(Calendar.MINUTE, mBinding.startTime.text.toString().split(":")[1].toInt())

        val calendarEndDate = Calendar.getInstance()
        calendarEndDate.add(Calendar.HOUR, mBinding.endTime.text.toString().split(":")[0].toInt())
        calendarEndDate.add(Calendar.MINUTE, mBinding.endTime.text.toString().split(":")[1].toInt())

        batchesModel.id = UUID.randomUUID().toString()
        batchesModel.timing?.startTime = Timestamp(calendarStartDate.time)

        batchesModel.timing?.endTime = Timestamp(calendarEndDate.time)

        batchesModel.subject?.name = mBinding.selectSubject.text.toString()
        batchesModel.subject?.id = subjectList[selectedSubPosition].id

        batchesModel.grade?.name = "Class_" + gradesList[selectedClassPosition].grade
        batchesModel.grade?.id = gradesList[selectedClassPosition].id

        batchesModel.status = AppConstants.STATUS_ACTIVE

        batchesModel.teacher?.id = tutorData.id
        batchesModel.teacher?.name = tutorData.personInfo?.firstName + " " + tutorData.personInfo?.lastName
        batchesModel.teacher?.instituteName = tutorData.academicInfo?.instituteNeme
        batchesModel.teacher?.accountPicture = tutorData.personInfo?.accountPicture
        batchesModel.teacher?.bioData = tutorData.personInfo?.biodata
        batchesModel.selectedDays = selectedDaysNames

        batchesModel.batchCode = tutorData.personInfo?.firstName.toString().toCharArray()[0].toString().toUpperCase() +
                tutorData.personInfo?.lastName.toString().toCharArray()[0].toString().toUpperCase() +
                mBinding.batchName.text.toString().toCharArray()[0].toString().toUpperCase() +
                mBinding.batchName.text.toString().toCharArray()[1].toString().toUpperCase() +
                mBinding.selectSubject.text.toString().toCharArray()[0].toString().toUpperCase() +
                mBinding.selectSubject.text.toString().toCharArray()[1].toString().toUpperCase() +
                (((Math.random() * 9000) + 1000).toInt())

        if (!isAddBatch) {
            batchesModel.documentId = batch.documentId;
            firebaseStore.collection(getString(R.string.db_root_batches)).document(batch.documentId!!).update(
                    "title", batchesModel.title,
                    "timing.startTime", batchesModel.timing?.startTime,
                    "timing.startTime", batchesModel.timing?.endTime,
                    "grade.name", "Class_" + gradesList[selectedClassPosition].grade,
                    "grade.id", gradesList[selectedClassPosition].id,
                    "subject.name", batchesModel.subject?.name,
                    "subject.name", batchesModel.subject?.name,
                    "selectedDays", batchesModel.selectedDays).addOnSuccessListener {
                hideLoading()
                showSnackError("Batch updated successfully!!")
            }.addOnFailureListener { e ->
                hideLoading()
                showSnackError(e.toString())
            }
        } else {
            firebaseStore.collection(getString(R.string.db_root_batches)).add(batchesModel).
                    addOnSuccessListener {
                batchesModel.documentId = it.id
                hideLoading()
                showSnackError("Batch Added successfully!!")

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Hi ,Below is your Batch Code for " +
                            mBinding.selectSubject.text.toString().toUpperCase() + " Batch.\n\n" + batchesModel.batchCode)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, "Share Batch Code.")
                startActivity(shareIntent)

                homeListener.showBatchOptionsFragment(batchesModel)
                homeListener.refreshSchedule()
            }.addOnFailureListener { e ->
                hideLoading()
                showSnackError(e.toString())
            }
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
            selectedDaysNames.add(view.text.toString())
        } else {
            view.background = resources.getDrawable(R.drawable.bg_round_black, null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.setTextColor(resources.getColor(R.color.black, null))
            } else {
                view.setTextColor(resources.getColor(R.color.black))
            }
            counter--
            selectedDaysNames.remove(view.text.toString())
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

    private fun addSelectionToDays() {
        for (i in 0 until batch.selectedDays.size) {
            if (batch.selectedDays[i].contains(AppConstants.DAY_MON)) {
                onDaySelected(mBinding.mon)
            }
            if (batch.selectedDays[i].contains(AppConstants.DAY_TUE)) {
                onDaySelected(mBinding.tue)
            }
            if (batch.selectedDays[i].contains(AppConstants.DAY_WED)) {
                onDaySelected(mBinding.wed)
            }
            if (batch.selectedDays[i].contains(AppConstants.DAY_THU)) {
                onDaySelected(mBinding.thu)
            }
            if (batch.selectedDays[i].contains(AppConstants.DAY_FRI)) {
                onDaySelected(mBinding.fri)
            }
            if (batch.selectedDays[i].contains(AppConstants.DAY_SAT)) {
                onDaySelected(mBinding.sat)
            }
            if (batch.selectedDays[i].contains(AppConstants.DAY_SUN)) {
                onDaySelected(mBinding.sun)
            }
        }
    }


    companion object {
        fun newInstance(): AddBatchFragment = AddBatchFragment()
    }
}
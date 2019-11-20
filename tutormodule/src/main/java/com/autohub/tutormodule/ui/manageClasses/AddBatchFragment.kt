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
import com.autohub.skln.models.batches.BatchesModel
import com.autohub.skln.models.tutor.TutorData
import com.autohub.skln.utills.AppConstants
import com.autohub.skln.utills.CommonUtils
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.FragmentTutorAddBatchBinding
import com.autohub.tutormodule.ui.dashboard.listner.HomeListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddBatchFragment : BaseFragment() {

    private lateinit var mBinding: FragmentTutorAddBatchBinding
    private lateinit var homeListener: HomeListener

    private var isAddBatch: Boolean = true
    private val selectedSub = ArrayList<String>()
    private val selectedClass = ArrayList<String>()
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

        showDialog(items, mBinding.selectClass, "Select Class", selectedClass)

    }

    fun showSubjects() {
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

        showDialog(items, mBinding.selectSubject, "Select Subject", selectedSub)

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

//        batchesModel.timing.startTime = firebase.firestore.Timestamp mBinding . startTime . text . toString ()
//        batchesModel.timing.endTime = mBinding.endTime.text.toString() as Timestamp

        batchesModel.subject?.name = mBinding.selectSubject.text.toString()

        batchesModel.grade?.name = mBinding.selectClass.text.toString()

        batchesModel.teacher?.id = tutorData.id
        batchesModel.teacher?.name = tutorData.personInfo?.firstName + " " + tutorData.personInfo?.lastName
        batchesModel.teacher?.instituteName = tutorData.academicInfo?.instituteNeme
        batchesModel.teacher?.accountPicture = tutorData.personInfo?.accountPicture
        batchesModel.teacher?.bioData = tutorData.personInfo?.biodata

        batchesModel.batchCode = mBinding.batchName.text.toString().toCharArray()[0] +
                mBinding.batchName.text.toString().toCharArray()[1].toString() +
                mBinding.selectSubject.text.toString().toCharArray()[0].toString() +
                mBinding.selectSubject.text.toString().toCharArray()[1].toString() +
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
package com.autohub.tutormodule.ui.dashboard.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.models.batches.BatchesModel
import com.autohub.skln.models.tutor.TutorData
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.FragmentTutorScheduleBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Vt Netzwelt
 */

class ScheduleFragment : BaseFragment() {

    private lateinit var mBinding: FragmentTutorScheduleBinding
    private lateinit var adapter: ScheduleAdapter
    private lateinit var tutorData: TutorData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_tutor_schedule, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentTutorScheduleBinding.bind(view)
        val dates = getDates("01-01-" + Calendar.getInstance().get(Calendar.YEAR),
                "31-12-" + Calendar.getInstance().get(Calendar.YEAR))

        mBinding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            /*Change position of recycler view with change in seek bar
            * */
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                when {
                    i < 10 -> {
                        mBinding.calendarView.scrollToPosition(0)
                    }
                    i > 90 -> {
                        mBinding.calendarView.scrollToPosition(dates.size - 1)
                    }
                    (i * 3) < (dates.size - 1) -> {
                        mBinding.calendarView.scrollToPosition((i * 3))
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        mBinding.scheduleRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = ScheduleAdapter(requireContext())
        mBinding.scheduleRecyclerView.adapter = adapter
        fetchTutorData()
        initializeCalendarView(dates)

    }

    /*Fetch data of tutor
    * */
    fun fetchTutorData() {
        firebaseStore.collection(getString(R.string.db_root_tutors)).document(appPreferenceHelper.getuserID()).get()
                .addOnSuccessListener { documentSnapshot ->
                    hideLoading()
                    tutorData = documentSnapshot.toObject(TutorData::class.java)!!
                    fetchBatches()
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }
    }

    /*Fetch batches on the basis of tutor id
        * */
    private fun fetchBatches() {
        firebaseStore.collection(getString(R.string.db_root_batches)).whereEqualTo("teacher.id", tutorData.id)
                .get().addOnSuccessListener { documentSnapshot ->
                    val data = documentSnapshot.toObjects(BatchesModel::class.java)
                    adapter.setData(data)
                }
                .addOnFailureListener { e ->
                    showSnackError(e.message)
                }
    }

    private fun initializeCalendarView(dates: List<String>) {
        mBinding.calendarView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        val adapter = ScheduleCalenderAdapter(dates)
        mBinding.calendarView.adapter = adapter

        val timeStamp = SimpleDateFormat("MMM,dd,EEE", Locale.US).format(Calendar.getInstance().time)
        for (i in dates.indices) {
            if (dates[i].contains(timeStamp)) {
                mBinding.seekBar.progress = (i * 0.27).toInt()
                mBinding.calendarView.scrollToPosition(i)
                adapter.selectedPosition = i
                this.adapter.notifyDataSetChanged()
            }
        }
    }


    private fun getDates(dateString1: String, dateString2: String): List<String> {
        val dates = ArrayList<String>()
        val df1 = SimpleDateFormat("dd-MM-yyyy", Locale.US)

        val format = SimpleDateFormat("MMM,dd,EEE", Locale.US)

        lateinit var date1: Date
        lateinit var date2: Date

        try {
            date1 = df1.parse(dateString1)!!
            date2 = df1.parse(dateString2)!!
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val cal1 = Calendar.getInstance()
        cal1.time = date1


        val cal2 = Calendar.getInstance()
        cal2.time = date2

        while (!cal1.after(cal2)) {
            dates.add(format.format(cal1.time))
            cal1.add(Calendar.DATE, 1)
        }
        return dates
    }
}
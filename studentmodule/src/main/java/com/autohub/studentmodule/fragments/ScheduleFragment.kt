package com.autohub.studentmodule.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.autohub.skln.fragment.BaseFragment
import com.autohub.studentmodule.R
import com.autohub.studentmodule.adaptors.ScheduleCalenderAdapter
import com.autohub.studentmodule.adaptors.SchedulesAdaptor
import com.autohub.studentmodule.databinding.FragmentScheduleBinding
import com.autohub.studentmodule.models.BatchesModel
import com.autohub.studentmodule.utils.AppUtil.uTCToLocal
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Vt Netzwelt
 */
class ScheduleFragment : BaseFragment() {
    private lateinit var mBinding: FragmentScheduleBinding
    private lateinit var adaptor: SchedulesAdaptor
    private lateinit var scheduleData: MutableList<BatchesModel>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentScheduleBinding.bind(view)
        val dates = getDates("01-01-" + Calendar.getInstance().get(Calendar.YEAR),
                "31-12-" + Calendar.getInstance().get(Calendar.YEAR))
        scheduleData = ArrayList()
        setUpSeekBar(dates)

        mBinding.schedulerecycleview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adaptor = SchedulesAdaptor(requireContext())
        mBinding.schedulerecycleview.adapter = adaptor
        fetchBatches()
        initializeCalendarView(dates)
        mBinding.swiperefresh.setOnRefreshListener {
            fetchBatches()
        }
    }

    fun fetchBatches() {
        firebaseStore.collection("students").document(appPreferenceHelper.getuserID()).get().addOnSuccessListener {
            if (it["batchCodes"] != null && (it["batchCodes"] as ArrayList<*>).size > 0) {
                val userBatchesCode: ArrayList<String> = it["batchCodes"] as ArrayList<String>

                firebaseStore.collection(getString(R.string.db_root_batches)).whereArrayContains("enrolledStudentsId", firebaseAuth.currentUser!!.uid)
                        .get().addOnCompleteListener { task ->
                            mBinding.swiperefresh.isRefreshing = false
                            if (task.isSuccessful) {
                                scheduleData.clear()
                                for (document in task.result!!) {
                                    val batchesModel = document.toObject(BatchesModel::class.java)

                                    try {
                                        val endTime = uTCToLocal("EEE MMM dd HH:mm:ss z yyyy",
                                                "EEE, d MMM yyyy HH:mm:ss z", batchesModel.timing!!.endTime!!.toDate().toString()
                                        )
                                        val startTime = uTCToLocal("EEE MMM dd HH:mm:ss z yyyy",
                                                "EEE, d MMM yyyy HH:mm:ss z", batchesModel.timing!!.startTime!!.toDate().toString()
                                        ).toString()
                                        batchesModel.batchTiming =
                                                "$startTime - $endTime"
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }

                                    if (userBatchesCode.contains(batchesModel.batchCode)) {
                                        scheduleData.add(batchesModel)


                                    }
                                }

                                updateEmptyview()


                                adaptor.setData(scheduleData)

                            }

                        }
                        .addOnFailureListener { e ->
                            mBinding.swiperefresh.isRefreshing = false
                            showSnackError(e.message)
                        }


            } else {
                mBinding.swiperefresh.isRefreshing = false
                scheduleData.clear()
                adaptor.setData(scheduleData)
                updateEmptyview()


            }
        }.addOnFailureListener()
        {
            mBinding.swiperefresh.isRefreshing = false
            scheduleData.clear()
            adaptor.setData(scheduleData)

        }


    }

    private fun updateEmptyview() {
        if (scheduleData.size > 0) {
            mBinding.rrempty.visibility = View.GONE
            mBinding.seekBar.visibility = View.VISIBLE
        } else {
            mBinding.rrempty.visibility = View.VISIBLE
            mBinding.seekBar.visibility = View.GONE

        }
    }

    private fun setUpSeekBar(dates: List<String>) {

        mBinding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                when {
                    i < 10 -> mBinding.calendarView.scrollToPosition(0)
                    i > 90 -> mBinding.calendarView.scrollToPosition(dates.size - 1)
                    (i * 3) < (dates.size - 1) -> mBinding.calendarView.scrollToPosition((i * 3))
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
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
                adaptor.notifyDataSetChanged()
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

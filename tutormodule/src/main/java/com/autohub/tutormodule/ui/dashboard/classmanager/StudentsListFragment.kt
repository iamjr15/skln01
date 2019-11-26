package com.autohub.tutormodule.ui.dashboard.classmanager


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.autohub.skln.fragment.BaseFragment
import com.autohub.skln.models.UserModel
import com.autohub.skln.models.batchRequests.GradeData
import com.autohub.tutormodule.R
import com.autohub.tutormodule.databinding.FragmentStudentsListBinding

/**
 * A simple [Fragment] subclass.
 */
class StudentsListFragment : BaseFragment() {

    private lateinit var mAdapter: StudentListAdaptor


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_students_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentStudentsListBinding.bind(view)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = StudentListAdaptor(requireContext())
        binding.recyclerView.adapter = mAdapter

        fetchGrades()
    }

    private fun fetchGrades() {
        showLoading()
        firebaseStore.collection(getString(R.string.db_root_grades)).get()
                .addOnSuccessListener { documentSnapshot ->
                    val grades = documentSnapshot.toObjects(GradeData::class.java)
                    fetchStudents(grades)

                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }
    }

    private fun fetchStudents(grades: MutableList<GradeData>) {
        firebaseStore.collection(getString(R.string.db_root_students)).get()
                .addOnSuccessListener { documentSnapshot ->
                    hideLoading()
                    val students = documentSnapshot.toObjects(UserModel::class.java)
                    for (i in 0 until students.size) {
                        for (j in 0 until grades.size) {
                            if (students[i].academicInfo?.selectedClass == grades[j].id) {
                                students[i].className = grades[j].grade?.trim()
                            }
                        }
                    }
                    mAdapter.setData(students)
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    showSnackError(e.message)
                }
    }

}

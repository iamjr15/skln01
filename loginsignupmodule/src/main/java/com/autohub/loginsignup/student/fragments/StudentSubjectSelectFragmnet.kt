package com.autohub.loginsignup.student.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.autohub.SubjectsModel
import com.autohub.loginsignup.R
import com.autohub.loginsignup.databinding.FragmentStudentSubjectSelectBinding
import com.autohub.loginsignup.listners.ClassSelectionListner

/**
 * Created by Vt Netzwelt
 */
class StudentSubjectSelectFragmnet : Fragment() {

    private var mBinding: FragmentStudentSubjectSelectBinding? = null
    private lateinit var datalist: ArrayList<SubjectsModel>
    private lateinit var classSelectionListner: ClassSelectionListner
    private var position: Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        datalist = arguments?.getParcelableArrayList<SubjectsModel>("data") as ArrayList<SubjectsModel>
        position = arguments?.getInt("position", 0)!!
        return inflater.inflate(R.layout.fragment_student_subject_select, container, false)
    }

    override fun onAttach(context: Context) {

        classSelectionListner = context as ClassSelectionListner
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding = FragmentStudentSubjectSelectBinding.bind(view)
        mBinding!!.callback = this

        setUi()
    }

    private fun setUi() {
        mBinding!!.img.setImageResource(datalist[0].icon)
        mBinding!!.txt.text = "${datalist[0].name}. "
        val unwrappedDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.selectclass_bg)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(requireContext(), datalist[0].bloccolor) /* it.color*/)
        mBinding!!.rr.background = wrappedDrawable
        mBinding!!.greenfirst.visibility = if (datalist[0].selected) View.VISIBLE else View.GONE
        mBinding!!.whitefirst.visibility = if (datalist[0].selected) View.GONE else View.VISIBLE


        mBinding!!.imgsecond.setImageResource(datalist[1].icon)
        mBinding!!.txtsecond.text = "${datalist[1].name}. "
        val unwrappedDrawablesecond = ContextCompat.getDrawable(requireContext(), R.drawable.selectclass_bg)
        val wrappedDrawablesecond = DrawableCompat.wrap(unwrappedDrawablesecond!!)
        DrawableCompat.setTint(wrappedDrawablesecond, ContextCompat.getColor(requireContext(), datalist[1].bloccolor) /*it.color*/)
        mBinding!!.rr2.background = wrappedDrawablesecond

        mBinding!!.greensecond.visibility = if (datalist[1].selected) View.VISIBLE else View.GONE
        mBinding!!.whitesecond.visibility = if (datalist[1].selected) View.GONE else View.VISIBLE

    }

    fun onSelectFirst() {
        classSelectionListner.selectedClass(position = position,
                isSecondSelected = false, selectedClass = datalist[0].id!!)
    }

    fun onSelectSecond() {
        classSelectionListner.selectedClass(position = position,
                isSecondSelected = true, selectedClass = datalist[1].id!!)
    }

    fun updateFragment(selected: Boolean, isSecond: Boolean) {

        if (isSecond) {
            datalist[1].selected = selected

        } else {
            datalist[0].selected = selected

        }
        setUi()

    }


    companion object {
        fun newInstance(position: Int, arrayList: ArrayList<SubjectsModel>): StudentSubjectSelectFragmnet {
            val fragmentFirst = StudentSubjectSelectFragmnet()
            val args = Bundle()
            args.putParcelableArrayList("data", arrayList)
            args.putInt("position", position)
            fragmentFirst.arguments = args
            return fragmentFirst
        }
    }
}

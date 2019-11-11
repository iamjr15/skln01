package com.netzwelt.loginsignup.student.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.autohub.skln.listeners.ClassSelectionListner
import com.autohub.skln.models.Classdata
import com.netzwelt.loginsignup.R
import com.netzwelt.loginsignup.databinding.StudenclassFragmentBinding


/**
 * A simple [Fragment] subclass.
 */
class StudentClassFragment : Fragment() {
    private var mBinding: StudenclassFragmentBinding? = null
    lateinit var datalist: ArrayList<Classdata>
    lateinit var classSelectionListner: ClassSelectionListner
    var position: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        datalist = getArguments()?.getParcelableArrayList<Classdata>("data") as ArrayList<Classdata>
        position = getArguments()?.getInt("position", 0)!!
        return inflater.inflate(R.layout.studenclass_fragment, container, false)
    }

    override fun onAttach(context: Context) {

        classSelectionListner = context as ClassSelectionListner
        super.onAttach(context)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = StudenclassFragmentBinding.bind(view)
        mBinding!!.callback = this
        // mBinding.rr

        setUi()
    }

    fun onSelectFirst() {
        classSelectionListner.selectedClass(position = position, isSecondSelected = false, selectedClass = datalist.get(0).classname!!)
    }

    fun onSelectSecond() {
        classSelectionListner.selectedClass(position = position, isSecondSelected = true, selectedClass = datalist.get(1).classname!!)
    }

    fun updateFragment(deselectall: Boolean = false, isSecondSelect: Boolean = false) {
        if (deselectall) {
            datalist[0].selected = false
            datalist[1].selected = false
        } else if (isSecondSelect) {
            datalist[0].selected = false
            datalist[1].selected = true
        } else {
            datalist[0].selected = true
            datalist[1].selected = false
        }

        setUi()


    }

    fun setUi() {
        mBinding!!.img.setImageResource(datalist.get(0).icon)
        mBinding!!.txt.setText("class ${datalist.get(0).classname}. ")
        val unwrappedDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.selectclass_bg)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(requireContext(), datalist.get(0).color) /* it.color*/)
        mBinding!!.rr.setBackground(wrappedDrawable)
        mBinding!!.greenfirst.visibility = if (datalist.get(0).selected) View.VISIBLE else View.GONE
        mBinding!!.whitefirst.visibility = if (datalist.get(0).selected) View.GONE else View.VISIBLE


        mBinding!!.imgsecond.setImageResource(datalist.get(1).icon)
        mBinding!!.txtsecond.setText("class ${datalist.get(1).classname}. ")
        val unwrappedDrawablesecond = ContextCompat.getDrawable(requireContext(), R.drawable.selectclass_bg)
        val wrappedDrawablesecond = DrawableCompat.wrap(unwrappedDrawablesecond!!)
        DrawableCompat.setTint(wrappedDrawablesecond, ContextCompat.getColor(requireContext(), datalist.get(1).color) /*it.color*/)
        mBinding!!.rr2.setBackground(wrappedDrawablesecond)

        mBinding!!.greensecond.visibility = if (datalist.get(1).selected) View.VISIBLE else View.GONE
        mBinding!!.whitesecond.visibility = if (datalist.get(1).selected) View.GONE else View.VISIBLE


    }


    companion object {
        fun newInstance(position: Int, arrayList: ArrayList<Classdata>): StudentClassFragment {
            val fragmentFirst = StudentClassFragment()
            val args = Bundle()
            args.putParcelableArrayList("data", arrayList)
            args.putInt("position", position)
            fragmentFirst.setArguments(args)
            return fragmentFirst
        }
    }
}


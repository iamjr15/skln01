package com.netzwelt.loginsignup.student.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

import com.netzwelt.loginsignup.listners.ClassSelectionListner
import com.autohub.skln.models.HobbiesData
import com.netzwelt.loginsignup.R
import com.netzwelt.loginsignup.databinding.FragmentHobbiesBinding

/**
 * A simple [Fragment] subclass.
 */
class HobbiesFragment : Fragment() {
    private var mBinding: FragmentHobbiesBinding? = null
    lateinit var datalist: ArrayList<HobbiesData>
    lateinit var classSelectionListner: ClassSelectionListner
    var position: Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        datalist = arguments?.getParcelableArrayList<HobbiesData>("data") as ArrayList<HobbiesData>
        position = arguments?.getInt("position", 0)!!
        return inflater.inflate(R.layout.fragment_hobbies, container, false)
    }

    override fun onAttach(context: Context) {

        classSelectionListner = context as ClassSelectionListner
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding = FragmentHobbiesBinding.bind(view)
        mBinding!!.callback = this

        setUi()
    }

    private fun setUi() {
        mBinding!!.img.setImageResource(datalist.get(0).icon)
        mBinding!!.txt.text = "${datalist.get(0).hobbyName}. "
        val unwrappedDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.selectclass_bg)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(requireContext(), datalist.get(0).color) /* it.color*/)
        mBinding!!.rr.background = wrappedDrawable
        mBinding!!.greenfirst.visibility = if (datalist.get(0).selected) View.VISIBLE else View.GONE
        mBinding!!.whitefirst.visibility = if (datalist.get(0).selected) View.GONE else View.VISIBLE


        mBinding!!.imgsecond.setImageResource(datalist.get(1).icon)
        mBinding!!.txtsecond.text = "${datalist.get(1).hobbyName}. "
        val unwrappedDrawablesecond = ContextCompat.getDrawable(requireContext(), R.drawable.selectclass_bg)
        val wrappedDrawablesecond = DrawableCompat.wrap(unwrappedDrawablesecond!!)
        DrawableCompat.setTint(wrappedDrawablesecond, ContextCompat.getColor(requireContext(), datalist.get(1).color) /*it.color*/)
        mBinding!!.rr2.background = wrappedDrawablesecond

        mBinding!!.greensecond.visibility = if (datalist.get(1).selected) View.VISIBLE else View.GONE
        mBinding!!.whitesecond.visibility = if (datalist.get(1).selected) View.GONE else View.VISIBLE

    }

    fun onSelectFirst() {
        classSelectionListner.selectedClass(position = position,
                isSecondSelected = false, selectedClass = datalist.get(0).hobbyName!!)
    }

    fun onSelectSecond() {
        classSelectionListner.selectedClass(position = position,
                isSecondSelected = true, selectedClass = datalist.get(1).hobbyName!!)
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
        fun newInstance(position: Int, arrayList: ArrayList<HobbiesData>): HobbiesFragment {
            val fragmentFirst = HobbiesFragment()
            val args = Bundle()
            args.putParcelableArrayList("data", arrayList)
            args.putInt("position", position)
            fragmentFirst.arguments = args
            return fragmentFirst
        }
    }

}

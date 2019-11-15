package com.autohub.loginsignup.student.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.autohub.loginsignup.R
import com.autohub.loginsignup.databinding.StudenclassFragmentBinding
import com.autohub.loginsignup.listners.ClassSelectionListner
import com.autohub.loginsignup.student.models.Classdata


/**
 * Created by Vt Netzwelt
 */
class StudentClassFragment : Fragment() {
    private var mBinding: StudenclassFragmentBinding? = null
    private lateinit var datalist: ArrayList<Classdata>
    private lateinit var classSelectionListner: ClassSelectionListner
    private var position: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        datalist = arguments?.getParcelableArrayList<Classdata>("data") as ArrayList<Classdata>
        position = arguments?.getInt("position", 0)!!
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
        classSelectionListner.selectedClass(position = position, isSecondSelected = false, selectedClass = datalist[0].classname!!)
    }

    fun onSelectSecond() {
        classSelectionListner.selectedClass(position = position, isSecondSelected = true, selectedClass = datalist[1].classname!!)
    }

    fun updateFragment(deselectall: Boolean = false, isSecondSelect: Boolean = false) {
        when {
            deselectall -> {
                datalist[0].selected = false
                datalist[1].selected = false
            }
            isSecondSelect -> {
                datalist[0].selected = false
                datalist[1].selected = true
            }
            else -> {
                datalist[0].selected = true
                datalist[1].selected = false
            }
        }

        setUi()


    }

    private fun setUi() {
        mBinding!!.img.setImageResource(datalist[0].icon)
        mBinding!!.txt.text = "class ${datalist[0].classname}. "
        val unwrappedDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.selectclass_bg)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(requireContext(), datalist[0].color) /* it.color*/)
        mBinding!!.rr.background = wrappedDrawable
        mBinding!!.greenfirst.visibility = if (datalist[0].selected) View.VISIBLE else View.GONE
        mBinding!!.whitefirst.visibility = if (datalist[0].selected) View.GONE else View.VISIBLE


        mBinding!!.imgsecond.setImageResource(datalist[1].icon)
        mBinding!!.txtsecond.text = "class ${datalist[1].classname}. "
        val unwrappedDrawablesecond = ContextCompat.getDrawable(requireContext(), R.drawable.selectclass_bg)
        val wrappedDrawablesecond = DrawableCompat.wrap(unwrappedDrawablesecond!!)
        DrawableCompat.setTint(wrappedDrawablesecond, ContextCompat.getColor(requireContext(), datalist[1].color) /*it.color*/)
        mBinding!!.rr2.background = wrappedDrawablesecond

        mBinding!!.greensecond.visibility = if (datalist[1].selected) View.VISIBLE else View.GONE
        mBinding!!.whitesecond.visibility = if (datalist[1].selected) View.GONE else View.VISIBLE


    }


    companion object {
        fun newInstance(position: Int, arrayList: ArrayList<Classdata>): StudentClassFragment {
            val fragmentFirst = StudentClassFragment()
            val args = Bundle()
            args.putParcelableArrayList("data", arrayList)
            args.putInt("position", position)
            fragmentFirst.arguments = args
            return fragmentFirst
        }
    }
}



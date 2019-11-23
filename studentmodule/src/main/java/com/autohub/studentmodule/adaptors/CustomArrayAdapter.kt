package com.autohub.studentmodule.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.autohub.studentmodule.R


/**
 * Created by Vt Netzwelt
 */
class CustomArrayAdapter(context: Context,
                         private var gradesarray: Array<String>) : ArrayAdapter<String>(context, R.layout.spinner_row, gradesarray) {

    private fun getCustomView(position: Int, parent: ViewGroup): View {

        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.spinner_row, parent, false)

        val tvLanguage = layout
                .findViewById(R.id.txtview) as TextView

        tvLanguage.text = " grade ${gradesarray[position]}"
        return layout
    }

    override fun getDropDownView(position: Int, convertView: View?,
                                 parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }
}



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

        // Inflating the layout for the custom Spinner
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.spinner_row, parent, false)

        val tvLanguage = layout
                .findViewById(R.id.txtview) as TextView

        tvLanguage.text = " grade ${gradesarray[position]}"
        return layout
    }

    // It gets a View that displays in the drop down popup the data at the specified position
    override fun getDropDownView(position: Int, convertView: View?,
                                 parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    // It gets a View that displays the data at the specified position
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }
}



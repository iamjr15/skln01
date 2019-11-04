package com.autohub.skln.activities.user.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.autohub.skln.R


// Creating an Adapter Class
class CustomArrayAdapter(context: Context,
                        var gradesarray: Array<String>) : ArrayAdapter<String>(context, R.layout.spinner_row, gradesarray) {

    fun getCustomView(position: Int, convertView: View?,
                      parent: ViewGroup): View {

        // Inflating the layout for the custom Spinner
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.spinner_row, parent, false)

        val tvLanguage = layout
                .findViewById(R.id.txtview) as TextView

        tvLanguage.setText(" grade ${gradesarray[position]}")

        // Declaring and Typecasting the textview in the inflated layout
        /*  val tvLanguage = layout
                  .findViewById(R.id.tvLanguage) as TextView*/

        // Setting the text using the array
        /* tvLanguage.setText(Languages[position])

         // Setting the color of the text
         tvLanguage.setTextColor(Color.rgb(75, 180, 225))

         // Declaring and Typecasting the imageView in the inflated layout
         val img = layout.findViewById(R.id.imgLanguage) as ImageView

         // Setting an image using the id's in the array
         img.setImageResource(images[position])

         // Setting Special atrributes for 1st element
         if (position == 0) {
             // Removing the image view
             img.setVisibility(View.GONE)
             // Setting the size of the text
             tvLanguage.textSize = 20f
             // Setting the text Color
             tvLanguage.setTextColor(Color.BLACK)

         }
 */
        return layout
    }

    // It gets a View that displays in the drop down popup the data at the specified position
    override fun getDropDownView(position: Int, convertView: View?,
                                 parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    // It gets a View that displays the data at the specified position
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }
}

/*
class CustomArrayAdapter( var context: Context,var gradeslist: Array<String>) : BaseAdapter() {
    internal var inflter: LayoutInflater

    init {
        inflter = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return gradeslist.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View, viewGroup: ViewGroup): View {
       var view = inflter.inflate(R.layout.spinner_row, null)

        var textview = view.findViewById<View>(R.id.txtview) as TextView
        textview.setText(gradeslist[i])
       */
/* val icon = view.findViewById<View>(R.id.imageView) as ImageView
        val names = view.findViewById<View>(R.id.textView) as TextView
        icon.setImageResource(flags[i])
        names.text = countryNames[i]*//*

        return view
    }
}*/

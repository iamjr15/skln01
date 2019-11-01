package com.autohub.skln.activities.user.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.autohub.skln.R

/**
 * A simple [Fragment] subclass.
 */
class StudentClassFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.studenclass_fragment, container, false)
    }


}

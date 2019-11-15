package com.autohub.studentmodule.listners

import com.autohub.skln.models.RequestViewModel
import com.autohub.skln.models.User

/**
 * Created by Vt Netzwelt
 */
interface HomeListners {

    fun onAcadmicsSelect(user: User, classname: String)
    fun onClassRequestSelectListner(requestViewModel: RequestViewModel)
}
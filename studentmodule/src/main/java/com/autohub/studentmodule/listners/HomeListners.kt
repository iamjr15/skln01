package com.autohub.studentmodule.listners

import com.autohub.skln.models.RequestViewModel
import com.autohub.skln.models.UserModel

/**
 * Created by Vt Netzwelt
 */
interface HomeListners {

    fun onAcadmicsSelect(user: UserModel, classname: String)
    fun onClassRequestSelectListner(requestViewModel: RequestViewModel)
}
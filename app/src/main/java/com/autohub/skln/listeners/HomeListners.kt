package com.autohub.skln.listeners

import com.autohub.skln.models.RequestViewModel
import com.autohub.skln.models.User

interface HomeListners {

    fun onAcadmicsSelect(user: User, classname: String)
    fun onClassRequestSelectListner(requestViewModel: RequestViewModel)
}
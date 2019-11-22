package com.autohub.studentmodule.listners

import com.autohub.skln.models.UserModel
import com.autohub.skln.models.batches.BatchRequestViewModel

/**
 * Created by Vt Netzwelt
 */
interface HomeListners {

    fun onAcadmicsSelect(user: UserModel, classname: String)
    fun onClassRequestSelectListner(requestViewModel: BatchRequestViewModel)
    fun updateScheduleFragment()
}
package com.autohub.skln.models

import androidx.databinding.BaseObservable
import com.autohub.skln.utills.CommonUtils


/**
 * Modified by Vt Netzwelt
 */

class UserViewModel(private var mUser: UserModel) : BaseObservable() {

    val rating: String
        get() = "4.2"


    val fullName: String
        get() = mUser.personInfo!!.firstName + " " + mUser.personInfo!!.lastName

    val firstAndLastNameLetter: String
        get() = mUser.personInfo!!.firstName + " " + (mUser.personInfo!!.lastName!!.substring(0, 1) + ".".toUpperCase())

    val firstName: String
        get() = mUser.personInfo!!.firstName!!

    val lastName: String
        get() = CommonUtils.getString(mUser.personInfo!!.lastName)

    val classesWithAffix: String
        get() = mUser.getClassesWithAffix().replace(",", "-")


    val qualification: String
        get() = ""/*CommonUtils.getString(mUser!!.qualification)*/


    var user: UserModel
        get() = mUser
        set(user) {
            this.mUser = user
            notifyChange()
        }


}

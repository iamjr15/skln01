package com.autohub.skln.models

import android.text.TextUtils

import androidx.databinding.BaseObservable

import com.autohub.skln.utills.CommonUtils


/**
 * Modified by Vt Netzwelt
 */

class UserViewModelold(private var mUser: User) : BaseObservable() {

    val rating: String
        get() = "4.2"


    val fullName: String
        get() = mUser!!.firstName + " " + mUser!!.lastName

    val firstAndLastNameLetter: String
        get() = mUser!!.firstName + " " + (mUser!!.lastName.substring(0, 1) + ".".toUpperCase())

    val firstName: String
        get() = mUser!!.firstName

    val lastName: String
        get() = CommonUtils.getString(mUser!!.lastName)

    val classesWithAffix: String
        get() = mUser!!.classesWithAffix.replace(",", "-")

    val classesToTeach: String
        get() = CommonUtils.getString(mUser!!.classesToTeach)

    val classType: String
        get() = CommonUtils.getString(mUser!!.classType)

    val classFrequency: String
        get() = CommonUtils.getString(mUser!!.classFrequency)

    val maxStudentsCapacity: String
        get() = CommonUtils.getString(mUser!!.maxStudentsCapacity)

    val subjectsToTeach: String
        get() = CommonUtils.getString(mUser!!.subjectsToTeach.trim { it <= ' ' }).replace(",", " | ")

    val subjectsToTeachAsArray: Array<String>
        get() = mUser!!.subjectsToTeach.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

    val subjectOrHobbiesToTeach: String
        get() = if (TextUtils.isEmpty(subjectsToTeach)) {
            hobbiesToTeach
        } else subjectsToTeach

    val hobbiesToTeach: String
        get() = CommonUtils.getString(mUser!!.hobbiesToTeach)

    val occupation: String
        get() = CommonUtils.getString(mUser!!.occupation)

    val teachingExp: String
        get() = CommonUtils.getString(mUser!!.experience)

    val noOfClasses: String
        get() = CommonUtils.getString(mUser!!.noOfClasses) + " / " + mUser!!.classFrequency

    val qualification: String
        get() = CommonUtils.getString(mUser!!.qualification)

    val areaQualification: String
        get() = CommonUtils.getString(mUser!!.areaQualification)

    val board: String
        get() = CommonUtils.getString(mUser!!.board)

    var bioData: String
        get() = CommonUtils.getString(mUser!!.bioData)
        set(bio) {
            mUser!!.bioData = bio
        }


    val costPerClasses: String
        get() = String.format("RS %1\$s/%2\$s Classes per %3\$s", mUser!!.rate, mUser!!.noOfClasses, mUser!!.paymentDuration)

    /* String.format("RS %1$s / %2$s", mUser.rate, mUser.paymentDuration);*/ val costPerDuration: String
        get() = "$ " + mUser!!.rate + "/ " + mUser!!.noOfClasses + " classes " + mUser!!.paymentDuration

    val city: String
        get() = CommonUtils.getString(mUser!!.city)

    var user: User
        get() = mUser
        set(user) {
            this.mUser = user
            notifyChange()
        }

    val userId: String
        get() = mUser!!.id

    fun gettDistance(): Double? {
        return mUser!!.distance
    }

    fun setClasses(classesToTeach: String) {
        mUser!!.classesToTeach = classesToTeach
        notifyChange()
    }
}

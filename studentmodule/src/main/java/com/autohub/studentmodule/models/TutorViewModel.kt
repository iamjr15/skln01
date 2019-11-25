package com.autohub.studentmodule.models

import androidx.databinding.BaseObservable
import com.autohub.skln.models.tutor.TutorData
import com.autohub.skln.utills.CommonUtils

/**
 * Created by Vt Netzwelt
 */

class TutorViewModel(private var mUser: TutorData) : BaseObservable() {

    val rating: String
        get() = "4.2"


    val fullName: String
        get() = mUser.personInfo!!.firstName + " " + mUser.personInfo!!.lastName

    val firstAndLastNameLetter: String
        get() = CommonUtils.capitalize(mUser.personInfo!!.firstName + " " + (mUser.personInfo!!.lastName!!.substring(0, 1) + ".".toUpperCase()))

/*    val firstName: String
        get() = mUser.personInfo!!.firstName!!

    val lastName: String
        get() = CommonUtils.getString(mUser.personInfo!!.lastName)*/

    val classesWithAffix: String
        get() = mUser.getClassesWithAffix().replace(",", "-")

/*    val classesToTeach: String
        get() = CommonUtils.getString(mUser.classToTeach)*/

    val classType: String
        get() {

            if (mUser.qualification!!.classType!!.size > 0) {
                val list = mUser.qualification!!.classType!!
                val classtypebuilder = StringBuilder()
                for (i in list) {
                    classtypebuilder.append("/$i")
                }

                return classtypebuilder.toString().removeRange(0..0)
            }
            return ""


        }

    /*{

            CommonUtils.getString(mUser.academicInfo!!.classType)
        }*/

/*    val classFrequency: String
        get() = CommonUtils.getString(mUser.packageInfo!!.frequency)*/

    val maxStudentsCapacity: String
        get() = CommonUtils.getString(mUser.packageInfo!!.studentCapacity)

    val subjectsToTeach: String
        get() = CommonUtils.getString(mUser.subjectsToTeach!!.trim { it <= ' ' }).replace(",", " | ")

//    val subjectsToTeachAsArray: Array<String>
//        get() = mUser.subjectsToTeach!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//
//    val subjectOrHobbiesToTeach: String
//        get() = if (TextUtils.isEmpty(subjectsToTeach)) {
//            /*hobbiesToTeach*/""
//        } else subjectsToTeach

//    val hobbiesToTeach: String
//        get() = CommonUtils.getString(mUser!!.hobbiesToTeach)

    val occupation: String
        get() = CommonUtils.getString(mUser.qualification!!.currentOccupation)

    val teachingExp: String
        get() = CommonUtils.getString(mUser.qualification!!.experience)

    val noOfClasses: String
        //pemnoofclasses
        get() = CommonUtils.getString(mUser.packageInfo!!.occurances) + " / " + mUser.packageInfo!!.frequency

    val qualification: String
        get() = CommonUtils.getString(mUser.qualification!!.qualification)

    val areaQualification: String
        get() = CommonUtils.getString(mUser.qualification!!.qualificationArea)

//    val board: String
//        get() = CommonUtils.getString(mUser.qualification!!.targetBoard)

    var bioData: String
        get() = CommonUtils.getString(mUser.personInfo!!.biodata).replace("\n", "")
        set(bio) {
            mUser.personInfo!!.biodata = bio
        }

    //noOfClasses pen

//    val costPerClasses: String
//        get() = String.format("RS %1\$s/%2\$s Classes per %3\$s", mUser.packageInfo!!.price, mUser.packageInfo!!.frequency
//                , "pending")

    val costPerDuration: String
        get() = "$ " + mUser.packageInfo!!.price + "/ " + mUser.packageInfo!!.occurances + " classes " + mUser.packageInfo!!.rateOption

//    val city: String
//        get() = CommonUtils.getString("pen")

    var user: TutorData
        get() = mUser
        set(user) {
            this.mUser = user
            notifyChange()
        }

    val userId: String
        get() = mUser.id!!

    fun gettDistance(): String? {
        return "${mUser.distance} km"
    }

//    fun setClasses(classesToTeach: String) {
//        mUser.classToTeach = classesToTeach
//        notifyChange()
//    }
}

package com.autohub.skln.models

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import com.autohub.skln.R
import com.autohub.skln.utills.AppConstants
import com.autohub.skln.utills.CommonUtils
import java.util.*

data class UserModel(var academicInfo: AcademicInformation? = AcademicInformation(),
                     var id: String? = "", var personInfo: PersonalInformation? = PersonalInformation()


) : Parcelable {

    private val FAVORITE_CLASSES = HashMap<String, Int>()
    private val ACADMICS_DATA_HASH_MAP = HashMap<String, AcadmicsData>()
    private val ACADMICS_SENIORDATA_HASH_MAP = HashMap<String, AcadmicsData>()
    private val HOBBIES_DATA_HASH_MAP = HashMap<String, HobbiesData>()

    constructor(parcel: Parcel) : this(
            TODO("academicInfo"),
            parcel.readString(),
            TODO("personInfo"))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }

    init {
        FAVORITE_CLASSES[AppConstants.SUBJECT_SCIENCE] = R.drawable.microscope
        FAVORITE_CLASSES[AppConstants.SUBJECT_ENGLISH] = R.drawable.noun
        FAVORITE_CLASSES[AppConstants.SUBJECT_MATHS] = R.drawable.geometry
        FAVORITE_CLASSES[AppConstants.SUBJECT_SOCIAL_STUDIES] = R.drawable.strike
        FAVORITE_CLASSES[AppConstants.SUBJECT_LANGUAGES] = R.drawable.language
        FAVORITE_CLASSES[AppConstants.SUBJECT_COMPUTER_SCIENCE] = R.drawable.informatic

        FAVORITE_CLASSES[AppConstants.SUBJECT_PHYSICS] = R.drawable.physics
        FAVORITE_CLASSES[AppConstants.SUBJECT_BIOLOGY] = R.drawable.microscope
        FAVORITE_CLASSES[AppConstants.SUBJECT_CHEMISTRY] = R.drawable.test_tube
        FAVORITE_CLASSES[AppConstants.SUBJECT_BUSINESS] = R.drawable.rupee
        FAVORITE_CLASSES[AppConstants.SUBJECT_ACCOUNTANCY] = R.drawable.accounting
        FAVORITE_CLASSES[AppConstants.SUBJECT_ECONOMICS] = R.drawable.rating


        ACADMICS_DATA_HASH_MAP[AppConstants.SUBJECT_SCIENCE] = AcadmicsData(R.color.science, AppConstants.SUBJECT_SCIENCE, R.drawable.microscope)
        ACADMICS_DATA_HASH_MAP[AppConstants.SUBJECT_ENGLISH] = AcadmicsData(R.color.english, AppConstants.SUBJECT_ENGLISH, R.drawable.noun)
        ACADMICS_DATA_HASH_MAP[AppConstants.SUBJECT_MATHS] = AcadmicsData(R.color.math, AppConstants.SUBJECT_MATHS, R.drawable.geometry)

        ACADMICS_DATA_HASH_MAP[AppConstants.SUBJECT_SOCIAL_STUDIES] = AcadmicsData(R.color.socialstudies, AppConstants.SUBJECT_SOCIAL_STUDIES, R.drawable.strike)
        ACADMICS_DATA_HASH_MAP[AppConstants.SUBJECT_LANGUAGES] = AcadmicsData(R.color.language, AppConstants.SUBJECT_LANGUAGES, R.drawable.language)
        ACADMICS_DATA_HASH_MAP[AppConstants.SUBJECT_COMPUTER_SCIENCE] = AcadmicsData(R.color.computerscience, AppConstants.SUBJECT_COMPUTER_SCIENCE, R.drawable.informatic)


        ACADMICS_SENIORDATA_HASH_MAP[AppConstants.SUBJECT_ENGLISH] = AcadmicsData(R.color.english, AppConstants.SUBJECT_ENGLISH, R.drawable.noun)
        ACADMICS_SENIORDATA_HASH_MAP[AppConstants.SUBJECT_MATHS] = AcadmicsData(R.color.math, AppConstants.SUBJECT_MATHS, R.drawable.geometry)
        ACADMICS_SENIORDATA_HASH_MAP[AppConstants.SUBJECT_COMPUTER_SCIENCE] = AcadmicsData(R.color.computerscience, AppConstants.SUBJECT_COMPUTER_SCIENCE, R.drawable.informatic)

        ACADMICS_SENIORDATA_HASH_MAP[AppConstants.SUBJECT_PHYSICS] = AcadmicsData(R.color.physics, AppConstants.SUBJECT_PHYSICS, R.drawable.physics)
        ACADMICS_SENIORDATA_HASH_MAP[AppConstants.SUBJECT_BIOLOGY] = AcadmicsData(R.color.biology, AppConstants.SUBJECT_BIOLOGY, R.drawable.microscope)
        ACADMICS_SENIORDATA_HASH_MAP[AppConstants.SUBJECT_CHEMISTRY] = AcadmicsData(R.color.chemistry, AppConstants.SUBJECT_CHEMISTRY, R.drawable.test_tube)
        ACADMICS_SENIORDATA_HASH_MAP[AppConstants.SUBJECT_BUSINESS] = AcadmicsData(R.color.business, AppConstants.SUBJECT_BUSINESS, R.drawable.rupee)
        ACADMICS_SENIORDATA_HASH_MAP[AppConstants.SUBJECT_ACCOUNTANCY] = AcadmicsData(R.color.account, AppConstants.SUBJECT_ACCOUNTANCY, R.drawable.accounting)
        ACADMICS_SENIORDATA_HASH_MAP[AppConstants.SUBJECT_ECONOMICS] = AcadmicsData(R.color.economics, AppConstants.SUBJECT_ECONOMICS, R.drawable.rating)


        HOBBIES_DATA_HASH_MAP[AppConstants.HOBBY_DANCE] = HobbiesData(R.color.dance, AppConstants.HOBBY_DANCE, R.drawable.dancing, false)
        HOBBIES_DATA_HASH_MAP[AppConstants.HOBBY_DRUM] = HobbiesData(R.color.drum, AppConstants.HOBBY_DRUM, R.drawable.drum, false)
        HOBBIES_DATA_HASH_MAP[AppConstants.HOBBY_GUITAR] = HobbiesData(R.color.guitar, AppConstants.HOBBY_GUITAR, R.drawable.guitar, false)
        HOBBIES_DATA_HASH_MAP[AppConstants.HOBBY_KEYBOARD] = HobbiesData(R.color.keyboard, AppConstants.HOBBY_KEYBOARD, R.drawable.piano, false)
        HOBBIES_DATA_HASH_MAP[AppConstants.HOBBY_MARTIAL] = HobbiesData(R.color.materialarts, AppConstants.HOBBY_MARTIAL, R.drawable.attack, false)
        HOBBIES_DATA_HASH_MAP[AppConstants.HOBBY_PAINT] = HobbiesData(R.color.painting, AppConstants.HOBBY_PAINT, R.drawable.brush, false)

    }


    fun getAcadmics(): List<AcadmicsData> {

        val isSeniorClass = academicInfo!!.selectedClass == AppConstants.CLASS_11 || academicInfo!!.selectedClass == AppConstants.CLASS_12
        val acadmicsDataList = ArrayList<AcadmicsData>()
        //        if (TextUtils.isEmpty(favoriteClasses)) return acadmicsDataList;
        //        String[] subjects = favoriteClasses.split(",");

        if (isSeniorClass) {
            for ((key) in ACADMICS_SENIORDATA_HASH_MAP) {
                acadmicsDataList.add(ACADMICS_SENIORDATA_HASH_MAP[key]!!)
            }


        } else {

            for ((key) in ACADMICS_DATA_HASH_MAP) {
                acadmicsDataList.add(ACADMICS_DATA_HASH_MAP[key]!!)
            }
        }
        return acadmicsDataList
    }


    fun getHobbies(): List<HobbiesData> {
        val hobbies = ArrayList<HobbiesData>()

        for ((key) in HOBBIES_DATA_HASH_MAP) {
            hobbies.add(HOBBIES_DATA_HASH_MAP[key]!!)
        }


        return hobbies
    }


    fun getClassesWithAffix(): String {
        if (TextUtils.isEmpty(academicInfo!!.selectedClass)) return ""
        val builder = StringBuilder()
        val classes = academicInfo!!.selectedClass!!.split(",")
        for (clazz in classes) {
            var value = clazz.trim({ it <= ' ' })
            builder.append(value).append(CommonUtils.getClassSuffix(Integer.parseInt(value))).append(", ")
        }
        return builder.substring(0, builder.length - 2)
    }

}
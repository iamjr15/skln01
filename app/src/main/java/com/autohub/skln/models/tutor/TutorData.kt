package com.autohub.skln.models.tutormodels

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import com.autohub.skln.models.tutor.TutorLocation
import com.autohub.skln.models.tutor.TutorPackageInfo
import com.autohub.skln.utills.CommonUtils


data class TutorData(var academicInfo: TutorAcademicInfo? = TutorAcademicInfo(),
                     var id: String? = "",
                     var location: TutorLocation? = TutorLocation(),
                     var otpPassword: TutorOtpPassword? = TutorOtpPassword(),
                     var packageInfo: TutorPackageInfo? = TutorPackageInfo(),
                     var personInfo: TutorPersonalInfo? = TutorPersonalInfo(),
                     var qualification: TutorQualification? = TutorQualification(),
                     var classToTeach: String? = "",
                     var subjectsToTeach: String? = "",
                     var distance: Double? = 0.0
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(TutorAcademicInfo::class.java.classLoader),
            parcel.readString(),
            parcel.readParcelable(TutorLocation::class.java.classLoader),
            parcel.readParcelable(TutorOtpPassword::class.java.classLoader),
            parcel.readParcelable(TutorPackageInfo::class.java.classLoader),
            parcel.readParcelable(TutorPersonalInfo::class.java.classLoader),
            parcel.readParcelable(TutorQualification::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(academicInfo, flags)
        parcel.writeString(id)
        parcel.writeParcelable(location, flags)
        parcel.writeParcelable(otpPassword, flags)
        parcel.writeParcelable(packageInfo, flags)
        parcel.writeParcelable(personInfo, flags)
        parcel.writeParcelable(qualification, flags)
        parcel.writeString(classToTeach)
        parcel.writeString(subjectsToTeach)
        parcel.writeValue(distance)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TutorData> {
        override fun createFromParcel(parcel: Parcel): TutorData {
            return TutorData(parcel)
        }

        override fun newArray(size: Int): Array<TutorData?> {
            return arrayOfNulls(size)
        }
    }


    fun getClassesWithAffix(): String {
        if (TextUtils.isEmpty(classToTeach)) return ""
        val builder = StringBuilder()
        val classes = classToTeach!!.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        for (clazz in classes) {
            var value = clazz.trim({ it <= ' ' })
            builder.append(value).append(CommonUtils.getClassSuffix(Integer.parseInt(value))).append(", ")
        }
        return builder.substring(0, builder.length - 2)
    }
}
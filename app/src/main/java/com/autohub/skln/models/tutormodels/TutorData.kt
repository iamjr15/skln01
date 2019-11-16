package com.autohub.skln.models.tutormodels

import android.os.Parcel
import android.os.Parcelable


data class TutorData(var academicInfo: TutorAcademicInfo? = TutorAcademicInfo(),
                     var id: String? = "",
                     var location: TutorLocation? = TutorLocation(),
                     var otpPassword: TutorOtpPassword? = TutorOtpPassword(),
                     var packageInfo: TutorPackageInfo? = TutorPackageInfo(),
                     var personInfo: TutorPersonalInfo? = TutorPersonalInfo(),
                     var qualification: TutorQualification? = TutorQualification()) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(TutorAcademicInfo::class.java.classLoader),
            parcel.readString(),
            parcel.readParcelable(TutorLocation::class.java.classLoader),
            parcel.readParcelable(TutorOtpPassword::class.java.classLoader),
            parcel.readParcelable(TutorPackageInfo::class.java.classLoader),
            parcel.readParcelable(TutorPersonalInfo::class.java.classLoader),
            parcel.readParcelable(TutorQualification::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(academicInfo, flags)
        parcel.writeString(id)
        parcel.writeParcelable(location, flags)
        parcel.writeParcelable(otpPassword, flags)
        parcel.writeParcelable(packageInfo, flags)
        parcel.writeParcelable(personInfo, flags)
        parcel.writeParcelable(qualification, flags)
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

}
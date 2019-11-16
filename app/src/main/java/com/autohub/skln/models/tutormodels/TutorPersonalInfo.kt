package com.autohub.skln.models.tutormodels

import android.os.Parcel
import android.os.Parcelable

data class TutorPersonalInfo(var accountPicture: String? = "",
                             var biodata: String? = "",
                             var email: String? = "",
                             var firstName: String? = "",
                             var gender: String? = "",
                             var lastName: String? = "",
                             var phone: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(accountPicture)
        parcel.writeString(biodata)
        parcel.writeString(email)
        parcel.writeString(firstName)
        parcel.writeString(gender)
        parcel.writeString(lastName)
        parcel.writeString(phone)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TutorPersonalInfo> {
        override fun createFromParcel(parcel: Parcel): TutorPersonalInfo {
            return TutorPersonalInfo(parcel)
        }

        override fun newArray(size: Int): Array<TutorPersonalInfo?> {
            return arrayOfNulls(size)
        }
    }
}

package com.autohub.skln.models.tutor

import android.os.Parcel
import android.os.Parcelable

data class TutorPersonalInfo(var accountPicture: String? = null,
                             var biodata: String? = null,
                             var email: String? = null,
                             var firstName: String? = null,
                             var gender: String? = null,
                             var lastName: String? = null,
                             var phone: String? = null
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

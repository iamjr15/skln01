package com.autohub.skln.models.tutor

import android.os.Parcel
import android.os.Parcelable

data class TutorOtpPassword(var otp: String? = null,
                            var password: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(otp)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TutorOtpPassword> {
        override fun createFromParcel(parcel: Parcel): TutorOtpPassword {
            return TutorOtpPassword(parcel)
        }

        override fun newArray(size: Int): Array<TutorOtpPassword?> {
            return arrayOfNulls(size)
        }
    }
}
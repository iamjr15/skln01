package com.autohub.skln.models.tutor

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

data class TutorPackageInfo(var frequency: String? = null,
                            var occurances: String? = null,
                            var price: String? = null,
                            var rateOption: String? = null,
                            var studentCapacity: String? = null,
                            var paymentOptions: ArrayList<String>? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            TODO("paymentOptions")) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(frequency)
        parcel.writeString(occurances)
        parcel.writeString(price)
        parcel.writeString(rateOption)
        parcel.writeString(studentCapacity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TutorPackageInfo> {
        override fun createFromParcel(parcel: Parcel): TutorPackageInfo {
            return TutorPackageInfo(parcel)
        }

        override fun newArray(size: Int): Array<TutorPackageInfo?> {
            return arrayOfNulls(size)
        }
    }
}
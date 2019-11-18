package com.autohub.skln.models.tutor

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class TutorPackageInfo(var frequency: String? = "",
                            var occurances: String? = "",
                            var price: String? = "",
                            var rateOption: String? = "",
                            var studentCapacity: String? = "",
                            var paymentOptions: ArrayList<String>? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readArrayList(null) as ArrayList<String>?)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(frequency)
        parcel.writeString(occurances)
        parcel.writeString(price)
        parcel.writeString(rateOption)
        parcel.writeString(studentCapacity)
        parcel.writeList(paymentOptions as List<*>?)
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
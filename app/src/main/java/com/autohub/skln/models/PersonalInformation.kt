package com.autohub.skln.models

import android.os.Parcel
import android.os.Parcelable

data class PersonalInformation(var city: String? = "",
                               var email: String? = "", var firstName: String? = "",
                               var lastName: String? = "", var gender: String? = "", var latitude: Double? = 0.0,
                               var longitude: Double? = 0.0, var password: String? = "", var phoneNumber: String? = "", var accountPicture: String? = "") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(city)
        parcel.writeString(email)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(gender)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        parcel.writeString(password)
        parcel.writeString(phoneNumber)
        parcel.writeString(accountPicture)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PersonalInformation> {
        override fun createFromParcel(parcel: Parcel): PersonalInformation {
            return PersonalInformation(parcel)
        }

        override fun newArray(size: Int): Array<PersonalInformation?> {
            return arrayOfNulls(size)
        }
    }
}

package com.krasovsky.dima.demoproject.base.dialog.alert.model

import android.os.Parcel
import android.os.Parcelable

class DialogData(val title: String, val message: String?, val btnOk: String, val btnCancel: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(message)
        parcel.writeString(btnOk)
        parcel.writeString(btnCancel)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DialogData> {
        override fun createFromParcel(parcel: Parcel): DialogData {
            return DialogData(parcel)
        }

        override fun newArray(size: Int): Array<DialogData?> {
            return arrayOfNulls(size)
        }
    }
}
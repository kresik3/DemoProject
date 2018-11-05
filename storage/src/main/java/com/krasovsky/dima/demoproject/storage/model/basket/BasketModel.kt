package com.krasovsky.dima.demoproject.storage.model.basket

import android.os.Parcel
import android.os.Parcelable
import kotlin.properties.Delegates

class BasketModel() : Parcelable {
    var id: String = ""
    var items: MutableList<BasketItemModel> = mutableListOf()
    var totalCount: Int = 0
    var totalPrice: Float = 0f

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        totalCount = parcel.readInt()
        totalPrice = parcel.readFloat()
        parcel.readTypedList(items, BasketItemModel.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(totalCount)
        parcel.writeFloat(totalPrice)
        parcel.writeTypedList(items)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BasketModel> {
        override fun createFromParcel(parcel: Parcel): BasketModel {
            return BasketModel(parcel)
        }

        override fun newArray(size: Int): Array<BasketModel?> {
            return arrayOfNulls(size)
        }
    }
}
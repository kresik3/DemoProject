package com.krasovsky.dima.demoproject.storage.model.basket

import android.os.Parcel
import android.os.Parcelable

open class BasketItemModel() : Parcelable {
    var shopItemDetailId: String = ""
    var count: Int = 0
    var price: Float = 0f
    var title: String = ""
    var kind: String = ""
    var imagePath: String = ""

    constructor(parcel: Parcel) : this() {
        shopItemDetailId = parcel.readString()
        count = parcel.readInt()
        price = parcel.readFloat()
        title = parcel.readString()
        kind = parcel.readString()
        imagePath = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(shopItemDetailId)
        parcel.writeInt(count)
        parcel.writeFloat(price)
        parcel.writeString(title)
        parcel.writeString(kind)
        parcel.writeString(imagePath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BasketItemModel> {
        override fun createFromParcel(parcel: Parcel): BasketItemModel {
            return BasketItemModel(parcel)
        }

        override fun newArray(size: Int): Array<BasketItemModel?> {
            return arrayOfNulls(size)
        }
    }
}
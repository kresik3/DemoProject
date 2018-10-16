package com.krasovsky.dima.demoproject.storage.model.dish

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class DetailModel() : RealmObject(), Parcelable {

    @PrimaryKey
    open var id: String = ""

    open var subOrder: Int = 0
    open var kind: String? = null
    open var quantity: String = ""
    open var price: Float = 0f
    open var shopItemId: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        subOrder = parcel.readInt()
        kind = parcel.readString()
        quantity = parcel.readString()
        price = parcel.readFloat()
        shopItemId = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(subOrder)
        parcel.writeString(kind)
        parcel.writeString(quantity)
        parcel.writeFloat(price)
        parcel.writeString(shopItemId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DetailModel> {
        override fun createFromParcel(parcel: Parcel): DetailModel {
            return DetailModel(parcel)
        }

        override fun newArray(size: Int): Array<DetailModel?> {
            return arrayOfNulls(size)
        }
    }

}
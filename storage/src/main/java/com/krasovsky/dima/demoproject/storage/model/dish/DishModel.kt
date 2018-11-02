package com.krasovsky.dima.demoproject.storage.model.dish

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class DishModel() : RealmObject(), Parcelable {

    @PrimaryKey
    open var id: String = ""

    open var title: String = ""
    open var description: String? = null
    open var imagePath: String = ""

    open var categoryId: String = ""

    open var details: RealmList<DetailModel> = RealmList()

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        title = parcel.readString()
        description = parcel.readString()
        imagePath = parcel.readString()
        categoryId = parcel.readString()
        parcel.readTypedList(details, DetailModel.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(imagePath)
        parcel.writeString(categoryId)
        parcel.writeTypedList(details)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DishModel> {
        override fun createFromParcel(parcel: Parcel): DishModel {
            return DishModel(parcel)
        }

        override fun newArray(size: Int): Array<DishModel?> {
            return arrayOfNulls(size)
        }
    }
}

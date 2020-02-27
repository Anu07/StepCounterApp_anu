package com.sd.src.stepcounterapp.model

import android.os.Parcel
import android.os.Parcelable


data class OptionsModel(
    var id: Int,
    var name: String,
    var isSelected: Boolean) : Parcelable {
    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString(),
        1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(name)
        writeInt((if (isSelected) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<OptionsModel> = object : Parcelable.Creator<OptionsModel> {
            override fun createFromParcel(source: Parcel): OptionsModel = OptionsModel(source)
            override fun newArray(size: Int): Array<OptionsModel?> = arrayOfNulls(size)
        }
    }
}
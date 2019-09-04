package com.codemobiles.mymobilephone.database

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.codemobiles.mobilephone.models.MobileBean
import com.codemobiles.mymobilephone.converter.Converters

@Entity(tableName = "mobilePhone")
data class MobileEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val mobileID:Int,
    val description:String,
    val thumbImageURL:String,
    val name:String,
    val price:Double,
    val brand:String,
    val rating:Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString().toString(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeInt(mobileID)
        parcel.writeString(description)
        parcel.writeString(thumbImageURL)
        parcel.writeString(name)
        parcel.writeDouble(price)
        parcel.writeString(brand)
        parcel.writeDouble(rating)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MobileEntity> {
        override fun createFromParcel(parcel: Parcel): MobileEntity {
            return MobileEntity(parcel)
        }

        override fun newArray(size: Int): Array<MobileEntity?> {
            return arrayOfNulls(size)
        }
    }
}
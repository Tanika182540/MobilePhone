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
@TypeConverters(Converters::class)
data class MobileEntity(
    @PrimaryKey(autoGenerate = true) var id:Int?,
    var phoneList: ArrayList<MobileBean>
)
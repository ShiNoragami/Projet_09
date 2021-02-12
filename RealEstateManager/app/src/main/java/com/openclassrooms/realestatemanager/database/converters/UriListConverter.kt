package com.openclassrooms.realestatemanager.database.converters

import androidx.room.TypeConverter
import com.openclassrooms.realestatemanager.models.UriList
import java.util.*

class UriListConverter {

    @TypeConverter
    fun toUriList(value: String?): UriList {
        if (value == null || value.isEmpty()) {
            return UriList()
        }

        val list: List<String> = value.split(",")
        val uriList = ArrayList<String>()
        for (item in list) {
            if (item.isNotEmpty()) {
                uriList.add(item.toString())
            }
        }
        return UriList(uriList)
    }

    @TypeConverter
    fun toString(photoList: UriList?): String {

        var string = ""

        if (photoList == null) {
            return string
        }

        photoList.photoList.forEach {
            string += "$it,"
        }
        return string
    }
}
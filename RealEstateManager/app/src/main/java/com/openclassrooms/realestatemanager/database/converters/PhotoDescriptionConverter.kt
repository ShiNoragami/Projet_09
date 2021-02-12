package com.openclassrooms.realestatemanager.database.converters

import androidx.room.TypeConverter

import com.openclassrooms.realestatemanager.models.PhotoDescription

class PhotoDescriptionConverter {

    @TypeConverter
    fun toPhotoDescription(value: String?): PhotoDescription {
        if (value == null || value.isEmpty()) {
            return PhotoDescription()
        }

        val list: List<String> = value.split(",")
        val mutableList = mutableListOf<String>()
        mutableList.addAll(list)
        mutableList.removeAt(list.size - 1)
        val photoDescription = ArrayList<String>()
        for (item in mutableList) {
            if (item.isNotEmpty()) {
                photoDescription.add(item.toString())
            } else {
                photoDescription.add("")
            }
        }
        return PhotoDescription(photoDescription)
    }

    @TypeConverter
    fun toString(photoDescription: PhotoDescription?): String {

        var string = ""

        if (photoDescription == null) {
            return string
        }

        photoDescription.photoDescription.forEach {
            string += "$it,"
        }
        return string
    }
}
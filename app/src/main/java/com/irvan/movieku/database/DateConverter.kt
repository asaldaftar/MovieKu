package com.irvan.movieku.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DateConverter {
    @TypeConverter
    fun fromString(value: String): Array<String> {
        val listType = object : TypeToken<Array<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: Array<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
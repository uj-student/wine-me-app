package com.student.wine_me_up.wine_repo

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class WineTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun regionToList(region: String?): List<String> {
        if (region == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<String?>?>() {}.type

        return gson.fromJson<List<String>>(region, listType)
    }

    @TypeConverter
    fun regionToString(region: List<String>): String {
        return gson.toJson(region)
    }
}
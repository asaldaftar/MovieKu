package com.irvan.movieku.helpers

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class FormatStringHelper {
    fun convertDateToIndo(dataDate: String?): String? {
        var date: String?
        try {
            val dateFormatServer = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateFormatServer.timeZone = TimeZone.getTimeZone("GMT") // IMP !!!
            val humanFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ID"))
            date = humanFormat.format(dateFormatServer.parse(dataDate!!)!!)
        } catch (e: ParseException) {
            e.printStackTrace()
            date = null
        }
        return date
    }
}
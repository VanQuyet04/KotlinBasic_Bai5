package com.example.kotlinbasic_bai5

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

data class Task(
    val id: Long,
    val name: String,
    val datetime: String
){
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm")
            .optionalStart()
            .appendPattern(":ss")
            .optionalStart()
            .appendFraction(ChronoField.MILLI_OF_SECOND, 0, 3, true)
            .optionalEnd()
            .optionalEnd()
            .toFormatter()
    }
}

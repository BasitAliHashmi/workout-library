package com.basit.workout_library.core.data.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@ProvidedTypeConverter
internal class AppDateTimeTypeConverter {
    private val dateTimePattern = "uuuu-MM-dd HH:mm:ss"

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let {
            LocalDateTime.parse(value, DateTimeFormatter.ofPattern(dateTimePattern))
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.format(DateTimeFormatter.ofPattern(dateTimePattern))
    }
}
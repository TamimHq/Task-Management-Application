package com.example.shared.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

fun toEpochMillis(year: Int, month: Int, day: Int): Long {
    val date = LocalDate(year, month, day)
    return date.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}

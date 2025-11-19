package com.example.shared.util

import kotlinx.datetime.LocalDate

fun isDateValid(selected: LocalDate, today: LocalDate): Boolean {
    return !selected.isBefore(today)
}

fun LocalDate.isBefore(other: LocalDate): Boolean {
    return this < other
}

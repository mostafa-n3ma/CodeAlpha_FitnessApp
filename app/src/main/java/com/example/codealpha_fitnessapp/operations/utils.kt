package com.example.codealpha_fitnessapp.operations

import java.util.Calendar
import java.util.Date

fun validateEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    return emailRegex.matches(email)
}



fun getCurrentDate(): Date {
    return Calendar.getInstance().time
}
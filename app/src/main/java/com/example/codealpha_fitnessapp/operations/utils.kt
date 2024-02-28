package com.example.codealpha_fitnessapp.operations


import com.example.codealpha_fitnessapp.operations.dataMangment.Workout
import com.google.gson.Gson
import java.util.Calendar
import java.util.Date
fun validateEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    return emailRegex.matches(email)
}



fun getCurrentDate(): Date {
    return Calendar.getInstance().time
}


fun getCurrentWeekDaysPairs(): List<Pair<String,String>> {
    val calendar = Calendar.getInstance()
    val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val weekDays = mutableListOf<Date>()

    // Calculate the offset to the start of the week (Saturday)
    val offset = (currentDayOfWeek - Calendar.SATURDAY + 7) % 7
    calendar.add(Calendar.DAY_OF_MONTH, -offset)

    // Add dates for Saturday to Friday
    repeat(7) {
        weekDays.add(calendar.time)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    val datePairsList = mutableListOf<Pair<String,String>>()
    weekDays.map { date->
        datePairsList.add(Pair(
            date.toString().subSequence(startIndex = 0, endIndex = 3).toString(),
            date.toString().subSequence(startIndex = 8, endIndex = 10).toString()
        ))
    }

    return datePairsList.toList()

}

fun getCurrentWeekDays(): List<Date> {
    val calendar = Calendar.getInstance()
    val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val weekDays = mutableListOf<Date>()

    // Calculate the offset to the start of the week (Saturday)
    val offset = (currentDayOfWeek - Calendar.SATURDAY + 7) % 7
    calendar.add(Calendar.DAY_OF_MONTH, -offset)

    // Add dates for Saturday to Friday
    repeat(7) {
        weekDays.add(calendar.time)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    return weekDays

}


fun isInCurrentMonth(date: Date): Boolean {
    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)

    calendar.time = date
    val givenMonth = calendar.get(Calendar.MONTH)
    val givenYear = calendar.get(Calendar.YEAR)

    return currentMonth == givenMonth && currentYear == givenYear
}


fun getWorkoutArg(workout: Workout):String{
    return Gson().toJson(workout)
}

fun Workout.toArg():String{
     return Gson().toJson(this)
}

fun String.fromArg():Workout{
    return Gson().fromJson(this,Workout::class.java)
}
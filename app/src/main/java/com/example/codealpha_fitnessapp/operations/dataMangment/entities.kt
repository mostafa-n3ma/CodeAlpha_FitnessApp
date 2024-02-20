package com.example.codealpha_fitnessapp.operations.dataMangment

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    var name:String,
    val email:String,
    var weight:List<WeightRecord>,
)
data class WeightRecord(val weight:Double,val lastUpdate:Date)
class WeightRecordConverter{
    @TypeConverter
    fun fromWeightRecordList(value:List<WeightRecord>?):String?{
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toWeightRecordList(value: String?):List<WeightRecord>?{
        val gson = Gson()
        val type = object :TypeToken<List<WeightRecord>>() {}.type
        return gson.fromJson<List<WeightRecord>>(value,type)
    }

}



class WeightListTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String): List<Int> {
        val listType = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Int>): String {
        return gson.toJson(list)
    }
}


@Entity(tableName = "Workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userID:Int,
    var title:String,
    var date: Date?,
    var muscleGroup: MuscleGroup?,
    var restDuration:Int,
    val exercises:List<Exercise>
)

data class Exercise(
    var name: String,
    var sets:Int,
    var reps:Int,
    val duration:Int
)

class ExerciseListConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromJson(value: String): List<Exercise> {
        val listType = object : TypeToken<List<Exercise>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(list: List<Exercise>): String {
        return gson.toJson(list)
    }
}

class DateConverter {

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}




@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userId:Int,
    var title:String,
    var goalTypes: GoalTypes,
    var targetValue: TargetValue,
    var period: Period,
    var hasPeriod:Boolean
)
package com.example.codealpha_fitnessapp.operations.dataMangment

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.util.Date
import com.google.gson.*
import java.lang.reflect.Type
import org.json.JSONObject


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



//class WeightListTypeConverter {
//
//    private val gson = Gson()
//
//    @TypeConverter
//    fun fromString(value: String): List<Int> {
//        val listType = object : TypeToken<List<Int>>() {}.type
//        return gson.fromJson(value, listType)
//    }
//
//    @TypeConverter
//    fun fromList(list: List<Int>): String {
//        return gson.toJson(list)
//    }
//}


@Entity(tableName = "Workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val userID:Int?,
    var title:String,
    var date: Date?,
    var muscleGroup: MuscleGroup?,
    var restDuration:Int,
    var exercises:List<Exercise>
)
{
    fun calculateDuration():Double{
        var duration = 0.0
        exercises.map {exercise->
            duration += exercise.sets * exercise.duration
        }
        return duration/60
    }
}

data class Exercise(
    var name: String,
    var sets:Int,
    var reps:Int,
    var duration:Int
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




//@Entity(tableName = "goals")
//data class Goal(
//    @PrimaryKey(autoGenerate = true)
//    val id: Int,
//    val userId:Int,
//    var title:String,
//    var goalTypes: GoalTypes,
//    var targetValue: TargetValue,
//    var period: Period,
//    var hasPeriod:Boolean
//)



@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val userId: Int,
    @ColumnInfo(name = "goal_data")
    val goalData: GoalData
)

sealed class GoalData {
    data class WeightLoss(val targetValue: Double, val period: Period) : GoalData()
    data class WorkoutCounts(val targetValue: Int, val period: Period) : GoalData()
    data class WorkoutsDuration(val targetValue: Double, val period: Period) : GoalData()
    data class MuscleCounts(val muscleGroup: MuscleGroup, val targetValue: Int, val period: Period) : GoalData()


    // Serialize GoalData to JSON
    fun toJson(): String {
        val typeValue = when (this) {
            is WeightLoss -> "WeightLoss"
            is WorkoutCounts -> "WorkoutCounts"
            is WorkoutsDuration -> "WorkoutsDuration"
            is MuscleCounts -> "MuscleCounts"
        }
        val gson = Gson()
        val json = gson.toJson(this)
        val jsonObject = JSONObject(json)
        jsonObject.put("type", typeValue)
        return jsonObject.toString()
    }

    // Deserialize JSON to GoalData
    companion object {
        fun fromJson(json: String): GoalData {
            val jsonObject = JSONObject(json)
            val type = jsonObject.getString("type")
            return when (type) {
                "WeightLoss" -> WeightLoss(jsonObject.getDouble("targetValue"), Period.valueOf(jsonObject.getString("period")))
                "WorkoutCounts" -> WorkoutCounts(jsonObject.getInt("targetValue"), Period.valueOf(jsonObject.getString("period")))
                "WorkoutsDuration" -> WorkoutsDuration(jsonObject.getDouble("targetValue"), Period.valueOf(jsonObject.getString("period")))
                "MuscleCounts" -> MuscleCounts(MuscleGroup.valueOf(jsonObject.getString("muscleGroup")), jsonObject.getInt("targetValue"), Period.valueOf(jsonObject.getString("period")))
                else -> throw IllegalArgumentException("Invalid goal type: $type")
            }
        }
    }
}


class GoalDataTypeConverter {
    @TypeConverter
    fun fromGoalData(goalData: GoalData): String {
        return goalData.toJson()
    }

    @TypeConverter
    fun toGoalData(json: String): GoalData {
        return GoalData.fromJson(json)
    }
}


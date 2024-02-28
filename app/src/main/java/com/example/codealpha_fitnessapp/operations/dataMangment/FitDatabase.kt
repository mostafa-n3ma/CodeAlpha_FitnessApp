package com.example.codealpha_fitnessapp.operations.dataMangment

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [User::class,Workout::class,Goal::class], version = 5, exportSchema = false)
@TypeConverters(
    ExerciseListConverter::class,
    DateConverter::class,
    WeightRecordConverter::class,
    GoalDataTypeConverter::class)
abstract class FitDatabase: RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun goalDao(): GoalDao
}
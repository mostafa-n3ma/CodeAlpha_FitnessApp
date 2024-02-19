package com.example.codealpha_fitnessapp.operations.dataMangment

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [User::class,Workout::class,Goal::class], version = 2, exportSchema = false)
@TypeConverters(
    ExerciseListConverter::class,
    WeightListTypeConverter::class,
    DateConverter::class,
    WeightRecordConverter::class)
abstract class FitDatabase: RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun goalDao(): GoalDao
}
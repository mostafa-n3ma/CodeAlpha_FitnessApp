package com.example.codealpha_fitnessapp.operations.dataMangment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao{
    @Query("SELECT * FROM users")
    suspend fun getAllUsers():List<User>

    @Insert
    suspend fun insertUser(user: User)
}

@Dao
interface WorkoutDao{
    @Query("SELECT * FROM workouts WHERE userID = :userID")
    suspend fun getUserWorkouts(userID:Int):List<Workout>

    @Insert
    suspend fun insertWorkout(workout: Workout)
}

@Dao
interface GoalDao{
    @Query("SELECT * FROM goals WHERE userId = :userID")
    suspend fun getUsersGoals(userID:Int):List<Goal>


    @Insert
    suspend fun insertGoal(goal: Goal)
}
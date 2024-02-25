package com.example.codealpha_fitnessapp.operations.dataMangment

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workouts WHERE userID = :userID")
    suspend fun getUserWorkouts(userID: Int): List<Workout>


    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    suspend fun getWorkoutById(workoutId: Int): Workout

    @Insert
    suspend fun insertWorkout(workout: Workout)

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkout(workout: Workout): Int

    @Query("DELETE FROM workouts WHERE userID = :userID")
    suspend fun deleteAllUserWorkouts(userID: Int)

}

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals WHERE userId = :userID")
    suspend fun getUsersGoals(userID: Int): List<Goal>

    @Insert
    suspend fun insertGoal(goal: Goal)

    @Update
    suspend fun updateGoal(goal: Goal)

    @Delete
    suspend fun deleteGoal(goal: Goal)

    @Query("DELETE FROM goals WHERE userId = :userID")
    suspend fun deleteAllUserGoals(userID: Int)
}

package com.example.codealpha_fitnessapp.operations.dataMangment

import javax.inject.Inject

class FitRepository
@Inject
constructor(
    private val userDao: UserDao,
    private val workoutDao: WorkoutDao,
    private val goalDao: GoalDao
){

    // UserDao functions
    suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    // WorkoutDao functions
    suspend fun getUserWorkouts(userID: Int): List<Workout> {
        return workoutDao.getUserWorkouts(userID)
    }

    suspend fun insertWorkout(workout: Workout) {
        workoutDao.insertWorkout(workout)
    }

    // GoalDao functions
    suspend fun getUsersGoals(userID: Int): List<Goal> {
        return goalDao.getUsersGoals(userID)
    }

    suspend fun insertGoal(goal: Goal) {
        goalDao.insertGoal(goal)
    }
}
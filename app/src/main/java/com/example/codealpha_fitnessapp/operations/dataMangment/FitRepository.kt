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

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    suspend fun deleteAllUsers() {
        userDao.deleteAllUsers()
    }

    // WorkoutDao functions
    suspend fun getUserWorkouts(userID: Int): List<Workout> {
        return workoutDao.getUserWorkouts(userID)
    }

    suspend fun insertWorkout(workout: Workout) {
        workoutDao.insertWorkout(workout)
    }

    suspend fun updateWorkout(workout: Workout) {
        workoutDao.updateWorkout(workout)
    }

    suspend fun deleteWorkout(workout: Workout) {
        workoutDao.deleteWorkout(workout)
    }

    suspend fun deleteAllUserWorkouts(userID: Int) {
        workoutDao.deleteAllUserWorkouts(userID)
    }

    // GoalDao functions
    suspend fun getUsersGoals(userID: Int): List<Goal> {
        return goalDao.getUsersGoals(userID)
    }

    suspend fun insertGoal(goal: Goal) {
        goalDao.insertGoal(goal)
    }

    suspend fun updateGoal(goal: Goal) {
        goalDao.updateGoal(goal)
    }

    suspend fun deleteGoal(goal: Goal) {
        goalDao.deleteGoal(goal)
    }

    suspend fun deleteAllUserGoals(userID: Int) {
        goalDao.deleteAllUserGoals(userID)
    }
}

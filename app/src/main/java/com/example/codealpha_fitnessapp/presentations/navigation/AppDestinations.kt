package com.example.codealpha_fitnessapp.presentations.navigation

enum class AppDestinations(val route:String) {
    WelcomeScreen("WELCOME_SCREEN"),
    LogInScreen("LOGIN"),
    SignUpScreen("SIGNUP"),
    DashboardScreen("DASHBOARD"),
    WorkoutsScreen("WORKOUTS"),
    WorkoutDetailsScreen("WORKOUT_DETAILS"),
    WorkoutPlayScreen("PLAY_WORKOUT"),
    GoalsScreen("GOALS")
}
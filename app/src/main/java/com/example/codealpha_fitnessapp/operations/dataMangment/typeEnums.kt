package com.example.codealpha_fitnessapp.operations.dataMangment

enum class Gender { Male, Female }
enum class MuscleGroup {
    Back,
    Chest,
    Biceps,
    Triceps,
    Legs,
    Shoulders,
    Abs
}

enum class GoalTypes {
    WEIGHT_LOSS,
    WORKOUTS_PER_WEEK,
    WORKOUT_DURATION_PER_WEEK,
    MUSCLE_GROUP_COUNTS
}

enum class TargetValue(val value: Int = 0) {
    TARGET_WEIGHT,
    TARGET_WORKOUTS_PER_WEEK,
    TARGET_WORKOUTS_DURATION,
    TARGET_MUSCLE_WORKOUT
}
enum class Period{WEEKLY, MONTHLY}
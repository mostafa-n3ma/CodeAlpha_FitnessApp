package com.example.codealpha_fitnessapp.presentations.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codealpha_fitnessapp.operations.dataMangment.Exercise
import com.example.codealpha_fitnessapp.operations.dataMangment.FitRepository
import com.example.codealpha_fitnessapp.operations.dataMangment.MuscleGroup
import com.example.codealpha_fitnessapp.operations.dataMangment.User
import com.example.codealpha_fitnessapp.operations.dataMangment.Workout
import com.example.codealpha_fitnessapp.operations.getCurrentWeekDays
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel
@Inject
constructor(private val repository: FitRepository) : ViewModel() {
    companion object {
        const val TAG = "DashboardViewModel"
    }

    private var firebaseAuth: FirebaseAuth

    private val _userInfo = MutableStateFlow<User?>(null)
    val userInfo: StateFlow<User?> = _userInfo


    private var _backWeeklyActivity = MutableStateFlow<String>("0")
    val backWeeklyActivity: StateFlow<String> = _backWeeklyActivity

    private var _chestWeeklyActivity = MutableStateFlow<String>("0")
    val chestWeeklyActivity: StateFlow<String> = _chestWeeklyActivity

    private var _bicepsWeeklyActivity = MutableStateFlow<String>("0")
    val bicepsWeeklyActivity: StateFlow<String> = _bicepsWeeklyActivity

    private var _tricepsWeeklyActivity = MutableStateFlow<String>("0")
    val tricepsWeeklyActivity: StateFlow<String> = _tricepsWeeklyActivity

    private var _legsWeeklyActivity = MutableStateFlow<String>("0")
    val legsWeeklyActivity: StateFlow<String> = _legsWeeklyActivity

    private var _shouldersWeeklyActivity = MutableStateFlow<String>("0")
    val shouldersWeeklyActivity: StateFlow<String> = _shouldersWeeklyActivity

    private var _absWeeklyActivity = MutableStateFlow<String>("0")
    val absWeeklyActivity: StateFlow<String> = _absWeeklyActivity




    fun loadWeeklyRecords(user: User) {
        val weekDays: List<Date> = getCurrentWeekDays()
        Log.d(TAG, "loadWeeklyRecords: weekdays:$weekDays")
        viewModelScope.launch {
            val allWorkouts = repository.getUserWorkouts(user.id)
            Log.d(TAG, "loadWeeklyRecords: allWorkouts:$allWorkouts")
            val weekdaysDateRange: List<Int> = getCurrentWeekDays().map {
                it.date
            }
            _backWeeklyActivity.update {
                collectMuscleWeeklyDuration(allWorkouts.filter {
                    it.muscleGroup == MuscleGroup.Back && it.date!!.date in weekdaysDateRange
                })
            }



            _chestWeeklyActivity.update {
                collectMuscleWeeklyDuration(allWorkouts.filter {
                    it.muscleGroup == MuscleGroup.Chest && it.date!!.date in weekdaysDateRange
                })
            }


            _bicepsWeeklyActivity.update {
                collectMuscleWeeklyDuration(allWorkouts.filter {
                    it.muscleGroup == MuscleGroup.Biceps && it.date!!.date in weekdaysDateRange
                })
            }


            _tricepsWeeklyActivity.update {
                collectMuscleWeeklyDuration(allWorkouts.filter {
                    it.muscleGroup == MuscleGroup.Triceps && it.date!!.date in weekdaysDateRange
                })
            }


            _legsWeeklyActivity.update {
                collectMuscleWeeklyDuration(allWorkouts.filter {
                    it.muscleGroup == MuscleGroup.Legs && it.date!!.date in weekdaysDateRange
                })
            }



            _shouldersWeeklyActivity.update {
                collectMuscleWeeklyDuration(allWorkouts.filter {
                    it.muscleGroup == MuscleGroup.Shoulders && it.date!!.date in weekdaysDateRange
                })
            }


            _absWeeklyActivity.update {
                collectMuscleWeeklyDuration(allWorkouts.filter {
                    it.muscleGroup == MuscleGroup.Abs && it.date!!.date in weekdaysDateRange
                })
            }


        }
    }

    private fun collectMuscleWeeklyDuration(filteredWorkouts: List<Workout>): String {
        Log.d(TAG, "collectMuscleWeeklyDuration: filteredlist:$filteredWorkouts")
        var duration = 0
        filteredWorkouts.map { workout ->
            workout.exercises.map { exercise ->
                duration += exercise.sets * exercise.duration
            }
        }
        return (duration.toDouble() / 60).toString()
    }

    private fun getUserInfo() {
        val email = firebaseAuth.currentUser!!.email
        viewModelScope.launch {
            repository.getAllUsers().map { user ->
                if (user.email == email) {
                    _userInfo.update { user }
                    loadWeeklyRecords(user)
//                    addDemoData(user.id)
                }
            }
        }

    }

    private fun addDemoData(id: Int) {
        viewModelScope.launch {
            demoWorkouts.map { workout ->
               demoWorkouts.map {
                   repository.insertWorkout(it)
               }
            }
        }
    }


    init {
        firebaseAuth = Firebase.auth
        getUserInfo()


    }


    val weekDays = getCurrentWeekDays()
    val demoWorkouts =
        listOf(
            Workout(
                userID = 1,
                title = "back",
                date = weekDays[0],
                muscleGroup = MuscleGroup.Back,
                restDuration = 30,
                exercises = listOf(
                    Exercise(
                        name = "ex1",
                        sets = 1,
                        reps = 8,
                        duration = 60
                    ))
            ),
            Workout(
                userID = 1,
                title = "Chest",
                date = weekDays[3],
                muscleGroup = MuscleGroup.Chest,
                restDuration = 30,
                exercises = listOf(
                    Exercise(
                        name = "ex1",
                        sets = 2,
                        reps = 8,
                        duration = 60
                    ),
                    Exercise(
                        name = "ex2",
                        sets = 3,
                        reps = 12,
                        duration = 30
                    ),
                    Exercise(
                        name = "ex3",
                        sets = 4,
                        reps = 3,
                        duration = 20
                    ),

                    )
            ),


        )


}
package com.example.codealpha_fitnessapp.presentations.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codealpha_fitnessapp.operations.dataMangment.FitRepository
import com.example.codealpha_fitnessapp.operations.dataMangment.MuscleGroup
import com.example.codealpha_fitnessapp.operations.dataMangment.User
import com.example.codealpha_fitnessapp.operations.dataMangment.Workout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutsViewModel
@Inject
constructor(private val repository: FitRepository) : ViewModel() {
    companion object{
        const val TAG = "WorkoutsViewModel"
    }

    private var firebaseAuth: FirebaseAuth

    private val _userInfo = MutableStateFlow<User?>(null)
    val userInfo: StateFlow<User?> = _userInfo



    private var _allWorkouts= MutableStateFlow<List<Workout>>(emptyList())
    val allWorkouts:StateFlow<List<Workout>> = _allWorkouts





init {
    firebaseAuth = Firebase.auth
    getUserInfo()

}


    private fun getUserInfo() {
        val email = firebaseAuth.currentUser!!.email
        viewModelScope.launch {
            repository.getAllUsers().map { user ->
                if (user.email == email) {
                    _userInfo.update { user }
                    getAllWorkouts(user.id)
                    Log.d(TAG, "getUserInfo: userID =  ${user.id}")
                }
            }
        }

    }

    private fun getAllWorkouts(id: Int) {
        viewModelScope.launch {
            _allWorkouts.update {
                repository.getUserWorkouts(id)
            }
            Log.d(TAG, "getAllWorkouts: ${_allWorkouts.value}")
        }
    }


    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            try {
                val deletionSuccessful = repository.deleteWorkout(workout)
                if (deletionSuccessful) {
                    // Deletion successful, update the list of all workouts
                    val updatedAllWorkouts: List<Workout> = _allWorkouts.value.filterNot { it == workout }
                    _allWorkouts.value = updatedAllWorkouts
                } else {
                    // Deletion failed
                    // Handle the failure, maybe log an error or notify the user

                }
            } catch (e: Exception) {
                // Exception occurred during deletion
                // Handle the exception, maybe log an error or notify the user
                Log.d(TAG, "deleteWorkout: error:${e.message}")
            }
        }
    }



}
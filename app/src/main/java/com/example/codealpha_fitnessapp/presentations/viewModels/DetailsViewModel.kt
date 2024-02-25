package com.example.codealpha_fitnessapp.presentations.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codealpha_fitnessapp.operations.dataMangment.Exercise
import com.example.codealpha_fitnessapp.operations.dataMangment.FitRepository
import com.example.codealpha_fitnessapp.operations.dataMangment.MuscleGroup
import com.example.codealpha_fitnessapp.operations.dataMangment.User
import com.example.codealpha_fitnessapp.operations.dataMangment.Workout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel
@Inject
constructor(private val repository: FitRepository) : ViewModel() {
    companion object {
        const val TAG = "DetailsViewModel"
    }


    private var firebaseAuth: FirebaseAuth = Firebase.auth

    private val currentUser = MutableStateFlow<User?>(null)

    private val newWorkoutObject: Workout = Workout(
        userID = null,
        title = "new workout",
        date = null,
        muscleGroup = null,
        restDuration = 30,
        exercises = emptyList()
    )

    private fun getUserInfo() {
        val email = firebaseAuth.currentUser!!.email
        viewModelScope.launch {
            repository.getAllUsers().map { user ->
                if (user.email == email) {
                    currentUser.update { user }
                    Log.d(TAG, "getUserInfo: currentUser = $currentUser")
                }
            }
        }
    }

    init {
        getUserInfo()
    }

    var workoutIsNewCreated = false

    fun initializeReceivedObject(wkId: Int) {
        if (wkId == 0) {
            receivedObject = newWorkoutObject
            _sessionObject.update { receivedObject.copy(exercises = receivedObject.exercises.map { it.copy() }) }
            receivedExercisesList = (_sessionObject.value?.exercises?.map { it.copy() }
                ?: emptyList<Exercise>()).toMutableList()
            _exercisesList.value = (_sessionObject.value?.exercises?.map { it.copy() }
                ?: emptyList<Exercise>()).toMutableList()
            workoutIsNewCreated = true

        } else {
            viewModelScope.launch {
                receivedObject = repository.getWorkoutById(wkId)
                _sessionObject.update { receivedObject.copy(exercises = receivedObject.exercises.map { it.copy() }) }
                receivedExercisesList = (_sessionObject.value?.exercises?.map { it.copy() }
                    ?: emptyList<Exercise>()).toMutableList()
                _exercisesList.value = (_sessionObject.value?.exercises?.map { it.copy() }
                    ?: emptyList<Exercise>()).toMutableList()
                updateMAinSaveBtnVisibility()
            }
        }
    }

    lateinit var receivedObject: Workout
    private val _sessionObject = MutableStateFlow<Workout?>(null)
    val sessionObject: StateFlow<Workout?> = _sessionObject

    fun updateWorkoutTitle(value: String) {
        val currentWorkout = _sessionObject.value
        val updatedWorkout = currentWorkout?.copy(title = value)
        _sessionObject.value = updatedWorkout
    }

    fun updateWorkoutMuscleGroup(value: MuscleGroup) {
        val currentWorkout = _sessionObject.value
        val updatedWorkout = currentWorkout?.copy(muscleGroup = value)
        _sessionObject.value = updatedWorkout
    }

    fun updateWorkoutResDuration(operationType: OperationType, editedProperty: EditedProperty) {
        val currentWorkout = _sessionObject.value
        if (editedProperty == EditedProperty.REST_DURATION_10) {
            val updatedWorkout = when (operationType) {
                OperationType.INCREASE -> {
                    currentWorkout?.copy(restDuration = currentWorkout.restDuration + 10)
                }

                OperationType.DECREASE -> {
                    if (currentWorkout?.restDuration!! > 10){
                        currentWorkout?.copy(restDuration = currentWorkout.restDuration - 10)
                    }else{
                        currentWorkout?.copy(restDuration = currentWorkout.restDuration)
                    }
                }
            }
            _sessionObject.value = updatedWorkout
        }
    }


    lateinit var receivedExercisesList: List<Exercise>
    private val _exercisesList = MutableStateFlow<MutableList<Exercise>>(mutableListOf())
    val exercisesList: StateFlow<List<Exercise>> = _exercisesList

    private val _mainSaveBtnVisible = MutableStateFlow<Boolean>(false)
    val mainSaveBtnVisible: StateFlow<Boolean> = _mainSaveBtnVisible

    private fun updateMAinSaveBtnVisibility() {
        _mainSaveBtnVisible.update {
            _sessionObject.value != receivedObject
                    ||
                    _exercisesList.value != receivedExercisesList
        }
        Log.d(TAG, "updateMAinSaveBtnVisibility :xcv99: value : ${_mainSaveBtnVisible.value}")
        Log.d(TAG, "updateMAinSaveBtnVisibility :xcv99: receivedObject : ${receivedObject}")

        Log.d(
            TAG,
            "updateMAinSaveBtnVisibility :xcv99: receivedExercisesList : ${receivedExercisesList}"
        )
        Log.d(TAG, "updateMAinSaveBtnVisibility :xcv99: _exercisesList : ${_exercisesList.value}")

        Log.d(TAG, "updateMAinSaveBtnVisibility :xcv99: sessionObject : ${_sessionObject.value}")
    }


    private val _newExercise =
        MutableStateFlow<Exercise>(Exercise(name = "", sets = 1, reps = 1, duration = 30))
    val newExercise: StateFlow<Exercise> = _newExercise

    fun emptyNewExerciseObject() {
        _newExercise.update { Exercise(name = "", sets = 1, reps = 1, duration = 30) }
    }


    fun updateExerciseName(value: String) {
        val currentExercise = _newExercise.value
        val updatedExercise = currentExercise.copy(name = value)
        _newExercise.value = updatedExercise
    }

    fun editExerciseObject(operationType: OperationType, editedProperty: EditedProperty) {
        val currentExercise = _newExercise.value
        val updatedExercise = when (editedProperty) {
            EditedProperty.SET_1 -> {
                when (operationType) {
                    OperationType.INCREASE -> currentExercise.copy(sets = currentExercise.sets + 1)
                    OperationType.DECREASE -> {
                        if (currentExercise.sets > 1) {
                            currentExercise.copy(sets = currentExercise.sets - 1)
                        } else {
                            currentExercise.copy(sets = currentExercise.sets)
                        }

                    }
                }
            }

            EditedProperty.REP_1 -> {
                when (operationType) {
                    OperationType.INCREASE -> currentExercise.copy(reps = currentExercise.reps + 1)
                    OperationType.DECREASE -> {
                        if (currentExercise.reps > 0) {
                            currentExercise.copy(reps = currentExercise.reps - 1)
                        } else {
                            currentExercise.copy(reps = currentExercise.reps)
                        }

                    }
                }
            }

            EditedProperty.DURATION_10 -> {
                when (operationType) {
                    OperationType.INCREASE -> currentExercise.copy(duration = currentExercise.duration + 10)
                    OperationType.DECREASE -> {
                        if (currentExercise.duration > 10) {
                            currentExercise.copy(duration = currentExercise.duration - 10)
                        } else {
                            currentExercise.copy(duration = currentExercise.duration)
                        }

                    }
                }
            }

            else -> {
                currentExercise.copy()
            }
        }
        _newExercise.value = updatedExercise
    }


    private val _isCardio = MutableStateFlow<Boolean>(false)
    val isCardio :StateFlow<Boolean> = _isCardio

    fun ConvertToCardio(value: Boolean){
        _isCardio.update { value }
    }
    fun saveNewExercise() {
        if (_isCardio.value){
            _newExercise.value.apply {
                sets = 1
                reps = 0
            }
        }
        Log.d(TAG, "saveNewExercise: exerciseList:${_exercisesList.value}")
        val updatedExerciseList: MutableList<Exercise> =
            _exercisesList.value.map { it.copy() }.toMutableList()
        Log.d(TAG, "saveNewExercise: updatedExercisesList:$updatedExerciseList")
        updatedExerciseList.add(_newExercise.value)
        Log.d(
            TAG,
            "saveNewExercise: updatedExercisesList:after adding new Exercise:$updatedExerciseList"
        )

        _exercisesList.value = updatedExerciseList
        Log.d(TAG, "saveNewExercise: exerciseList:after adding newExercise:${_exercisesList.value}")
        setEvent(DetailsEvent.CloseBottomSheet)
        emptyNewExerciseObject()
        updateMAinSaveBtnVisibility()
    }


    fun deleteExercise(ex: Exercise) {
        val updatedExerciseList: MutableList<Exercise> =
            _exercisesList.value.map { it.copy() }.toMutableList()
        updatedExerciseList.remove(ex)
        _exercisesList.value = updatedExerciseList
        updateMAinSaveBtnVisibility()
    }


    private val _bottomSheetStatus = MutableStateFlow<Boolean>(false)
    val bottomSheetStatus: StateFlow<Boolean> = _bottomSheetStatus


    private val _requestedBottomSheetLayout = MutableStateFlow<BsLayoutType>(BsLayoutType.EXERCISE)
    val requestBottomSheetLayout: StateFlow<BsLayoutType> = _requestedBottomSheetLayout


    fun setEvent(event: DetailsEvent) {
        when (event) {
            DetailsEvent.CloseBottomSheet -> {
                _bottomSheetStatus.update { false }
                emptyNewExerciseObject()
            }

            is DetailsEvent.OpenBottomSheetEvent -> {
                viewModelScope.launch {
                    _requestedBottomSheetLayout.update { event.layout }
                    Log.d(
                        TAG,
                        "bottomSheetTest : setEvent: requestedLayout= ${requestBottomSheetLayout.value}"
                    )
                    delay(500)
                    _bottomSheetStatus.update { true }
                    Log.d(
                        TAG,
                        "bottomSheetTest : setEvent: bottomSheetStatus : ${_bottomSheetStatus.value}"
                    )
                }
            }

            DetailsEvent.UpdateDatabaseObject -> {
                viewModelScope.launch {
                    val finalWorkoutObject: Workout? =
                        _sessionObject.value?.copy(exercises = _exercisesList.value.map { it.copy() })
                    if (workoutIsNewCreated) {
                        repository.insertWorkout(finalWorkoutObject?.copy(userID = currentUser.value?.id)!!)
                        setEvent(DetailsEvent.CloseBottomSheet)
                    } else {
                        //updating
                        repository.updateWorkout(finalWorkoutObject!!)
                        setEvent(DetailsEvent.CloseBottomSheet)
                    }

                }
            }
        }
    }


}





sealed class DetailsEvent() {
    data class OpenBottomSheetEvent(val layout: BsLayoutType) : DetailsEvent()
    data object CloseBottomSheet : DetailsEvent()


    data object UpdateDatabaseObject : DetailsEvent()
}
enum class OperationType {INCREASE, DECREASE }
enum class EditedProperty {SET_1, REP_1, DURATION_10, REST_DURATION_10 }
enum class BsLayoutType { WORKOUT, EXERCISE }


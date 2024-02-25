package com.example.codealpha_fitnessapp.presentations.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codealpha_fitnessapp.operations.dataMangment.FitRepository
import com.example.codealpha_fitnessapp.operations.dataMangment.Workout
import com.example.codealpha_fitnessapp.operations.getCurrentDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class TimerItem(
    val name: String = "",
    val setNumber: Int = 0,
    val reps: Int = 0,
    val duration: Int = 30,
    val timer: Int = 0,
    val isRestItem: Boolean = false,
    val index: Int
)

@HiltViewModel
class PlayWorkoutViewModel
@Inject
constructor(private val repository: FitRepository) : ViewModel() {
    companion object {
        const val TAG = "PlayVieWModel"
    }

    private lateinit var currentWorkout: Workout

    private val _btnStatus = MutableStateFlow<BtnStatus>(BtnStatus.OnPlay)
    val btnStatus: StateFlow<BtnStatus> = _btnStatus

    var timerIsOn = false
    fun toggleBtnStatus() {
        when (_btnStatus.value) {
            BtnStatus.OnPlay -> {
                _btnStatus.value = BtnStatus.OnPause
                when(timerIsOn){
                    true -> {

                    }
                    false -> {
                        startCountdown(_timerItemsList.value[_currentItemIndex.value].duration)
                    }
                }
                timerIsOn = true
            }

            BtnStatus.OnPause -> {
                _btnStatus.value = BtnStatus.OnPlay
            }
        }
    }


    fun passCurrentWorkout(wkId: Int) {
        Log.d(TAG, "timerTest99:passCurrentWorkout: wk:$wkId")
        viewModelScope.launch {
            currentWorkout = repository.getWorkoutById(wkId)
            Log.d(TAG, "passCurrentWorkout: timerTest99 : currentWorkout: $currentWorkout")
            prepareTimerItemList()
        }
    }


    private val _timerItemsList = MutableStateFlow<MutableList<TimerItem>>(mutableListOf())
    val timerItemsList: StateFlow<MutableList<TimerItem>> = _timerItemsList


    private fun prepareTimerItemList() {
        var index = 0
        Log.d(TAG, "prepareTimerItemList: timerTest99 currentWorkout: $currentWorkout")
        val tempList = mutableListOf<TimerItem>()
        currentWorkout.exercises.map { ex ->
            for (i in 1..ex.sets) {
                val addedEx = TimerItem(
                    name = ex.name,
                    setNumber = i,
                    reps = ex.reps,
                    duration = ex.duration,
                    isRestItem = false,
                    index = index
                )
                tempList.add(
                    addedEx
                )
                Log.d(
                    TAG,
                    "prepareTimerItemList: timerTest99 adding to the list item:$addedEx"
                )
                index += 1
                val addedRest = TimerItem(
                    name = "Rest",
                    duration = currentWorkout.restDuration,
                    isRestItem = true,
                    index = index
                )
                tempList.add(
                    addedRest
                )
                index += 1
                Log.d(
                    TAG,
                    "prepareTimerItemList: timerTest99 adding to the list item:$addedRest"
                )
            }

        }
        Log.d(TAG, "prepareTimerItemList:timerTest99// final itemsList : ${_timerItemsList.value}")
        _timerItemsList.value = tempList
    }

    private val _timerLiveValue = MutableStateFlow<String>("")
    val timerLiveValue: StateFlow<String> = _timerLiveValue


    private fun startCountdown(duration: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            countDown(duration)
        }
    }

    private suspend fun countDown(duration: Int) {
        var remainingTime = duration

        while (remainingTime >= 0) {
            if (_btnStatus.value == BtnStatus.OnPause) {
                _timerLiveValue.value = String.format("%02d", remainingTime)
                delay(1000) // Delay 1 second
                remainingTime -= 1
            }
        }
        if (_currentItemIndex.value != _timerItemsList.value.lastIndex){
            passToNextItemIndex()
        }else{
            saveWorkoutSession()
        }
    }

    private fun saveWorkoutSession() {
        viewModelScope.launch {
            Log.d(TAG, "itemTest99:saveWorkoutSession: save the fucking workout session and get out")
            val workoutSession = currentWorkout.copy(id = null, date = getCurrentDate(), exercises = currentWorkout.exercises.map { it.copy() })
            repository.insertWorkout(workoutSession)
            _onDone.value = true
        }
    }
    private val _onDone = MutableStateFlow<Boolean>(false)
    val onDone:StateFlow<Boolean> = _onDone


    private val _currentItemIndex = MutableStateFlow<Int>(0)
    val currentItemIndex: StateFlow<Int> = _currentItemIndex


    private fun passToNextItemIndex() {
        val updatedIndex = _currentItemIndex.value+1
        _currentItemIndex.value = updatedIndex
        Log.d(TAG, "itemTest99:passToNextItemIndex: current index is = ${_currentItemIndex.value}")
        startCountdown(_timerItemsList.value[_currentItemIndex.value].duration)
        val updatedList = _timerItemsList.value.map { it.copy() }
        _timerItemsList.value = updatedList.toMutableList()
    }

    fun getCountdownLiveData(duration: Int): LiveData<Int> {
        val countdownLiveData = MutableLiveData<Int>()

        CoroutineScope(Dispatchers.Default).launch {
            var remainingTime = duration

            while (remainingTime >= 0) {
                if (_btnStatus.value == BtnStatus.OnPlay) {
                    countdownLiveData.postValue(remainingTime)
                    delay(1000) // Delay 1 second
                    remainingTime -= 1
                }
            }
        }

        return countdownLiveData
    }


}


enum class BtnStatus { OnPlay, OnPause }



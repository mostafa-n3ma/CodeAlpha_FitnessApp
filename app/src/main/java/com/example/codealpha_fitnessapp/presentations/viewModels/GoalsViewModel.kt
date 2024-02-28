package com.example.codealpha_fitnessapp.presentations.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codealpha_fitnessapp.operations.dataMangment.FitRepository
import com.example.codealpha_fitnessapp.operations.dataMangment.Goal
import com.example.codealpha_fitnessapp.operations.dataMangment.GoalData
import com.example.codealpha_fitnessapp.operations.dataMangment.MuscleGroup
import com.example.codealpha_fitnessapp.operations.dataMangment.Period
import com.example.codealpha_fitnessapp.operations.dataMangment.User
import com.example.codealpha_fitnessapp.operations.dataMangment.WeightRecord
import com.example.codealpha_fitnessapp.operations.dataMangment.Workout
import com.example.codealpha_fitnessapp.operations.getCurrentDate
import com.example.codealpha_fitnessapp.operations.getCurrentWeekDays
import com.example.codealpha_fitnessapp.operations.isInCurrentMonth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel
@Inject
constructor(
    private var repository: FitRepository
) : ViewModel() {

    companion object {
        const val TAG = "GoalsViewModel"
    }

    private var firebaseAuth: FirebaseAuth = Firebase.auth
    private lateinit var currentUser: User

    private fun getUserInfo() {
        val email = firebaseAuth.currentUser!!.email
        viewModelScope.launch {
            repository.getAllUsers().map { user ->
                if (user.email == email) {
                    currentUser = user
//                    repository.deleteAllUserGoals(user.id)
                    Log.d(TAG, "getUserInfo: user:$user")
                    val goals: List<Goal> = repository.getUsersGoals(user.id)
                    val weightGoal: Goal? = goals.find { it.goalData is GoalData.WeightLoss }
                    Log.d(TAG, "getUserInfo: goals:$goals")
                    if (weightGoal != null) {
                        getWeightData()
                    }


                    val allWorkouts: List<Workout> = repository.getUserWorkouts(user.id)
//                    registerAllWorkouts(allWorkouts)
                    getWorkCountData(allWorkouts)
                    getWorkoutDurationData(allWorkouts)


                    getMuscleCountsData(allWorkouts,MuscleGroup.Back)
                    getMuscleCountsData(allWorkouts,MuscleGroup.Chest)
                    getMuscleCountsData(allWorkouts,MuscleGroup.Biceps)
                    getMuscleCountsData(allWorkouts,MuscleGroup.Triceps)
                    getMuscleCountsData(allWorkouts,MuscleGroup.Legs)
                    getMuscleCountsData(allWorkouts,MuscleGroup.Shoulders)
                    getMuscleCountsData(allWorkouts,MuscleGroup.Abs)
//                    repository.insertGoal(Goal(userId = user.id, goalData = GoalData.MuscleCounts(muscleGroup = MuscleGroup.Abs, targetValue = 10, period = Period.WEEKLY)))




                }
            }
        }
    }

    private fun registerAllWorkouts(allWorkouts: List<Workout>) {
//        val workoutSession = currentWorkout.copy(id = null, date = getCurrentDate(), exercises = currentWorkout.exercises.map { it.copy() })
//        repository.insertWorkout(workoutSession)

        val weekDays: List<Date> = getCurrentWeekDays()

        weekDays.map { date ->
            allWorkouts.map { workout: Workout ->
                if (workout.date == null) {
                    val newSession = workout.copy(
                        id = null,
                        date = date,
                        exercises = workout.exercises.map { it.copy() })
                    viewModelScope.launch {
                        repository.insertWorkout(newSession)
                    }
                }
            }
        }


    }

    init {
        getUserInfo()
    }


    //Weight Data
    data class WeightData(
        var initWeight: Pair<Double, String>,
        var currentWeight: Pair<Double, String>,
        var targetWeight: Double,
        var progressPercentage: Double,
        var weightDifferent: Double,
        var period: Period
    )

    private val _weightWidgetData = MutableStateFlow<WeightData?>(null)
    val weightWidgetData: StateFlow<WeightData?> = _weightWidgetData
    private fun getWeightData() {
        lateinit var initWeight: Pair<Double, String>
        lateinit var lastAddWeight: Pair<Double, String>
        var targetWeight = 0.0
        var lastAddedWeightValueAccordingToTheChosenPeriod: Double = 0.0
        var chosenPeriod = Period.WEEKLY

        viewModelScope.launch {
            initWeight =
                Pair(currentUser.weight[0].weight, formatDate(currentUser.weight[0].lastUpdate))
            lastAddWeight = Pair(
                currentUser.weight[currentUser.weight.lastIndex].weight,
                formatDate(currentUser.weight[currentUser.weight.lastIndex].lastUpdate)
            )
            Log.d(
                TAG,
                "getWeightData: initWeight:$initWeight // lastAddedWeight:$lastAddWeight from the user:$currentUser"
            )
            val allGoals: List<Goal> = repository.getUsersGoals(currentUser.id)
            Log.d(TAG, "getWeightData: allGoals:$allGoals")
            val weightGoal: Goal? = allGoals.find { it.goalData is GoalData.WeightLoss }

            if (weightGoal?.goalData is GoalData.WeightLoss) {
                targetWeight = weightGoal?.goalData.targetValue
                chosenPeriod = weightGoal?.goalData.period
                lastAddedWeightValueAccordingToTheChosenPeriod =
                    getLastAddedWeightIn(chosenPeriod, currentUser.weight)!!
                Log.d(
                    TAG,
                    "getWeightData: after adding goal>> initWeight=$initWeight, lastAddWeight=$lastAddWeight, targetWeight=${targetWeight}"
                )
            }

            val percentage =
                calculatePercentage(initWeight.first, lastAddWeight.first, targetWeight)

            val weightDifferent: Double =
                lastAddedWeightValueAccordingToTheChosenPeriod - initWeight.first
            _weightWidgetData.update {
                WeightData(
                    initWeight = initWeight,
                    currentWeight = lastAddWeight,
                    targetWeight = targetWeight,
                    progressPercentage = percentage,
                    weightDifferent = weightDifferent,
                    period = chosenPeriod
                )
            }
        }
    }

    private fun getLastAddedWeightIn(period: Period, weightList: List<WeightRecord>): Double? {
        val currentDate = getCurrentDate()
        val calendar = Calendar.getInstance()
        calendar.time = currentDate

        when (period) {
            Period.WEEKLY -> calendar.add(Calendar.DAY_OF_YEAR, -7)
            Period.MONTHLY -> calendar.add(Calendar.MONTH, -1)
        }

        val startDate = calendar.time

        val filteredRecords =
            weightList.filter { it.lastUpdate >= startDate && it.lastUpdate <= currentDate }

        return filteredRecords.lastOrNull()?.weight
    }

    fun updateWeightGoal(targetValue: Double, currentWeightValue: Double, period: Period) {
        viewModelScope.launch {
            val goals: List<Goal> = repository.getUsersGoals(currentUser.id)
            val weightGoal: Goal? = goals.find { it.goalData is GoalData.WeightLoss }
            when (weightGoal) {
                null -> {
                    repository.insertGoal(
                        Goal(
                            userId = currentUser.id,
                            goalData = GoalData.WeightLoss(
                                targetValue = targetValue,
                                period = period
                            )
                        )
                    )
                    val weightList = currentUser.weight.toMutableList()
                    weightList.add(WeightRecord(currentWeightValue, getCurrentDate()))
                    val updatedUser = currentUser.copy(weight = weightList)
                    repository.updateUser(updatedUser)
                    currentUser = updatedUser
                    getWeightData()
                }

                else -> {
                    var updatedGoalData: Goal
                    if (weightGoal.goalData is GoalData.WeightLoss) {
                        updatedGoalData = weightGoal.copy(
                            goalData = GoalData.WeightLoss(
                                targetValue = targetValue,
                                period = period
                            )
                        )
                        repository.updateGoal(updatedGoalData)
                    }
                    val weightList = currentUser.weight.toMutableList()
                    weightList.add(WeightRecord(currentWeightValue, getCurrentDate()))
                    val updatedUser = currentUser.copy(weight = weightList)
                    repository.updateUser(updatedUser)
                    currentUser = updatedUser
                    getWeightData()

                }
            }
        }
    }


    //workoutCount Data
    data class WorkoutCountData(
        var targetCount: Int,
        var totalCount: Int,
        var period: Period,
        var daysCountPairs: List<Pair<String, Int>>
    )

    private val _workCountsData = MutableStateFlow<WorkoutCountData?>(null)
    val workCountData: StateFlow<WorkoutCountData?> = _workCountsData
    private fun getWorkCountData(allWorkouts: List<Workout>) {
        var targetCount: Int = 0
        var totalWeekCount: Int = 0
        var totalMonthCount: Int = 0
        var period: Period = Period.WEEKLY
        val weekDays: List<Date> = getCurrentWeekDays()
        Log.d(
            TAG, "getWorkCountData: allWorkouts:${allWorkouts.size},${allWorkouts.size}" +
                    "    weekDays:$weekDays"
        )
        val daysCountPairs = mutableListOf<Pair<String, Int>>()
        weekDays.map { date ->
            var wkCount = 0
            allWorkouts.map { workout ->
                if (workout.date != null) {
                    totalWeekCount += 1
                    if (workout.date!!.date == date.date) {
                        wkCount += 1
                    }
                }
            }
            daysCountPairs.add(Pair(formatDate(date), wkCount))
        }

        allWorkouts.map { workout ->
            if (workout.date != null) {
                if (isInCurrentMonth(workout.date!!)) {
                    totalMonthCount += 1
                }
            }
        }
        Log.d(TAG, "getWorkCountData: totalMonthCount:$totalMonthCount")
        Log.d(TAG, "getWorkCountData: daysCountPairs:$daysCountPairs")

        viewModelScope.launch {
            val allGoals: List<Goal> = repository.getUsersGoals(currentUser.id)
            val workoutCountGoal = allGoals.find { it.goalData is GoalData.WorkoutCounts }
            if (workoutCountGoal?.goalData is GoalData.WorkoutCounts) {
                targetCount = workoutCountGoal?.goalData.targetValue
                period = workoutCountGoal?.goalData.period
                val workoutCountData = WorkoutCountData(
                    targetCount = targetCount,
                    totalCount = when (period) {
                        Period.WEEKLY -> {
                            totalWeekCount / 7
                        }

                        Period.MONTHLY -> totalMonthCount
                    },
                    period = period,
                    daysCountPairs = daysCountPairs
                )
                _workCountsData.update {
                    workoutCountData
                }
                Log.d(TAG, "getWorkCountData: workoutCountData:$workoutCountData")

            }


        }


    }

    fun updateWorkoutCountGoal(targetValue: Int, period: Period) {
        viewModelScope.launch {
            val allGoals: List<Goal> = repository.getUsersGoals(currentUser.id)
            val workoutCountGoal: Goal? = allGoals.find { it.goalData is GoalData.WorkoutCounts }
            when (workoutCountGoal) {
                null -> {
                    repository.insertGoal(
                        Goal(
                            userId = currentUser.id,
                            goalData = GoalData.WorkoutCounts(
                                targetValue = targetValue,
                                period = period
                            )
                        )
                    )
                    getWorkCountData(repository.getUserWorkouts(currentUser.id))
                }

                else -> {
                    val updatedWorkoutCountGoal: Goal
                    if (workoutCountGoal.goalData is GoalData.WorkoutCounts) {
                        updatedWorkoutCountGoal = workoutCountGoal.copy(
                            goalData = GoalData.WorkoutCounts(
                                targetValue = targetValue,
                                period = period
                            )
                        )
                        repository.updateGoal(updatedWorkoutCountGoal)
                    }

                    getWorkCountData(repository.getUserWorkouts(currentUser.id))
                }
            }


        }
    }


    data class WorkoutDurationData(
        var targetDuration: Double,
        var totalDuration: Double,
        var period: Period,
        var daysDurationsPairs: List<Pair<String, Int>>
    )

    private val _workoutDurationData = MutableStateFlow<WorkoutDurationData?>(null)
    val workoutDurationData: StateFlow<WorkoutDurationData?> = _workoutDurationData
    private fun getWorkoutDurationData(allWorkouts: List<Workout>) {
        var targetDuration: Double = 0.0
        var totalWeekDuration: Double = 0.0
        var totalMonthDuration: Double = 0.0
        var period: Period
        val weekDays: List<Date> = getCurrentWeekDays()
        val dysDurationPairs = mutableListOf<Pair<String, Int>>()
        weekDays.map { date ->
            var dallyDuration = 0.0
            allWorkouts.map { workout ->
                if (workout.date != null) {
                    if (workout.date?.date == date.date) {
                        dallyDuration += workout.calculateDuration()
                    }
                }
            }
            dysDurationPairs.add(Pair(formatDate(date), dallyDuration.toInt()))
            totalWeekDuration += dallyDuration
        }

        allWorkouts.map { workout: Workout ->
            if (workout.date != null) {
                if (isInCurrentMonth(workout.date!!)) {
                    totalMonthDuration += workout.calculateDuration()
                }
            }
        }

        viewModelScope.launch {
            val allGoals: List<Goal> = repository.getUsersGoals(currentUser.id)
            val workoutDurationGoal = allGoals.find { it.goalData is GoalData.WorkoutsDuration }
            if (workoutDurationGoal?.goalData is GoalData.WorkoutsDuration) {
                targetDuration = workoutDurationGoal?.goalData.targetValue
                period = workoutDurationGoal?.goalData.period
                val workoutDurationData = WorkoutDurationData(
                    targetDuration = targetDuration,
                    period = period,
                    daysDurationsPairs = dysDurationPairs,
                    totalDuration = when (period) {
                        Period.WEEKLY -> totalWeekDuration
                        Period.MONTHLY -> totalMonthDuration
                    }
                )
                _workoutDurationData.update {
                    workoutDurationData
                }
                Log.d(
                    TAG,
                    "getWorkoutDurationData: workotDurationData:${_workoutDurationData.value}"
                )
                Log.d(TAG, "getWorkoutDurationData: totalMonthDuration:$totalMonthDuration")

            }
        }


    }

    fun updateWorkoutDurationGoal(targetValue: Double, period: Period) {
        viewModelScope.launch {
            val allGoals: List<Goal> = repository.getUsersGoals(currentUser.id)
            val workoutDurationGoal = allGoals.find { it.goalData is GoalData.WorkoutsDuration }
            when (workoutDurationGoal) {
                null -> {
                    repository.insertGoal(
                        Goal(
                            userId = currentUser.id,
                            goalData = GoalData.WorkoutsDuration(
                                targetValue = targetValue, period = period
                            )
                        )
                    )
                    getWorkoutDurationData(repository.getUserWorkouts(currentUser.id))
                }

                else -> {
                    val updatedWorkoutDurationGoal: Goal
                    if (workoutDurationGoal.goalData is GoalData.WorkoutsDuration) {
                        updatedWorkoutDurationGoal = workoutDurationGoal.copy(
                            goalData = GoalData.WorkoutsDuration(
                                targetValue = targetValue, period = period
                            )
                        )
                        repository.updateGoal(updatedWorkoutDurationGoal)
                    }

                    getWorkoutDurationData(repository.getUserWorkouts(currentUser.id))
                }
            }
        }
    }


    data class MuscleCountData(
        var muscleGroup: MuscleGroup,
        var targetValue: Int,
        var period: Period,
        var totalCounts: Int,
        var totalDuration:Double,
        var progress: Int
    )

    private val _backMuscleCountData = MutableStateFlow<MuscleCountData?>(null)
    val backMuscleCountData: StateFlow<MuscleCountData?> = _backMuscleCountData

    private val _chestMuscleCountData = MutableStateFlow<MuscleCountData?>(null)
    val chestMuscleCountData: StateFlow<MuscleCountData?> = _chestMuscleCountData

    private val _bicepsMuscleCountData = MutableStateFlow<MuscleCountData?>(null)
    val bicepsMuscleCountData: StateFlow<MuscleCountData?> = _bicepsMuscleCountData

    private val _tricepsMuscleCountData = MutableStateFlow<MuscleCountData?>(null)
    val tricepsMuscleCountData: StateFlow<MuscleCountData?> = _tricepsMuscleCountData

    private val _legsMuscleCountData = MutableStateFlow<MuscleCountData?>(null)
    val legsMuscleCountData: StateFlow<MuscleCountData?> = _legsMuscleCountData

    private val _shouldersMuscleCountData = MutableStateFlow<MuscleCountData?>(null)
    val shouldersMuscleCountData: StateFlow<MuscleCountData?> = _shouldersMuscleCountData

    private val _absMuscleCountData = MutableStateFlow<MuscleCountData?>(null)
    val absMuscleCountData: StateFlow<MuscleCountData?> = _absMuscleCountData
    private fun getMuscleCountsData(allWorkouts: List<Workout>, targetMuscle: MuscleGroup) {
        var period: Period
        val weekDays: List<Date> = getCurrentWeekDays()
        var muscleTargetValue = 0
        var muscleWeekTotalCount = 0
        var muscleMonthTotalCount = 0
        var muscleWeekTotalDuration = 0.0
        var muscleMonthTotalDuration = 0.0
        weekDays.map { date ->
            val targetMuscleWorkoutsOnDate: List<Workout> =
                allWorkouts.filter {
                    it.muscleGroup == targetMuscle
                            &&
                            it.date != null
                            &&
                            it.date?.date == date.date
                }
            targetMuscleWorkoutsOnDate.map {muscleWeekWK->
                muscleWeekTotalCount+=1
                muscleWeekTotalDuration += muscleWeekWK.calculateDuration()
            }
        }

        val targetMuscleWorkouts: List<Workout> = allWorkouts.filter { it.muscleGroup == targetMuscle && it.date!=null}
        targetMuscleWorkouts.map {muscleMonthWK->
            if (isInCurrentMonth(muscleMonthWK.date!!)){
                muscleMonthTotalCount+=1
                muscleMonthTotalDuration+=muscleMonthWK.calculateDuration()
            }

        }
        viewModelScope.launch {
            val allGoals: List<Goal> = repository.getUsersGoals(currentUser.id)
            val muscleCountGoal = allGoals.find { it.goalData is GoalData.MuscleCounts && it.goalData.muscleGroup == targetMuscle}
            if (muscleCountGoal?.goalData is GoalData.MuscleCounts){
                muscleTargetValue = muscleCountGoal?.goalData.targetValue
                period = muscleCountGoal?.goalData.period
                val muscleCountData =MuscleCountData(
                    muscleGroup = targetMuscle,
                    targetValue = muscleTargetValue,
                    period = period,
                    totalCounts = when(period){
                        Period.WEEKLY -> muscleWeekTotalCount
                        Period.MONTHLY -> muscleMonthTotalCount
                    },
                    progress = when(period){
                        Period.WEEKLY -> muscleWeekTotalCount*10
                        Period.MONTHLY -> muscleMonthTotalCount*10
                    },
                    totalDuration = when(period){
                        Period.WEEKLY -> muscleWeekTotalDuration
                        Period.MONTHLY -> muscleMonthTotalDuration
                    }
                )
                Log.d(TAG, "getMuscleCountsData: muscleCountData:$muscleCountData")

                when(targetMuscle){
                    MuscleGroup.Back -> {_backMuscleCountData.update { muscleCountData }}
                    MuscleGroup.Chest -> {_chestMuscleCountData.update { muscleCountData }}
                    MuscleGroup.Biceps -> {_bicepsMuscleCountData.update { muscleCountData }}
                    MuscleGroup.Triceps -> {_tricepsMuscleCountData.update { muscleCountData }}
                    MuscleGroup.Legs -> {_legsMuscleCountData.update { muscleCountData }}
                    MuscleGroup.Shoulders -> {_shouldersMuscleCountData.update { muscleCountData }}
                    MuscleGroup.Abs -> {_absMuscleCountData.update { muscleCountData }}
                }
            }











        }







    }

    fun updateMuscleCountGoal(targetMuscle: MuscleGroup,targetValue: Int,period: Period){
        viewModelScope.launch {
            val allGoals: List<Goal> = repository.getUsersGoals(currentUser.id)
            val muscleCountGoal = allGoals.find { it.goalData is GoalData.MuscleCounts && it.goalData.muscleGroup == targetMuscle}
                when(muscleCountGoal){
                    null->{
                        repository.insertGoal(
                            Goal(
                                userId = currentUser.id,
                                goalData = GoalData.MuscleCounts(
                                    muscleGroup = targetMuscle,
                                    targetValue = targetValue,
                                    period = period
                                )
                            )
                        )
                        getMuscleCountsData(repository.getUserWorkouts(currentUser.id),targetMuscle = targetMuscle)
                    }
                    else->{
                        val updatedMuscleCountsGoal:Goal
                        if (muscleCountGoal?.goalData is GoalData.MuscleCounts){
                            updatedMuscleCountsGoal = muscleCountGoal.copy(
                                goalData = GoalData.MuscleCounts(
                                    muscleGroup = targetMuscle,
                                    targetValue = targetValue,
                                    period = period
                                )
                            )
                            repository.updateGoal(updatedMuscleCountsGoal)
                        }
                        getMuscleCountsData(repository.getUserWorkouts(currentUser.id),targetMuscle = targetMuscle)
                    }
                }



        }
    }

    private fun calculatePercentage(
        initialValue: Double,
        currentValue: Double,
        targetValue: Double
    ): Double {
        val percentage = ((initialValue - currentValue) / (initialValue - targetValue)) * 100
        return percentage.coerceIn(0.0, 100.0) // Ensure the percentage is between 0 and 100
    }

    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("MMM, d", Locale.getDefault())
        return dateFormat.format(date)
    }


}
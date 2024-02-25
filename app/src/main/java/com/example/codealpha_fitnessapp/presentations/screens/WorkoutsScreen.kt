package com.example.codealpha_fitnessapp.presentations.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.codealpha_fitnessapp.operations.dataMangment.Workout
import com.example.codealpha_fitnessapp.presentations.CostumeTopBarComposable
import com.example.codealpha_fitnessapp.presentations.WorkoutItemCard
import com.example.codealpha_fitnessapp.presentations.navigation.AppDestinations
import com.example.codealpha_fitnessapp.presentations.viewModels.WorkoutsViewModel
import com.example.codealpha_fitnessapp.ui.theme.backgroundColor

@Composable
fun WorkoutsScreen(navController: NavHostController) {
    val workoutsViewModel: WorkoutsViewModel = hiltViewModel()
    val workouts: List<Workout> by workoutsViewModel.allWorkouts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        CostumeTopBarComposable(
            title = "Workouts",
            onBackClicked = { },
            havBackBtn = false,
            haveAddBtn = true,
            onAddClicked = {
                navController.navigate("${AppDestinations.WorkoutDetailsScreen.rout}/${0}")
            })








        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 90.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(workouts) { workoutItem ->
                if (workoutItem.date == null) {
                    WorkoutItemCard(title = workoutItem.title,
                        muscleGroup = workoutItem.muscleGroup!!,
                        duration = workoutItem.calculateDuration(),
                        onStartClick = {
                                       navController.navigate("${AppDestinations.WorkoutPlayScreen.rout}/${workoutItem.id}")
                        },
                        onDeleteClicked = {
                                workoutsViewModel.deleteWorkout(workoutItem)
                        },
                        onEditClicked = {
                            navController.navigate("${AppDestinations.WorkoutDetailsScreen.rout}/${workoutItem.id}")
                        })
                }

            }

        }

    }
}


@Preview
@Composable
fun WorkoutPreview() {
    WorkoutsScreen(rememberNavController())
}
package com.example.codealpha_fitnessapp.presentations.screens.goalScreen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.codealpha_fitnessapp.operations.dataMangment.MuscleGroup
import com.example.codealpha_fitnessapp.presentations.viewModels.GoalsViewModel
import com.example.codealpha_fitnessapp.ui.theme.backgroundColor


@Preview
@Composable
fun PreviewGoals() {
    GoalsScreen()
}




@Composable
fun GoalsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val goalsViewModel:GoalsViewModel = hiltViewModel()
        LazyColumn(
            modifier = Modifier.padding(bottom = 90.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                WeightGoalWidget(goalsViewModel)
            }

            item {
                WorkoutCountWidget(goalsViewModel)
            }
            item {
                WorkoutDurationGoalWidget(goalsViewModel)
            }

            item {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ){
                    MuscleCountGoalWidget(goalsViewModel = goalsViewModel, forMuscle = MuscleGroup.Back)
                    MuscleCountGoalWidget(
                        forMuscle = MuscleGroup.Chest,
                        goalsViewModel = goalsViewModel
                    )
                }
            }
            item {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ){
                    MuscleCountGoalWidget(
                        forMuscle = MuscleGroup.Biceps,
                        goalsViewModel = goalsViewModel
                    )
                    MuscleCountGoalWidget(
                        forMuscle = MuscleGroup.Triceps,
                        goalsViewModel = goalsViewModel
                    )
                }
            }
            item {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ){
                    MuscleCountGoalWidget(
                        forMuscle = MuscleGroup.Legs,
                        goalsViewModel = goalsViewModel
                    )
                    MuscleCountGoalWidget(
                        forMuscle = MuscleGroup.Shoulders,
                        goalsViewModel = goalsViewModel
                    )
                }
            }
            item {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ){
                    MuscleCountGoalWidget(
                        forMuscle = MuscleGroup.Abs,
                        goalsViewModel = goalsViewModel
                    )
                }
            }

        }


    }


}


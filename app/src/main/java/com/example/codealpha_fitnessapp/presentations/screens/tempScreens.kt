package com.example.codealpha_fitnessapp.presentations.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.codealpha_fitnessapp.presentations.navigation.AppDestinations

@Composable
fun DashboardScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "DashBoard")
    }
}


@Composable
fun LogInScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "LogInScreen")
    }
}

@Composable
fun SignUpScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "SignUpScreen")
    }
}
@Composable
fun WorkoutsScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "WorkoutsScreen")
        Button(onClick = { navController.navigate(AppDestinations.WorkoutDetailsScreen.rout) }) {
            Text(text = "WorkoutDetailsScreen")
        }
    }
}
@Composable
fun WorkoutDetailsScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "WorkoutDetailsScreen")
    }
}
@Composable
fun GoalSScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "GoalSScreen")
    }
}
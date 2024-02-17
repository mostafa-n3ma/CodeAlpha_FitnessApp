package com.example.codealpha_fitnessapp.presentations.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.codealpha_fitnessapp.R
import com.example.codealpha_fitnessapp.presentations.screens.DashboardScreen
import com.example.codealpha_fitnessapp.presentations.screens.GoalSScreen
import com.example.codealpha_fitnessapp.presentations.screens.LogInScreen
import com.example.codealpha_fitnessapp.presentations.screens.SignUpScreen
import com.example.codealpha_fitnessapp.presentations.screens.WorkoutDetailsScreen
import com.example.codealpha_fitnessapp.presentations.screens.WorkoutsScreen

@Composable
fun NavigationBarComposable() {


    val navController = rememberNavController()
    val items = listOf(
        BottomNavigationItem(
            title = "Goals",
            selectedIcon = painterResource(id = R.drawable.goals_filled),
            unSelectedIcon = painterResource(id = R.drawable.goals_outlined)
        ),
        BottomNavigationItem(
            title = "Dashboard",
            selectedIcon = painterResource(id = R.drawable.dashboard_filled),
            unSelectedIcon = painterResource(id = R.drawable.dashboard_outlined)
        ),
        BottomNavigationItem(
            title = "Workouts",
            selectedIcon = painterResource(id = R.drawable.workout_filled),
            unSelectedIcon = painterResource(id = R.drawable.workout_outlined)
        )
    )

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(1)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->

                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {

                        },
                        alwaysShowLabel = true,
                        label = {
                            Text(
                                text = item.title,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        },
                        icon = {
                            Icon(
                                painter = if (selectedItemIndex == index) {
                                    item.selectedIcon
                                }else{
                                     item.unSelectedIcon
                                }
                                , contentDescription = item.title
                            )
                        }
                    )

                }
            }
        }
    )
    {
        ScreenNavHost(navController)
    }


}

@Composable
fun ScreenNavHost(navController: NavHostController) {

    NavHost(navController = navController, startDestination = AppDestinations.DashboardScreen.rout){
       composable(AppDestinations.DashboardScreen.rout) {
           DashboardScreen()
       }

        composable(AppDestinations.LogInScreen.rout) {
            LogInScreen()
        }
        composable(AppDestinations.SignUpScreen.rout) {
            SignUpScreen()
        }
        composable(AppDestinations.WorkoutsScreen.rout) {
            WorkoutsScreen()
        }
        composable(AppDestinations.WorkoutDetailsScreen.rout) {
            WorkoutDetailsScreen()
        }
        composable(AppDestinations.GoalSScreen.rout) {
            GoalSScreen()
        }

    }
}

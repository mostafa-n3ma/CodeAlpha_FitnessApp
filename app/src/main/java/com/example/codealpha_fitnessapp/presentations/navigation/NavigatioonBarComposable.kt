package com.example.codealpha_fitnessapp.presentations.navigation

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.codealpha_fitnessapp.R
import com.example.codealpha_fitnessapp.presentations.screens.DashboardScreen
import com.example.codealpha_fitnessapp.presentations.screens.GoalSScreen
import com.example.codealpha_fitnessapp.presentations.screens.LogInScreen
import com.example.codealpha_fitnessapp.presentations.screens.SignUpScreen
import com.example.codealpha_fitnessapp.presentations.screens.WelcomeScreen
import com.example.codealpha_fitnessapp.presentations.screens.WorkoutDetailsScreen
import com.example.codealpha_fitnessapp.presentations.screens.WorkoutsScreen
import com.example.codealpha_fitnessapp.presentations.viewModels.AuthViewModel

@Preview
@Composable
fun myPreview() {
//    NavigationBarComposable(authViewModel)
}


@Composable
fun NavigationBarComposable(authViewModel: AuthViewModel) {
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


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(1)
    }

    Scaffold(
        bottomBar = {
            if(currentDestination?.route !in listOf(
                AppDestinations.WorkoutDetailsScreen.rout,
                AppDestinations.WelcomeScreen.rout,
                AppDestinations.LogInScreen.rout,
                AppDestinations.SignUpScreen.rout
            )){
                NavigationBar {
                    items.forEachIndexed { index, item ->

                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                                when(item.title){
                                    "Goals"->{navController.navigate(AppDestinations.GoalSScreen.rout)}
                                    "Dashboard"->{navController.navigate(AppDestinations.DashboardScreen.rout)}
                                    "Workouts"->{navController.navigate(AppDestinations.WorkoutsScreen.rout)}
                                }
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
                                    },
                                    contentDescription = item.title,
                                    tint = Color.Blue
                                )
                            }
                        )

                    }
                }
            }

        }
    )
    {
        ScreensNavHost(navController,authViewModel)
    }


}

@Composable
fun ScreensNavHost(navController: NavHostController, authViewModel: AuthViewModel) {
    NavHost(navController = navController, startDestination = AppDestinations.WelcomeScreen.rout){

        composable(AppDestinations.WelcomeScreen.rout){
            WelcomeScreen(navController = navController, authViewModel = authViewModel)
        }


       composable(AppDestinations.DashboardScreen.rout) {
           DashboardScreen()
       }

        composable(AppDestinations.LogInScreen.rout) {
            LogInScreen(navController, authViewModel)
        }
        composable(AppDestinations.SignUpScreen.rout) {
            SignUpScreen(navController,authViewModel)
        }
        composable(AppDestinations.WorkoutsScreen.rout) {
            WorkoutsScreen(navController)
        }
        composable(AppDestinations.WorkoutDetailsScreen.rout) {
            WorkoutDetailsScreen()
        }
        composable(AppDestinations.GoalSScreen.rout) {
            GoalSScreen()
        }

    }
}

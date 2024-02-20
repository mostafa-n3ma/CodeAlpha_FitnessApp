package com.example.codealpha_fitnessapp.presentations.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.codealpha_fitnessapp.R
import com.example.codealpha_fitnessapp.operations.dataMangment.User
import com.example.codealpha_fitnessapp.operations.getCurrentDate
import com.example.codealpha_fitnessapp.operations.getCurrentWeekDaysPairs
import com.example.codealpha_fitnessapp.presentations.Head2Text
import com.example.codealpha_fitnessapp.presentations.viewModels.AuthViewModel
import com.example.codealpha_fitnessapp.presentations.viewModels.DashboardViewModel
import com.example.codealpha_fitnessapp.ui.theme.backgroundColor
import com.example.codealpha_fitnessapp.ui.theme.cardsColor
import com.example.codealpha_fitnessapp.ui.theme.lightTextColor
import com.example.codealpha_fitnessapp.ui.theme.primaryColor
import com.example.codealpha_fitnessapp.ui.theme.textColor

const val DashBoardScreenTAG = "DashboardScreen"
@Composable
fun DashboardScreen(navController: NavHostController, authViewModel: AuthViewModel? = null) {
    val dashboardViewModel:DashboardViewModel = hiltViewModel()
    val userInfo: State<User?> = dashboardViewModel.userInfo.collectAsState()
    if (userInfo.value !=null){
        Log.d(DashBoardScreenTAG, "DashboardScreen: $userInfo")
    }
    val backWeeklyActivity: String by dashboardViewModel.backWeeklyActivity.collectAsState()
    val chestWeeklyActivity: String by dashboardViewModel.chestWeeklyActivity.collectAsState()
    val bicepsWeeklyActivity: String by dashboardViewModel.bicepsWeeklyActivity.collectAsState()
    val tricepsWeeklyActivity: String by dashboardViewModel.tricepsWeeklyActivity.collectAsState()
    val legsWeeklyActivity: String by dashboardViewModel.legsWeeklyActivity.collectAsState()
    val shouldersWeeklyActivity: String by dashboardViewModel.shouldersWeeklyActivity.collectAsState()
    val absWeeklyActivity: String by dashboardViewModel.absWeeklyActivity.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 16.dp)
    ) {
        UserWidget(name = userInfo.value?.name?:"", onUserClick = {})
        Spacer(modifier = Modifier.height(16.dp))
        WeekDaysWidget()
        Head2Text(txt = stringResource(R.string.this_week))
        ActivitiesWidgets(backWeeklyActivity,chestWeeklyActivity,bicepsWeeklyActivity, tricepsWeeklyActivity,legsWeeklyActivity ,shouldersWeeklyActivity , absWeeklyActivity)
        Head2Text(txt = stringResource(R.string.goals_progress))

    }
}

@Composable
fun ActivitiesWidgets(
    backWeeklyActivity: String,
    chestWeeklyActivity: String,
    bicepsWeeklyActivity: String,
    tricepsWeeklyActivity: String,
    legsWeeklyActivity: String,
    shouldersWeeklyActivity: String,
    absWeeklyActivity: String
) {
    LazyRow {
        item {
            MuscleActivityCard(icon = painterResource(id = R.drawable.cardio), activityName = "Cardio", total_duration = "30 Min")
        }
        item {
            MuscleActivityCard(icon = painterResource(id = R.drawable.back_muscle), activityName = "Back", total_duration = "$backWeeklyActivity Min")
        }
        item {
            MuscleActivityCard(icon = painterResource(id = R.drawable.chest_muscle), activityName = "Chest", total_duration = "$chestWeeklyActivity Min")
        }
        item {
            MuscleActivityCard(icon = painterResource(id = R.drawable.biceps_muscle), activityName = "Biceps", total_duration = "$bicepsWeeklyActivity Min")
        }
        item {
            MuscleActivityCard(icon = painterResource(id = R.drawable.triceps_muscle), activityName = "Triceps", total_duration = "$tricepsWeeklyActivity Min")
        }
        item {
            MuscleActivityCard(icon = painterResource(id = R.drawable.legs_muscle), activityName = "Legs", total_duration = "$legsWeeklyActivity Min")
        }
        item {
            MuscleActivityCard(icon = painterResource(id = R.drawable.sholders_muscle), activityName = "Shoulders", total_duration = "$shouldersWeeklyActivity Min")
        }
        item {
            MuscleActivityCard(icon = painterResource(id = R.drawable.abs_muscle), activityName = "Abs", total_duration = "$absWeeklyActivity Min")
        }

    }

}

@Composable
fun MuscleActivityCard(icon:Painter, activityName:String, total_duration:String) {
    Card(
        modifier = Modifier.padding(end = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardsColor
        ),
        shape = RoundedCornerShape(10)
    ) {
        Row (

        ){
            Icon(
                painter = icon,
                contentDescription = "",
                Modifier.size(64.dp),
                tint = Color.Black
            )
            Column(
                Modifier.padding(8.dp),

                ) {
                Text(
                    text = activityName,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        color = textColor
                    )
                )


                Text(
                    text = "${total_duration.take(4)} Min",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_light)),
                        color = textColor,
                        textAlign = TextAlign.Center,
                    ),

                )
            }

        }
    }
}


@Composable
fun WeekDaysWidget() {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(getCurrentWeekDaysPairs()) { datePair ->
            val isToday: Boolean = datePair.second == getCurrentDate().date.toString()
            Card(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(10),
                colors = CardDefaults.cardColors(
                    containerColor = if (isToday) {
                        primaryColor
                    } else {
                        cardsColor
                    }
                )
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = datePair.second,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_semi_bold)),
                        textAlign = TextAlign.Center,
                        color = if (isToday) {
                            backgroundColor
                        } else {
                            textColor
                        }
                    )
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = datePair.first,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        textAlign = TextAlign.Center,
                        color = if (isToday) {
                            textColor
                        } else {
                            lightTextColor
                        }
                    )
                )

            }
        }
    }


}

@Composable
fun UserWidget(name: String, onUserClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(top = 30.dp)
            .clickable {
                onUserClick()
            },
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.big_user),
            contentDescription = "",
            Modifier.size(64.dp)
        )
        Column {
            Text(
                text = "Hello",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    fontSize = 20.sp
                )
            )
            Text(
                text = name,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontSize = 24.sp
                )
            )
        }
    }
}


@Preview
@Composable
fun DashboardPreview() {
    DashboardScreen(navController = rememberNavController())
}
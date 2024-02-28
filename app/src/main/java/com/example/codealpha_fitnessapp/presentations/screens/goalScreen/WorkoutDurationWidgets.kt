package com.example.codealpha_fitnessapp.presentations.screens.goalScreen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codealpha_fitnessapp.R
import com.example.codealpha_fitnessapp.operations.dataMangment.Goal
import com.example.codealpha_fitnessapp.operations.dataMangment.GoalData
import com.example.codealpha_fitnessapp.operations.dataMangment.Period
import com.example.codealpha_fitnessapp.operations.getCurrentWeekDaysPairs
import com.example.codealpha_fitnessapp.presentations.viewModels.GoalsViewModel
import com.example.codealpha_fitnessapp.ui.theme.backgroundColor
import com.example.codealpha_fitnessapp.ui.theme.cardsColor
import com.example.codealpha_fitnessapp.ui.theme.lightTextColor
import com.example.codealpha_fitnessapp.ui.theme.primaryColor
import com.example.codealpha_fitnessapp.ui.theme.textColor


val workoutsDuration = Goal(
    userId = 1,
    goalData = GoalData.WorkoutsDuration(targetValue = 60.0, period = Period.WEEKLY)
)


//@Preview
//@Composable
//fun widgetPreview_WorkoutDuration() {
//    WorkoutDurationGoalWidget(workoutsDuration.goalData)
//}


@Composable
fun WorkoutDurationGoalWidget(goalsViewModel: GoalsViewModel) {
    val workoutDurationData = goalsViewModel.workoutDurationData.collectAsState()

    Log.d("duration test99", "WorkoutDurationGoalWidget: workoutDurationData:${workoutDurationData.value}")
    var switchToDataForm by remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(
            visible = !switchToDataForm,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(500)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(100)
            )
        ) {
            when (workoutDurationData.value) {
                null -> {
                    WorkoutDurationGoalCard_Null(onClickHer = { switchToDataForm = true })
                }

                else -> {
                    WorkoutDurationGoalCard(data = workoutDurationData ,onClickHer = { switchToDataForm = true })

                }
            }
        }

        AnimatedVisibility(
            visible = switchToDataForm,
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(500)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(100)
            )
        ) {
            WorkoutDurationGetDataLayout(data = workoutDurationData,goalsViewModel= goalsViewModel,  onDoneClicked = { switchToDataForm = false })
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDurationGetDataLayout(
    onDoneClicked: () -> Unit,
    data: State<GoalsViewModel.WorkoutDurationData?>,
    goalsViewModel: GoalsViewModel
) {
    var targetValue  by remember {
        mutableStateOf(data.value?.targetDuration?.toString()?:"0.0")
    }

    var period by remember {
        mutableStateOf(data.value?.period?.name?:"")
    }

    var updatedPeriod by remember{
        mutableStateOf(data.value?.period?:Period.WEEKLY)
    }




    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(10),
        backgroundColor = cardsColor,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            OutlinedTextField(
                value = targetValue,
                onValueChange = {
                        targetValue = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "target workouts duration") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = primaryColor,
                    focusedLabelColor = primaryColor,
                    cursorColor = primaryColor
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )


            var dropDown by remember {
                mutableStateOf(false)
            }


            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                OutlinedTextField(
                    value = period,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = "Period")
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        cursorColor = primaryColor
                    ),
                    keyboardOptions = KeyboardOptions.Default,
                    trailingIcon = {
                        IconButton(onClick = { dropDown = true }) {
                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
                        }
                    }
                )

                DropdownMenu(
                    expanded = dropDown,
                    onDismissRequest = { dropDown = false }) {
                    Period.values().forEach { periodItem ->
                        DropdownMenuItem(
                            modifier = Modifier
                                .width(100.dp)
                                .height(20.dp),
                            onClick = {
                                period = periodItem.name
                                updatedPeriod = periodItem
                                dropDown = false
                            }) {
                            Text(text = periodItem.name)
                        }
                    }

                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    onDoneClicked()
                    if(targetValue!="0.0"){
                        goalsViewModel.updateWorkoutDurationGoal(targetValue.toDouble(),updatedPeriod)
                    }
                },
                shape = RoundedCornerShape(10),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor
                )
            ) {
                Text(
                    text = "Done",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_semi_bold)),
                        color = backgroundColor
                    )
                )
            }


        }


    }
}

@Composable
private fun WorkoutDurationGoalCard(
    onClickHer: () -> Unit,
    data: State<GoalsViewModel.WorkoutDurationData?>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp),
        shape = RoundedCornerShape(10),
        backgroundColor = cardsColor,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Workout Duration Goal",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            color = textColor
                        )
                    )

                    val targetTxt = (data.value?.targetDuration.toString()).take(5)
                    val periodTxt = when(data.value?.period!!){
                        Period.WEEKLY -> "week"
                        Period.MONTHLY -> "month"
                    }
                    Text(
                        text = "Target : $targetTxt min/$periodTxt",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                            color = textColor
                        )
                    )

                    val totalTxt = data.value?.totalDuration.toString().take(5)

                    Text(
                        text = "you finished $totalTxt min\n" +
                                "this $periodTxt",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                            color = textColor
                        )
                    )


                }









                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(top = 45.dp, end = 4.dp)
                ) {

                    val weekDays: List<Pair<String, String>> = getCurrentWeekDaysPairs()
                    val sat = 70
                    val san = 60
                    val mon = 50
                    val tue = 44
                    val wed = 25
                    val the = 55
                    val fri = 70

                    val pairs = data.value?.daysDurationsPairs!!

                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        CostumeLinearProgressBar(
                            progress = pairs[0].second*2,
                            width = 125.dp,
                            height = 9.dp,
                            color = primaryColor,
                            backgroundColor = lightTextColor,
                            strokeCap = StrokeCap.Round
                        )

                        Text(
                            text = pairs[0].first,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = textColor
                            )
                        )

                    }


                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        CostumeLinearProgressBar(
                            progress = pairs[1].second*2,
                            width = 125.dp,
                            height = 9.dp,
                            color = primaryColor,
                            backgroundColor = lightTextColor,
                            strokeCap = StrokeCap.Round
                        )
                        Text(
                            text = pairs[1].first,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = lightTextColor
                            )
                        )

                    }


                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        CostumeLinearProgressBar(
                            progress = pairs[2].second*2,
                            width = 125.dp,
                            height = 9.dp,
                            color = primaryColor,
                            backgroundColor = lightTextColor,
                            strokeCap = StrokeCap.Round
                        )
                        Text(
                            text = pairs[2].first,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = lightTextColor
                            )
                        )

                    }


                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        CostumeLinearProgressBar(
                            progress = pairs[3].second*2,
                            width = 125.dp,
                            height = 9.dp,
                            color = primaryColor,
                            backgroundColor = lightTextColor,
                            strokeCap = StrokeCap.Round
                        )
                        Text(
                            text = pairs[3].first,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = lightTextColor
                            )
                        )

                    }


                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        CostumeLinearProgressBar(
                            progress = pairs[4].second*2,
                            width = 125.dp,
                            height = 9.dp,
                            color = primaryColor,
                            backgroundColor = lightTextColor,
                            strokeCap = StrokeCap.Round
                        )
                        Text(
                            text = pairs[4].first,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = lightTextColor
                            )
                        )

                    }


                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        CostumeLinearProgressBar(
                            progress = pairs[5].second*2,
                            width = 125.dp,
                            height = 9.dp,
                            color = primaryColor,
                            backgroundColor = lightTextColor,
                            strokeCap = StrokeCap.Round
                        )
                        Text(
                            text = pairs[5].first,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = lightTextColor
                            )
                        )
                    }
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        CostumeLinearProgressBar(
                            progress = pairs[6].second*2,
                            width = 125.dp,
                            height = 9.dp,
                            color = primaryColor,
                            backgroundColor = lightTextColor,
                            strokeCap = StrokeCap.Round
                        )
                        Text(
                            text = pairs[6].first,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = lightTextColor
                            )
                        )
                    }




                }


            }



            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clickable { onClickHer() },
                text = "<<<< Click to update Wight Goal >>>>",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = lightTextColor,
                    textAlign = TextAlign.Center
                )
            )
        }


    }
}




@Composable
private fun WorkoutDurationGoalCard_Null(onClickHer: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(10),
        backgroundColor = cardsColor,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Workout Duration Goal",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            color = textColor
                        )
                    )

                    Text(
                        text = "Not set Yet!",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                            color = textColor
                        )
                    )

                }
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clickable { onClickHer() },
                text = "<<<< Click to Set Workout Counts Goal >>>>",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = lightTextColor,
                    textAlign = TextAlign.Center
                )
            )
        }


    }
}
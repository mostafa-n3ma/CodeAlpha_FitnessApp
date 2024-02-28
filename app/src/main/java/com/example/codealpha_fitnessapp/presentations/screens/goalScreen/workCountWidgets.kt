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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codealpha_fitnessapp.R
import com.example.codealpha_fitnessapp.operations.dataMangment.Goal
import com.example.codealpha_fitnessapp.operations.dataMangment.GoalData
import com.example.codealpha_fitnessapp.operations.dataMangment.Period
import com.example.codealpha_fitnessapp.presentations.viewModels.GoalsViewModel
import com.example.codealpha_fitnessapp.ui.theme.backgroundColor
import com.example.codealpha_fitnessapp.ui.theme.cardsColor
import com.example.codealpha_fitnessapp.ui.theme.lightTextColor
import com.example.codealpha_fitnessapp.ui.theme.primaryColor
import com.example.codealpha_fitnessapp.ui.theme.textColor

val workoutsCount = Goal(
    userId = 1,
    goalData = GoalData.WorkoutCounts(targetValue = 15, period = Period.WEEKLY)
)

@Preview
@Composable
fun widgetPreview_WorkoutCounts() {
//    WorkoutCountWidget(goalsViewModel)
}


@Composable
fun WorkoutCountWidget(goalsViewModel: GoalsViewModel) {

    val workoutCountsData = goalsViewModel.workCountData.collectAsState()
    Log.d("workoutCount test 99 ", "workoutCountsData: $workoutCountsData ")


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
            when (workoutCountsData.value) {


                null -> {
                    WorkoutCountGoalCard_Null(onClickHer = { switchToDataForm = true })
                }

                else -> {
                    WorkoutCountGoalCard(
                        data = workoutCountsData,
                        onClickHer = { switchToDataForm = true })

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
            WorkoutCountGetDataLayout(
                data = workoutCountsData,
                goalsViewModel = goalsViewModel,
                onDoneClicked = { switchToDataForm = false }
            )
        }
    }

}

//@Preview
//@Composable
//fun WKCPreview() {
//    WorkoutCountGoalCard(onClickHer = {})
//}

@Composable
fun WorkoutCountGoalCard(onClickHer: () -> Unit, data: State<GoalsViewModel.WorkoutCountData?>) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Workout Count Goal",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            color = textColor
                        )
                    )
                    val periodTxt = when(data.value!!.period){
                        Period.WEEKLY -> {
                            "week"
                        }
                        Period.MONTHLY -> {
                            "month"
                        }
                    }

                    Text(
                        text = "Target : ${data.value!!.targetCount}/$periodTxt",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                            color = textColor
                        )
                    )



                    Text(
                        text = "you finished ${data.value!!.totalCount} workouts\n" +
                                "this $periodTxt",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                            color = textColor
                        )
                    )

                    Box(modifier = Modifier.fillMaxWidth()) {
                        val pairsList: List<Pair<String, Int>> = data.value!!.daysCountPairs
                        val sat = pairsList[0].second*4
                        val san =  pairsList[1].second*4
                        val mon =  pairsList[2].second*4
                        val tue =  pairsList[3].second*4
                        val wed =  pairsList[4].second*4
                        val the =  pairsList[5].second*4
                        val fri =  pairsList[6].second*4
                        CostumeCircularProgressBar(
                            modifier = Modifier.align(Alignment.Center),
                            progress = fri,
                            haveMedalTxt = false,
                            medalTxt = "",
                            size = 300.dp,
                            color = primaryColor,
                            strokeWidth = 15.dp,
                            strokeCap = StrokeCap.Round,
                        )

                        CostumeCircularProgressBar(
                            modifier = Modifier.align(Alignment.Center),
                            progress = the,
                            haveMedalTxt = false,
                            medalTxt = "",
                            size = 265.dp,
                            color = lightTextColor,
                            strokeWidth = 15.dp,
                            strokeCap = StrokeCap.Round,
                        )
                        CostumeCircularProgressBar(
                            modifier = Modifier.align(Alignment.Center),
                            progress = wed,
                            haveMedalTxt = false,
                            medalTxt = "",
                            size = 230.dp,
                            color = primaryColor,
                            strokeWidth = 15.dp,
                            strokeCap = StrokeCap.Round,
                        )
                        CostumeCircularProgressBar(
                            modifier = Modifier.align(Alignment.Center),
                            progress = tue,
                            haveMedalTxt = false,
                            medalTxt = "",
                            size = 195.dp,
                            color = lightTextColor,
                            strokeWidth = 15.dp,
                            strokeCap = StrokeCap.Round,
                        )
                        CostumeCircularProgressBar(
                            modifier = Modifier.align(Alignment.Center),
                            progress = mon,
                            haveMedalTxt = false,
                            medalTxt = "",
                            size = 160.dp,
                            color = primaryColor,
                            strokeWidth = 15.dp,
                            strokeCap = StrokeCap.Round,
                        )

                        CostumeCircularProgressBar(
                            modifier = Modifier.align(Alignment.Center),
                            progress = san,
                            haveMedalTxt = false,
                            medalTxt = "",
                            size = 125.dp,
                            color = lightTextColor,
                            strokeWidth = 15.dp,
                            strokeCap = StrokeCap.Round,
                        )
                        CostumeCircularProgressBar(
                            modifier = Modifier.align(Alignment.Center),
                            progress = sat,
                            haveMedalTxt = false,
                            medalTxt = "",
                            size = 90.dp,
                            color = primaryColor,
                            strokeWidth = 15.dp,
                            strokeCap = StrokeCap.Round,
                        )


                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 40.dp)
                        ) {

                            Text(
                                text = "${data.value!!.daysCountPairs[0].first}  ${data.value!!.daysCountPairs[0].second} workouts",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    color = textColor
                                )
                            )
                            Text(
                                text = "${data.value!!.daysCountPairs[1].first}  ${data.value!!.daysCountPairs[1].second} workouts",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    color = lightTextColor
                                )
                            )
                            Text(
                                text = "${data.value!!.daysCountPairs[2].first}  ${data.value!!.daysCountPairs[2].second} workouts",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    color = lightTextColor
                                )
                            )
                            Text(
                                text = "${data.value!!.daysCountPairs[3].first}  ${data.value!!.daysCountPairs[3].second} workouts",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    color = lightTextColor
                                )
                            )
                            Text(
                                text = "${data.value!!.daysCountPairs[4].first}  ${data.value!!.daysCountPairs[4].second} workouts",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    color = lightTextColor
                                )
                            )
                            Text(
                                text = "${data.value!!.daysCountPairs[5].first}  ${data.value!!.daysCountPairs[5].second} workouts",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    color = lightTextColor
                                )
                            )
                            Text(
                                text = "${data.value!!.daysCountPairs[6].first}  ${data.value!!.daysCountPairs[6].second} workouts",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    color = lightTextColor
                                )
                            )


                        }


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




//@Preview
//@Composable
//fun WorkoutCountDataLayoutPreview() {
//    WorkoutCountGetDataLayout({
//
//    }, workoutCountsData, goalsViewModel)
//}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutCountGetDataLayout(
    onDoneClicked: () -> Unit,
    data: State<GoalsViewModel.WorkoutCountData?>,
    goalsViewModel: GoalsViewModel
) {

    var targetValue  by remember {
        mutableStateOf(data.value?.targetCount?.toString()?:"0")
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
                label = { Text(text = "target workouts count") },
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
                    onValueChange = {

                    },
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
                    if(targetValue !="0"){
                        goalsViewModel.updateWorkoutCountGoal(targetValue.toInt(),updatedPeriod)
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
fun WorkoutCountGoalCard_Null(onClickHer: () -> Unit) {
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
                        text = "Workout Count Goal",
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


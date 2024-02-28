package com.example.codealpha_fitnessapp.presentations.screens.goalScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
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
import com.example.codealpha_fitnessapp.operations.dataMangment.MuscleGroup
import com.example.codealpha_fitnessapp.operations.dataMangment.Period
import com.example.codealpha_fitnessapp.presentations.viewModels.GoalsViewModel
import com.example.codealpha_fitnessapp.ui.theme.backgroundColor
import com.example.codealpha_fitnessapp.ui.theme.cardsColor
import com.example.codealpha_fitnessapp.ui.theme.lightTextColor
import com.example.codealpha_fitnessapp.ui.theme.primaryColor
import com.example.codealpha_fitnessapp.ui.theme.textColor

val muscleCounts_Back = Goal(
    userId = 1,
    goalData = GoalData.MuscleCounts(
        muscleGroup = MuscleGroup.Back,
        targetValue = 5,
        period = Period.WEEKLY
    )
)
val muscleCounts_Chest = Goal(
    userId = 1,
    goalData = GoalData.MuscleCounts(
        muscleGroup = MuscleGroup.Chest,
        targetValue = 5,
        period = Period.WEEKLY
    )
)
val muscleCounts_Biceps = Goal(
    userId = 1,
    goalData = GoalData.MuscleCounts(
        muscleGroup = MuscleGroup.Biceps,
        targetValue = 5,
        period = Period.WEEKLY
    )
)
val muscleCounts_Triceps= Goal(
    userId = 1,
    goalData = GoalData.MuscleCounts(
        muscleGroup = MuscleGroup.Triceps,
        targetValue = 5,
        period = Period.WEEKLY
    )
)
val muscleCounts_Legs = Goal(
    userId = 1,
    goalData = GoalData.MuscleCounts(
        muscleGroup = MuscleGroup.Legs,
        targetValue = 5,
        period = Period.WEEKLY
    )
)
val muscleCounts_Shoulders = Goal(
    userId = 1,
    goalData = GoalData.MuscleCounts(
        muscleGroup = MuscleGroup.Shoulders,
        targetValue = 5,
        period = Period.WEEKLY
    )
)
val muscleCounts_Abs = Goal(
    userId = 1,
    goalData = GoalData.MuscleCounts(
        muscleGroup = MuscleGroup.Abs,
        targetValue = 5,
        period = Period.WEEKLY
    )
)


//@Preview
//@Composable
//fun previewWidget_Muscle() {
//    MuscleCountGoalWidget(
//        goalData = muscleCounts_Triceps.goalData,
//        forMuscle = MuscleGroup.Triceps,
//        goalsViewModel = goalsViewModel
//    )
//}


@Composable
fun MuscleCountGoalWidget(
    forMuscle: MuscleGroup,
    goalsViewModel: GoalsViewModel
) {

    val muscleCountData: State<GoalsViewModel.MuscleCountData?> = when(forMuscle){
        MuscleGroup.Back -> {goalsViewModel.backMuscleCountData.collectAsState()}
        MuscleGroup.Chest -> {goalsViewModel.chestMuscleCountData.collectAsState()}
        MuscleGroup.Biceps -> {goalsViewModel.bicepsMuscleCountData.collectAsState()}
        MuscleGroup.Triceps -> {goalsViewModel.tricepsMuscleCountData.collectAsState()}
        MuscleGroup.Legs -> {goalsViewModel.legsMuscleCountData.collectAsState()}
        MuscleGroup.Shoulders -> {goalsViewModel.shouldersMuscleCountData.collectAsState()}
        MuscleGroup.Abs -> {goalsViewModel.absMuscleCountData.collectAsState()}
    }





    var switchToDataForm by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier.heightIn(min = 175.dp,max = 250.dp)
    ) {
        AnimatedVisibility(
            visible = !switchToDataForm,
            enter = slideInVertically (
                initialOffsetY = { it },
                animationSpec = tween(500)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(100)
            )
        ) {
            when (muscleCountData.value) {


                null -> {
                    MuscleCountGoalCard(muscleData = muscleCountData.value, forMuscle = forMuscle, onClickHer = {
                        switchToDataForm = true
                    })
                }

                else -> {
                    MuscleCountGoalCard(muscleData = muscleCountData.value, forMuscle = forMuscle, onClickHer = {
                        switchToDataForm = true
                    })

                }
            }
        }

        AnimatedVisibility(
            visible = switchToDataForm,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(500)
            ),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(100)
            )
        ) {
           MuscleCountGetDataLayout(muscleData = muscleCountData.value, goalsViewModel = goalsViewModel, forMuscle = forMuscle,   onDoneClicked = { switchToDataForm = false })
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuscleCountGetDataLayout(
    onDoneClicked: () -> Unit,
    muscleData: GoalsViewModel.MuscleCountData?,
    goalsViewModel: GoalsViewModel,
    forMuscle: MuscleGroup
){

    var targetValue  by remember {
        mutableStateOf(muscleData?.targetValue?.toString()?:"0")
    }

    var period by remember {
        mutableStateOf(muscleData?.period?.name?:"")
    }


    var updatedPeriod by remember{
        mutableStateOf(muscleData?.period?:Period.WEEKLY)
    }




    Card(
        modifier = Modifier
            .width(175.dp),
        shape = RoundedCornerShape(10),
        backgroundColor = cardsColor,
    ) {
        Column (
            modifier= Modifier
                .fillMaxSize()
                .padding(4.dp)
        ){


            OutlinedTextField(
                value = targetValue,
                onValueChange = {
                                targetValue = it
                },
                label = { Text(text = "Target") },
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    OutlinedTextField(
                        value = period,
                        onValueChange = {},
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
                                androidx.compose.material3.Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = ""
                                )
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
                    if (targetValue != "0"){
                        goalsViewModel.updateMuscleCountGoal(targetMuscle = forMuscle,targetValue = targetValue.toInt(),period = updatedPeriod)
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
private fun MuscleCountGoalCard(
    forMuscle: MuscleGroup,
    onClickHer: () -> Unit,
    muscleData: GoalsViewModel.MuscleCountData?
) {

    var icon: Painter
    val title:String
    if (muscleData != null) {
        title = muscleData.muscleGroup.name
        when (muscleData.muscleGroup) {
            MuscleGroup.Back -> {
                icon = painterResource(id = R.drawable.back_muscle)

            }

            MuscleGroup.Chest -> {
                icon = painterResource(id = R.drawable.chest_muscle)
            }

            MuscleGroup.Biceps -> {
                icon = painterResource(id = R.drawable.biceps_muscle)
            }

            MuscleGroup.Triceps -> {
                icon = painterResource(id = R.drawable.triceps_muscle)
            }

            MuscleGroup.Legs -> {
                icon = painterResource(id = R.drawable.legs_muscle)
            }

            MuscleGroup.Shoulders -> {
                icon = painterResource(id = R.drawable.sholders_muscle)
            }

            MuscleGroup.Abs -> {
                icon = painterResource(id = R.drawable.abs_muscle)
            }
        }
    }else {
        title = forMuscle.name
        when (forMuscle) {
            MuscleGroup.Back -> {
                icon = painterResource(id = R.drawable.back_muscle)
            }

            MuscleGroup.Chest -> {
                icon = painterResource(id = R.drawable.chest_muscle)
            }

            MuscleGroup.Biceps -> {
                icon = painterResource(id = R.drawable.biceps_muscle)
            }

            MuscleGroup.Triceps -> {
                icon = painterResource(id = R.drawable.triceps_muscle)
            }

            MuscleGroup.Legs -> {
                icon = painterResource(id = R.drawable.legs_muscle)
            }

            MuscleGroup.Shoulders -> {
                icon = painterResource(id = R.drawable.sholders_muscle)
            }

            MuscleGroup.Abs -> {
                icon = painterResource(id = R.drawable.abs_muscle)
            }
        }
    }

    Card(
        modifier = Modifier.size(180.dp),
        shape = RoundedCornerShape(10),
        backgroundColor = cardsColor,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {

            Icon(
                painter =icon,
                contentDescription = "",
                Modifier
                    .size(64.dp)
                    .align(Alignment.TopStart)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        color = textColor
                    )
                )

                Text(
                    text = "${muscleData?.totalDuration?.toString()?.take(5)?:"0"} Min",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        color = lightTextColor
                    )
                )

            }





            when (muscleData == null) {
                true -> {
                    Text(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 35.dp),
                        text = "Not set " +
                                "\nYet!",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                            color = textColor,
                            textAlign = TextAlign.Center
                        )
                    )
                }

                false -> {
                    val periodTxt = when(muscleData.period){
                        Period.WEEKLY -> "week"
                        Period.MONTHLY -> "month"
                    }
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 35.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${muscleData.totalCounts} : ${muscleData.targetValue}/$periodTxt",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = textColor,
                                textAlign = TextAlign.Center
                            )
                        )



                        CostumeLinearProgressBar(
                            progress = 30,
                            width = 180.dp,
                            height = 10.dp,
                            color = primaryColor,
                            backgroundColor = lightTextColor,
                            strokeCap = StrokeCap.Round
                        )
                    }

                }
            }


            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(top = 8.dp)
                    .clickable { onClickHer() },
                text = "<<<< Click to Set >>>>",
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




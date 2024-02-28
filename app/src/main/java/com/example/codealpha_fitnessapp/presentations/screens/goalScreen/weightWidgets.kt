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



val weightGoal: Goal = Goal(
    userId = 1,
    goalData = GoalData.WeightLoss(targetValue = 85.0, period = Period.WEEKLY)
)


@Preview
@Composable
fun widgetPreview_Weight() {
//    WeightGoalWidget(goalsViewModel)
}




@Composable
fun WeightGoalWidget(goalsViewModel: GoalsViewModel) {


    val weightData: State<GoalsViewModel.WeightData?> = goalsViewModel.weightWidgetData.collectAsState()
    if (weightData.value != null){
        Log.d("weightData Test 99", "GoalsScreen: ${weightData.value}")
    }



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
            when (weightData.value) {
                null -> {
                    WeightGoalCard_Null(onClickHer = { switchToDataForm = true })
                }

                else -> {
                    WeightGoalCard(data= weightData.value!!,onClickHer = { switchToDataForm = true })

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
            WeightGetDataLayout(data= weightData.value,goalsViewModel= goalsViewModel, onDoneClicked = { switchToDataForm = false })
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightGetDataLayout(
    onDoneClicked: () -> Unit,
    data: GoalsViewModel.WeightData?,
    goalsViewModel: GoalsViewModel
) {
    var targetValue by remember {
        mutableStateOf(data?.targetWeight?.toString() ?: "0.0")
    }


    var currentWeight by remember {
        mutableStateOf(data?.currentWeight?.first?.toString()?:"0.0")
    }
    var period by remember {
        mutableStateOf(data?.period?.name?:"")
    }

    var updatedPeriod by remember{
        mutableStateOf(data?.period?:Period.WEEKLY)
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
                label = { Text(text = "Target Weight") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = primaryColor,
                    focusedLabelColor = primaryColor,
                    cursorColor = primaryColor
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = currentWeight,
                onValueChange = {
                    currentWeight = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "new current weight") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = primaryColor,
                    focusedLabelColor = primaryColor,
                    cursorColor = primaryColor
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
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
                    if(targetValue!="0.0"){
                        goalsViewModel.updateWeightGoal(targetValue.toDouble(),currentWeight.toDouble(),updatedPeriod)
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
private fun WeightGoalCard(onClickHer: () -> Unit, data: GoalsViewModel.WeightData) {
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
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Weight Goal",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            color = textColor
                        )
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "initial Weight:${data.initWeight.first.toString()} kg",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = textColor
                            )
                        )
                        Text(
                            text = "${data.initWeight.second}",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = lightTextColor
                            )
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "current Weight:${data.currentWeight.first.toString()}kg",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = textColor
                            )
                        )
                        Text(
                            text = "${data.currentWeight.second.toString()}",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = lightTextColor
                            )
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "target Weight:${data.targetWeight}kg",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = textColor
                            )
                        )

                    }

                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    val progress = data.progressPercentage
                    CostumeCircularProgressBar(
                        progress = progress.toInt(),
                        haveMedalTxt = true,
                        medalTxt = "${progress.toInt()}%",
                        size = 150.dp,
                        color = primaryColor,
                        strokeCap = StrokeCap.Butt,
                        strokeWidth = 25.dp
                    )
                    val periodTxt = when(data.period){
                        Period.WEEKLY -> "week"
                        Period.MONTHLY -> "month"
                    }
                    Text(
                        text = "${data.weightDifferent.toString()}Kg ,this $periodTxt",
                        style = TextStyle(
                            fontSize = 12.sp,
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
private fun WeightGoalCard_Null(onClickHer: () -> Unit) {
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
                        text = "Weight Goal",
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
                text = "<<<< Click to set Wight Goal >>>>",
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

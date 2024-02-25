package com.example.codealpha_fitnessapp.presentations.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.codealpha_fitnessapp.R
import com.example.codealpha_fitnessapp.presentations.CostumeTopBarComposable
import com.example.codealpha_fitnessapp.presentations.viewModels.BtnStatus
import com.example.codealpha_fitnessapp.presentations.viewModels.PlayWorkoutViewModel
import com.example.codealpha_fitnessapp.presentations.viewModels.TimerItem
import com.example.codealpha_fitnessapp.ui.theme.backgroundColor
import com.example.codealpha_fitnessapp.ui.theme.cardsColor
import com.example.codealpha_fitnessapp.ui.theme.lightTextColor
import com.example.codealpha_fitnessapp.ui.theme.pauseColor
import com.example.codealpha_fitnessapp.ui.theme.playColor
import com.example.codealpha_fitnessapp.ui.theme.primaryColor
import com.example.codealpha_fitnessapp.ui.theme.textColor
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlayScreen(navController: NavHostController, wkId: Int?) {

    val playViewModel: PlayWorkoutViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        playViewModel.passCurrentWorkout(wkId ?: 0)

    }
    val items: List<TimerItem> by playViewModel.timerItemsList.collectAsState()

    val onDone by playViewModel.onDone.collectAsState()
    if (onDone){
        LaunchedEffect(Unit){
            navController.popBackStack()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            CostumeTopBarComposable(
                title = "Play Workout",
                onAddClicked = {},
                haveAddBtn = false,
                onBackClicked = {
                    navController.popBackStack()
                }
            )


            LazyColumn(
                modifier = Modifier.padding(bottom = 60.dp)
            ) {
                items(items) { timerItme ->
                    ExercisePlayItem(timerItme, playViewModel)
                }
            }


        }
        val btnStatus: BtnStatus by playViewModel.btnStatus.collectAsState()
        Log.d("btnStatus99", "WorkoutPlayScreen: btn is : ${btnStatus.name}")
        PlayBtn(
            onClicked = {
                playViewModel.toggleBtnStatus()
            },
            btnStatus = btnStatus,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .align(Alignment.BottomCenter)
        )


    }

}

@Composable
fun ExercisePlayItem(
    timerItem: TimerItem,
    playViewModel: PlayWorkoutViewModel,
) {

    val timerLiveValue: State<String> = playViewModel.timerLiveValue.collectAsState()
    val currentItemIndex: State<Int> = playViewModel.currentItemIndex.collectAsState()
    if (timerItem.index == currentItemIndex.value){
        Log.d("itemTest99", "ExercisePlayItem:on item:${timerItem.index} timerLiveValue:${timerLiveValue.value} when currentItemIndex:${currentItemIndex.value} ")
    }

    when (timerItem.isRestItem) {
        true -> {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                shape = RoundedCornerShape(10),
                colors = CardDefaults.cardColors(
                    containerColor = lightTextColor
                )
            )
            {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp)
                    ) {
                        Text(
                            text = "Rest :${timerItem.duration}",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                color = textColor
                            )
                        )

                    }

                    Text(
                        modifier = Modifier.padding(end = 50.dp),
                        text = if (timerItem.index == currentItemIndex.value) {
                            if(timerLiveValue.value == ""){
                                "${timerItem.duration} sec"
                            }else{
                                "${timerLiveValue.value} sec"
                            }
                        } else {
                            ""
                        },
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            color = primaryColor
                        )
                    )


                }

            }
        }

        false -> {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(10),
                colors = CardDefaults.cardColors(
                    containerColor = cardsColor
                )
            )
            {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp)
                    ) {
                        Text(
                            text = "${timerItem.name}",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                color = textColor
                            )
                        )
                        Text(
                            text = "set:${timerItem.setNumber} X ${timerItem.reps} reps ",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                color = textColor
                            )
                        )
                    }

                    Text(
                        modifier = Modifier.padding(end = 50.dp),
                        text = if (timerItem.index == currentItemIndex.value) {
                            if(timerLiveValue.value == ""){
                                "${timerItem.duration} sec"
                            }else{
                                "${timerLiveValue.value} sec"
                            }
                        } else {
                            ""
                        },
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            color = primaryColor
                        )
                    )


                }

            }
        }
    }

}


@Composable
fun PlayBtn(onClicked: () -> Unit, btnStatus: BtnStatus, modifier: Modifier) {
    Box(modifier = modifier) {
        when (btnStatus) {
            BtnStatus.OnPlay -> {
                Button(
                    onClick = {
                        onClicked()
                    },
                    shape = RoundedCornerShape(10),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = playColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Play",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            color = backgroundColor
                        )
                    )
                }
            }

            BtnStatus.OnPause -> {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = {
                        onClicked()
                    },
                    shape = RoundedCornerShape(10),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = pauseColor
                    )
                ) {
                    Text(
                        text = "Pause",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            color = backgroundColor
                        )
                    )
                }
            }
        }
    }
}


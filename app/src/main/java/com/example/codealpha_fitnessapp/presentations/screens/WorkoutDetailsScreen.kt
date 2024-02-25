package com.example.codealpha_fitnessapp.presentations.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.codealpha_fitnessapp.R
import com.example.codealpha_fitnessapp.operations.dataMangment.Exercise
import com.example.codealpha_fitnessapp.operations.dataMangment.MuscleGroup
import com.example.codealpha_fitnessapp.operations.dataMangment.Workout
import com.example.codealpha_fitnessapp.presentations.CostumeTopBarComposable
import com.example.codealpha_fitnessapp.presentations.navigation.AppDestinations
import com.example.codealpha_fitnessapp.presentations.viewModels.BsLayoutType
import com.example.codealpha_fitnessapp.presentations.viewModels.DetailsEvent
import com.example.codealpha_fitnessapp.presentations.viewModels.DetailsViewModel
import com.example.codealpha_fitnessapp.presentations.viewModels.EditedProperty
import com.example.codealpha_fitnessapp.presentations.viewModels.OperationType
import com.example.codealpha_fitnessapp.ui.theme.backgroundColor
import com.example.codealpha_fitnessapp.ui.theme.cardsColor
import com.example.codealpha_fitnessapp.ui.theme.lightTextColor
import com.example.codealpha_fitnessapp.ui.theme.primaryColor
import com.example.codealpha_fitnessapp.ui.theme.textColor
import kotlinx.coroutines.launch


@Preview
@Composable
fun PreviewDetails() {
    WorkoutDetailsScreen(navController = rememberNavController(), wkId = null)
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutDetailsScreen(navController: NavHostController, wkId: Int?) {

    Log.d("", "WorkoutDetailsScreen: wkId = $wkId")
    val detailsViewModel: DetailsViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        detailsViewModel.initializeReceivedObject(wkId ?: 0)
    }


    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    // scaffold state which control the whole scaffold and get the sheetState argument
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    val scope = rememberCoroutineScope()

    val bottomSheet_isOpen by detailsViewModel.bottomSheetStatus.collectAsState()



    LaunchedEffect(bottomSheet_isOpen) {
        Log.d(
            "bottomSheetTest",
            "WorkoutDetailsScreen: from LaunchedEffect bottomShetIsopen = $bottomSheet_isOpen"
        )
        when (bottomSheet_isOpen) {
            true -> {
                scope.launch {
                    sheetState.expand()
                    Log.d(
                        "bottomSheetTest",
                        "WorkoutDetailsScreen: from the scope sheetState = ${sheetState.currentValue}"
                    )
                }
            }

            false -> {
                scope.launch {
                    sheetState.collapse()
                    Log.d(
                        "bottomSheetTest",
                        "WorkoutDetailsScreen: from the scope sheetState = ${sheetState.currentValue}"
                    )
                }
            }
        }
    }



    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {

            val requestedBottomSheetLayout by detailsViewModel.requestBottomSheetLayout.collectAsState()
            Log.d(
                "bottomSheetTest",
                "WorkoutDetailsScreen: from the bottomSheetScafold  requestedLayout:${requestedBottomSheetLayout.name}"
            )

            AnimatedVisibility(visible = requestedBottomSheetLayout == BsLayoutType.EXERCISE) {
                ExerciseBottomSheetLayout(
                    onCancelClicked = {
                        detailsViewModel.setEvent(DetailsEvent.CloseBottomSheet)
                    },
                    onAddClicked = {
                        detailsViewModel.saveNewExercise()
                    },
                    detailViewModel = detailsViewModel
                )
                Log.d(
                    "bottomSheetTest",
                    "WorkoutDetailsScreen: from the AnimatedVisibility  requestedLayout:${requestedBottomSheetLayout.name}"
                )
            }
            AnimatedVisibility(visible = requestedBottomSheetLayout == BsLayoutType.WORKOUT) {
                WorkoutBottomSheetLayout(
                    navController = navController,
                    detailsViewModel = detailsViewModel,
                    onCancelClicked = {
                        detailsViewModel.setEvent(DetailsEvent.CloseBottomSheet)
                    },
                    onSaveClicked = {

                    })
                Log.d(
                    "bottomSheetTest",
                    "WorkoutDetailsScreen: from the AnimatedVisibility  requestedLayout:${requestedBottomSheetLayout.name}"
                )
            }
        },
        sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
        sheetPeekHeight = 0.dp

    ) {
        DetailScreenMainContent(detailsViewModel, navController)


    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutBottomSheetLayout(
    onSaveClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    detailsViewModel: DetailsViewModel,
    navController: NavHostController
) {
    val sessionObject: State<Workout?> = detailsViewModel.sessionObject.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(cardsColor)
            .padding(16.dp)
    ) {
        Box(
            Modifier.fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier
                    .clickable { onCancelClicked() }
                    .align(Alignment.CenterStart),
                text = "Cancel",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = primaryColor
                )
            )


            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Name Your Workout",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    color = textColor
                )
            )


            val context = LocalContext.current
            Text(
                modifier = Modifier
                    .clickable {
                        if (sessionObject.value?.title != "" && sessionObject.value?.muscleGroup != null) {
                            detailsViewModel.setEvent(DetailsEvent.UpdateDatabaseObject)
                            navController.navigate(AppDestinations.WorkoutsScreen.rout){
                                popUpTo(AppDestinations.WorkoutsScreen.rout){inclusive =true}
                            }
                        } else {
                            Toast
                                .makeText(
                                    context,
                                    "Please Don't leave the title or the muscle grope fields empty ",
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        }

                    }
                    .align(Alignment.CenterEnd),
                text = "Save",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = primaryColor
                )
            )


        }
        OutlinedTextField(
            value = sessionObject.value?.title ?: "",
            onValueChange = {
                detailsViewModel.updateWorkoutTitle(it)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Exercise Name") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryColor,
                focusedLabelColor = primaryColor,
                cursorColor = primaryColor
            ),
            keyboardOptions = KeyboardOptions.Default,
        )

        var dropDown by remember {
            mutableStateOf(false)
        }


        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var chosenMuscleGroup by remember {
                mutableStateOf(sessionObject.value?.muscleGroup?.name ?: "")
            }

            OutlinedTextField(
                value = sessionObject.value?.muscleGroup?.name ?: "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Muscle Group")
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
                MuscleGroup.values().forEach { groupItem ->
                    DropdownMenuItem(
                        modifier = Modifier
                            .width(100.dp)
                            .height(20.dp),
                        onClick = {
                            chosenMuscleGroup = groupItem.name
                            detailsViewModel.updateWorkoutMuscleGroup(groupItem)
                            dropDown = false
                        }) {
                        Text(text = groupItem.name)
                    }
                }

            }
        }



        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                modifier = Modifier
                    .clickable {
                        detailsViewModel.updateWorkoutResDuration(
                            operationType = OperationType.DECREASE,
                            editedProperty = EditedProperty.REST_DURATION_10
                        )
                    },
                text = "-",
                style = TextStyle(
                    fontSize = 40.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_black)),
                    color = primaryColor
                )
            )


            Text(
                text = "duration:${sessionObject.value?.restDuration?:""}",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    color = textColor
                )
            )



            Text(
                modifier = Modifier
                    .clickable {
                        detailsViewModel.updateWorkoutResDuration(
                            operationType = OperationType.INCREASE,
                            editedProperty = EditedProperty.REST_DURATION_10
                        )
                    },
                text = "+",
                style = TextStyle(
                    fontSize = 40.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_black)),
                    color = primaryColor
                )
            )


        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseBottomSheetLayout(
    onAddClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    detailViewModel: DetailsViewModel
) {

    val newExercise = detailViewModel.newExercise.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(cardsColor)
            .padding(16.dp)
    ) {
        Box(
            Modifier.fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier
                    .clickable { onCancelClicked() }
                    .align(Alignment.CenterStart),
                text = "Cancel",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = primaryColor
                )
            )


            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Create Exercise",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = textColor
                )
            )


            Text(
                modifier = Modifier
                    .clickable { onAddClicked() }
                    .align(Alignment.CenterEnd),
                text = "Add",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = primaryColor
                )
            )


        }
        OutlinedTextField(
            value = newExercise.value.name,
            onValueChange = {
                detailViewModel.updateExerciseName(it)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Exercise Name") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryColor,
                focusedLabelColor = primaryColor,
                cursorColor = primaryColor
            ),
            keyboardOptions = KeyboardOptions.Default,
        )



        val isCardio by detailViewModel.isCardio.collectAsState()

        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                modifier = Modifier
                    .clickable {
                        detailViewModel.ConvertToCardio(false)
                               },
                text = "Weight",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    color = when (isCardio) {
                        true -> {
                            lightTextColor
                        }

                        false -> {
                            primaryColor
                        }
                    }
                )
            )

            Text(
                modifier = Modifier
                    .clickable {
                        detailViewModel.ConvertToCardio(true)

                    },
                text = "Cardio",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    color = when (isCardio) {
                        true -> {
                            primaryColor
                        }

                        false -> {
                            lightTextColor
                        }
                    }
                )
            )
        }

        AnimatedVisibility(visible = !isCardio) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    modifier = Modifier
                        .clickable {
                            Log.d(
                                "bottomTest99",
                                "ExerciseBottomSheetLayout: clicking + btn calling editExerciseObject +, sets"
                            )
                            detailViewModel.editExerciseObject(
                                OperationType.DECREASE,
                                EditedProperty.SET_1
                            )
                        },
                    text = "-",
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_black)),
                        color = primaryColor
                    )
                )


                Text(
                    text = "Sets:${newExercise.value.sets}",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                        color = textColor
                    )
                )



                Text(
                    modifier = Modifier
                        .clickable {
                            Log.d(
                                "bottomTest99",
                                "ExerciseBottomSheetLayout: clicking + btn calling editExerciseObject -, sets"
                            )
                            detailViewModel.editExerciseObject(
                                OperationType.INCREASE,
                                EditedProperty.SET_1
                            )
                        },
                    text = "+",
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_black)),
                        color = primaryColor
                    )
                )


            }


        }


        AnimatedVisibility(visible = !isCardio) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    modifier = Modifier
                        .clickable {
                            detailViewModel.editExerciseObject(
                                OperationType.DECREASE,
                                EditedProperty.REP_1
                            )
                        },
                    text = "-",
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_black)),
                        color = primaryColor
                    )
                )


                Text(
                    text = "Reps:${newExercise.value.reps}",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                        color = textColor
                    )
                )



                Text(
                    modifier = Modifier
                        .clickable {
                            detailViewModel.editExerciseObject(
                                OperationType.INCREASE,
                                EditedProperty.REP_1
                            )
                        },
                    text = "+",
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_black)),
                        color = primaryColor
                    )
                )


            }
        }



        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                modifier = Modifier
                    .clickable {
                        detailViewModel.editExerciseObject(
                            OperationType.DECREASE,
                            EditedProperty.DURATION_10
                        )
                    },
                text = "-",
                style = TextStyle(
                    fontSize = 40.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_black)),
                    color = primaryColor
                )
            )


            Text(
                text = "duration:${newExercise.value.duration}",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                    color = textColor
                )
            )



            Text(
                modifier = Modifier
                    .clickable {
                        detailViewModel.editExerciseObject(
                            OperationType.INCREASE,
                            EditedProperty.DURATION_10
                        )
                    },
                text = "+",
                style = TextStyle(
                    fontSize = 40.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_black)),
                    color = primaryColor
                )
            )


        }


    }
}

@Composable
fun DetailScreenMainContent(detailsViewModel: DetailsViewModel, navController: NavHostController) {
    val sessionObject: State<Workout?> = detailsViewModel.sessionObject.collectAsState()
    val exercises = detailsViewModel.exercisesList.collectAsState()
    val saveBtnIsVisible by detailsViewModel.mainSaveBtnVisible.collectAsState()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {

            CostumeTopBarComposable(
                title = sessionObject.value?.title ?: "",
                onAddClicked = {
                    detailsViewModel.setEvent(DetailsEvent.OpenBottomSheetEvent(BsLayoutType.EXERCISE))
                    Log.d(
                        "bottomSheetTest",
                        "DetailScreenMainContent: OpenBottomSheetEvent(BsLayoutType.EXERCISE "
                    )
                },
                haveAddBtn = true,
                onBackClicked = {
                    when (saveBtnIsVisible) {
                        true -> {
                            detailsViewModel.setEvent(DetailsEvent.OpenBottomSheetEvent(BsLayoutType.WORKOUT))
                        }

                        false -> {
                            navController.popBackStack()
                        }
                    }
                }
            )

            val items = emptyList<Exercise>()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 4.dp)
            ) {
                Log.d("saveNewExercise", "DetailScreenMainContent: exercises:${exercises.value}")
                items(exercises.value) { exItem ->
                    ExerciseItem(
                        exercise = exItem,
                        onXClicked = {
                            detailsViewModel.deleteExercise(ex = exItem)
                        })

                }
            }
        }


        //depends on the value of _sessionObject != receivedObject use AnimationVisibility
        Log.d(":xcv99:", "DetailScreenMainContent: saveBtnVisibility:$saveBtnIsVisible")
        AnimatedVisibility(
            visible = saveBtnIsVisible,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth(),
                onClick = {
                    detailsViewModel.setEvent(DetailsEvent.OpenBottomSheetEvent(BsLayoutType.WORKOUT))
                },
                shape = RoundedCornerShape(10),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor
                )
            ) {
                Text(
                    text = "Save",
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
fun ExerciseItem(exercise: Exercise, onXClicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .height(50.dp),
        shape = RoundedCornerShape(5),
        colors = CardDefaults.cardColors(
            containerColor = cardsColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(
                text = exercise.name,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = textColor
                )
            )

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {

                Text(
                    text = "sets:${exercise.sets}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        color = textColor
                    )
                )
                Text(
                    text = "reps:${exercise.reps}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        color = textColor
                    )
                )
                Text(
                    text = "duration:${exercise.duration}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        color = textColor
                    )
                )
            }

            IconButton(onClick = { onXClicked() }) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "",
                    Modifier.size(32.dp)
                )
            }


        }
    }
}


package com.example.codealpha_fitnessapp.presentations.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.codealpha_fitnessapp.R
import com.example.codealpha_fitnessapp.presentations.navigation.AppDestinations
import com.example.codealpha_fitnessapp.presentations.viewModels.AuthEvents
import com.example.codealpha_fitnessapp.presentations.viewModels.AuthViewModel
import com.example.codealpha_fitnessapp.ui.theme.backgroundColor
import com.example.codealpha_fitnessapp.ui.theme.cardsColor
import com.example.codealpha_fitnessapp.ui.theme.primaryColor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SignUpScreen(navController: NavController, authViewModel: AuthViewModel? = null) {
    val announcementMsg: String by authViewModel!!.announceMessage.collectAsState()

    if (announcementMsg.isNotEmpty()) {
        Toast.makeText(LocalContext.current, announcementMsg, Toast.LENGTH_SHORT).show()
        authViewModel!!.announceMessage("")
    }






    // the sheet state which starts with
    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    // scaffold state which control the whole scaffold and get the sheetState argument
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )


    BottomSheetScaffold(
        //first pass the scaffoldState
        scaffoldState = scaffoldState,
        sheetContent = {
            //bottomSheet Content
            SignUpBottomSheetContent(navController,authViewModel)
        },
//        sheetBackgroundColor = primaryColor,
        // the height where the sheet will be while the stable collapse state
        sheetPeekHeight = 600.dp,
        sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
    ) {
//            screen Content
        // the remain part of the screen without the bottomSheet where you can call the screen composable function
        Box(

            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.signup_img),
                contentDescription = "",
                modifier = Modifier.align(Alignment.TopCenter),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(start = 16.dp, end = 16.dp, bottom = 450.dp)
            ) {
                Text(
                    text = "Sign Up",
                    style = TextStyle(
                        fontSize = 36.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_semi_bold)),
                        color = primaryColor
                    )
                )

                Text(
                    text = "please Log In to continue using the app ",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        color = backgroundColor
                    )
                )
            }


        }
    }


}


@Preview
@Composable
fun SignUpPreview() {
    SignUpScreen(navController = rememberNavController())
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpBottomSheetContent(
    navController: NavController,
    authViewModel: AuthViewModel?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .background(cardsColor)
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {




        val name = remember {
            mutableStateOf("")
        }
        EditTextField(
            labelValue = " Name",
            icon = painterResource(id = R.drawable.big_user),
            editableTxt = name
        )


        val weight = remember {
            mutableStateOf("")
        }
        EditTextField(
            labelValue = " Weight",
            icon = painterResource(id = R.drawable.weight),
            editableTxt = weight
        )


        val email = remember {
            mutableStateOf("")
        }
        EditTextField(
            labelValue = " Email",
            icon = painterResource(id = R.drawable.email),
            editableTxt = email
        )



        val password = remember {
            mutableStateOf("")
        }
        PasswordTextField(
            labelValue = "Password",
            icon = painterResource(id = R.drawable.password),
            password = password
        )


        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                // viewModel.createUser
                      authViewModel!!.setEvent(AuthEvents.CreateUSerEvent(
                          name = name.value,
                          weight = weight.value.toDouble(),
                          email = email.value,
                          pass = password.value
                      ))
                      },
            shape = RoundedCornerShape(10),
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor
            )
        ) {
            Text(
                text = stringResource(id = R.string.sign_up),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_semi_bold)),
                    color = backgroundColor
                )
            )
        }

        AnnotatedText(
            normalText = stringResource(R.string.already_have_an_account),
            annotatedText = stringResource(R.string.log_in),
            onclick = {
                navController.popBackStack()
            })

    }
}
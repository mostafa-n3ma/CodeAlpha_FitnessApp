package com.example.codealpha_fitnessapp.presentations.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.codealpha_fitnessapp.R
import com.example.codealpha_fitnessapp.presentations.navigation.AppDestinations
import com.example.codealpha_fitnessapp.presentations.viewModels.AuthViewModel
import com.example.codealpha_fitnessapp.ui.theme.primaryColor
import com.google.firebase.auth.FirebaseUser


@Composable
fun WelcomeScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    val user: State<FirebaseUser?> = authViewModel.user.collectAsState()
    authViewModel.checkAndUpdateUserStatus()
    if (user.value != null){
        navController.navigate(AppDestinations.DashboardScreen.rout){
            popUpTo(AppDestinations.WelcomeScreen.rout){inclusive = true}
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.run),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 200.dp)
                .align(Alignment.TopCenter),
            text = stringResource(R.string.welcome_msg),
            style = TextStyle(
                color = Color.White,
                fontSize = 28.sp,
                fontFamily = FontFamily(Font(R.font.poppins_bold))
            )
        )
        WelcomeStartButton(
            txt = "Start",
            onClick = {
                navController.navigate(AppDestinations.LogInScreen.rout) {
                    // Ensure that the WelcomeScreen is popped off the back stack
                    popUpTo(AppDestinations.WelcomeScreen.rout) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .height(55.dp)
                .align(Alignment.BottomCenter)

        )
    }

}

@Composable
fun WelcomeStartButton(txt: String, onClick: () -> Unit, modifier: Modifier) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = primaryColor
        ),
        shape = RoundedCornerShape(25)
    ) {
        Text(
            text = txt,
            style = TextStyle(
                color = Color.White,
                fontSize = 28.sp,
                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                textAlign = TextAlign.Center
            ),
        )
    }
}

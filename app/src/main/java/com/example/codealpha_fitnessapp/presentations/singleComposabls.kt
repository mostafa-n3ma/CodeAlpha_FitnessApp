package com.example.codealpha_fitnessapp.presentations

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codealpha_fitnessapp.R
import com.example.codealpha_fitnessapp.operations.dataMangment.MuscleGroup
import com.example.codealpha_fitnessapp.ui.theme.backgroundColor
import com.example.codealpha_fitnessapp.ui.theme.cardsColor
import com.example.codealpha_fitnessapp.ui.theme.lightTextColor
import com.example.codealpha_fitnessapp.ui.theme.primaryColor
import com.example.codealpha_fitnessapp.ui.theme.textColor
import com.example.codealpha_fitnessapp.ui.theme.tobBarColor


@ExperimentalMaterial3Api
@Composable
fun EditTextField(labelValue: String, icon: Painter, editableTxt: MutableState<String>) {
    OutlinedTextField(
        value = editableTxt.value,
        onValueChange = { editableTxt.value = it },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = primaryColor,
            focusedLabelColor = primaryColor,
            cursorColor = primaryColor
        ),
        keyboardOptions = KeyboardOptions.Default,
        leadingIcon = {
            Icon(
                painter = icon,
                contentDescription = "email text field",
                modifier = Modifier.size(32.dp)
            )
        }
    )


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(labelValue: String, icon: Painter, password: MutableState<String>) {

    val passwordVisible = remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = password.value,
        onValueChange = { password.value = it },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedLabelColor = primaryColor,
            focusedBorderColor = primaryColor,
            cursorColor = primaryColor
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        label = { Text(text = labelValue) },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                painter = icon,
                contentDescription = "password leading icon",
                modifier = Modifier.size(32.dp)
            )
        },
        trailingIcon = {
            val iconImage = if (passwordVisible.value) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            val description = if (passwordVisible.value) {
                stringResource(R.string.hide_password)
            } else {
                stringResource(R.string.show_password)
            }

            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }
        },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation()
    )


}


@Composable
fun AnnotatedText(normalText: String, annotatedText: String, onclick: () -> Unit) {

    val annotatedString = buildAnnotatedString {
        append(normalText)
        append(" ")
        withStyle(SpanStyle(color = primaryColor)) {
            pushStringAnnotation(tag = annotatedText, annotation = annotatedText)
            append(annotatedText)
        }
    }

    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .padding(top = 8.dp),
        text = annotatedString,
        style = TextStyle(
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.poppins_semi_bold)),
            color = lightTextColor
        ),
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    if (span.tag == annotatedText) {
                        onclick()
                    }
                }
        })

}


@Composable
fun Head2Text(txt: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        text = txt,
        style = TextStyle(
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.poppins_regular)),
            color = textColor
        )
    )
}


@Composable
fun CostumeTopBarComposable(
    title: String,
    onAddClicked: () -> Unit,
    haveAddBtn: Boolean,
    onBackClicked: () -> Unit,
    havBackBtn:Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(tobBarColor)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ) {
        if (havBackBtn){
            IconButton(onClick = { onBackClicked() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "",
                    tint = primaryColor,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Text(
            text = "$title",
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                color = textColor
            )
        )


        Box(
            modifier = Modifier.size(35.dp)
        ) {
            if (haveAddBtn) {
                IconButton(onClick = {
                    onAddClicked()
                    Log.d("bottomSheetTest", "CostumeTopBarComposable: add btn clicked")
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.add),
                        contentDescription = "",
                        tint = primaryColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }


    }
}


@Composable
fun WorkoutItemCard(title:String,muscleGroup: MuscleGroup,duration:Double,onStartClick:()->Unit, onDeleteClicked:()->Unit,onEditClicked:()->Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        shape = RoundedCornerShape(5),
        colors = CardDefaults.cardColors(
            containerColor = cardsColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Column {
                Column(
                    modifier = Modifier.padding(top = 8.dp, start = 8.dp)
                ) {

                    Text(
                        text = title.take(20),
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            color = textColor
                        )
                    )

                    Text(
                        text = "Muscles: ${muscleGroup.name}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                            color = textColor
                        )
                    )


                    Text(
                        text = "${duration.toString().take(4)} Minutes",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                            color = textColor
                        )
                    )
                }

                Row {
                    IconButton(onClick = { onDeleteClicked() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.delete),
                            contentDescription = "",
                            Modifier.size(33.dp),
                            tint = primaryColor
                        )
                    }


                    IconButton(onClick = {onEditClicked() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.edit),
                            contentDescription = "",
                            Modifier.size(30.dp),
                            tint = primaryColor
                        )
                    }
                }

            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Button(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(100.dp, 45.dp),
                    onClick = {onStartClick()},
                    shape = RoundedCornerShape(10),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor
                    )
                ) {
                    Text(
                        text = "Start",
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
}


@Preview()
@Composable
fun DateItemPreview() {
    WorkoutItemCard(title = "Workout Name", muscleGroup = MuscleGroup.Abs, duration = 5.6, onStartClick = {}, onDeleteClicked = {}, onEditClicked = {})
}
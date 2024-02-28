package com.example.codealpha_fitnessapp.presentations.screens.goalScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.codealpha_fitnessapp.R
import com.example.codealpha_fitnessapp.ui.theme.textColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun CostumeCircularProgressBar(
    modifier: Modifier? = null,
    progress: Int,
    haveMedalTxt: Boolean,
    medalTxt: String,
    size: Dp,
    color: Color,
    strokeWidth: Dp,
    strokeCap: StrokeCap
) {
    Box(
        modifier = modifier ?: Modifier
    ) {
        var progressDelay by remember {
            mutableFloatStateOf(0f)
        }

        val scope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            scope.launch {
                repeat(progress) {
                    delay(1)
                    progressDelay += 0.01f
                }
            }
        }
        CircularProgressIndicator(
            modifier = Modifier
                .size(size)
                .rotate(180f),
            progress = progressDelay,
            color = color,
            strokeWidth = strokeWidth,
            strokeCap = strokeCap
        )

        if (haveMedalTxt) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = medalTxt,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    color = textColor
                )
            )
        }

    }
}


@Composable
fun CostumeLinearProgressBar(
    progress: Int,
    width: Dp,
    height: Dp,
    color: Color,
    backgroundColor: Color,
    strokeCap: StrokeCap
) {

    var progressDelay by remember {
        mutableFloatStateOf(0f)
    }

    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            repeat(progress) {
                delay(1)
                progressDelay += 0.01f
            }
        }
    }

    LinearProgressIndicator(
        modifier = Modifier
            .width(width)
            .height(height)
            .rotate(180f),
        backgroundColor = backgroundColor,
        strokeCap = strokeCap,
        progress = progressDelay,
        color = color
    )
}


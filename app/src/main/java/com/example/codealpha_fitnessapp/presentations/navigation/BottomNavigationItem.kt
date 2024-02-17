package com.example.codealpha_fitnessapp.presentations.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.codealpha_fitnessapp.R

data class BottomNavigationItem(
    val title:String,
    val selectedIcon:Painter,
    val unSelectedIcon:Painter
)



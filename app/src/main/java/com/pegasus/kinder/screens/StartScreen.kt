package com.pegasus.kinder.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pegasus.kinder.R
import com.pegasus.kinder.components.BackgroundImage
import com.pegasus.kinder.navigation.Destination

@Composable
fun StartScreen(
    navController: NavController
) {
    BackgroundImage {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.start_button),
                contentDescription = "Start",
                modifier = Modifier
                    .size(250.dp)
                    .clickable {
                        navController.navigate(Destination.LanguageSelection.route)
                    }
            )
        }
    }
} 
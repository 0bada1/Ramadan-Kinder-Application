package com.pegasus.kinder.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pegasus.kinder.R
import com.pegasus.kinder.navigation.Destination

@Composable
fun HomeButton(
    navController: NavController,
    language: String = "en"
) {
    Image(
        painter = painterResource(
            id = if (language == "en") {
                R.drawable.homepage_button
            } else {
                R.drawable.ar_homepage
            }
        ),
        contentDescription = if (language == "en") "Home" else "الصفحة الرئيسية",
        modifier = Modifier
            .padding(16.dp)
            .size(
            width = 100.dp, height = 50.dp)
            .clickable {
                navController.navigate(Destination.Start.route) {
                    popUpTo(Destination.Start.route) {
                        inclusive = true
                    }
                }
            }
    )
} 
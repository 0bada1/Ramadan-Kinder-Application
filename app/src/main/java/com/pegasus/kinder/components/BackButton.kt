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

@Composable
fun BackButton(
    navController: NavController
) {
    Image(
        painter = painterResource(id = R.drawable.back),
        contentDescription = "Back",
        modifier = Modifier
            .padding(16.dp)
            .size(50.dp)
            .clickable {
                navController.popBackStack()
            }
    )
} 
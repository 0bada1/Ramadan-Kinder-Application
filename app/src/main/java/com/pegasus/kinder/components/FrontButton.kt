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
fun FrontButton(
    navController: NavController,
    enabled: Boolean = false
) {
    Image(
        painter = painterResource(id = R.drawable.front),
        contentDescription = "Forward",
        modifier = Modifier
            .padding(16.dp)
            .size(200.dp)
            .clickable(enabled = enabled) {
                // Only navigate forward if there's a forward destination
                navController.currentBackStackEntry?.destination?.route?.let { currentRoute ->
                    when (currentRoute) {
                        "language_selection" -> navController.navigate("questions")
                        "questions" -> navController.navigate("user_info/false/false/en")
                        // Add other routes as needed
                    }
                }
            }
    )
} 
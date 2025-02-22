package com.pegasus.kinder.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pegasus.kinder.navigation.Destination
import com.pegasus.kinder.R
import androidx.compose.ui.draw.alpha
import com.pegasus.kinder.components.BackgroundImage
import com.pegasus.kinder.components.HomeButton
import com.pegasus.kinder.components.BackButton

@Composable
fun LanguageSelectionScreen(
    navController: NavController,
    onLanguageSelected: (String) -> Unit
) {
    BackgroundImage {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                BackButton(navController)
                HomeButton(navController)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                
                // Language Question Images (not clickable)
                Image(
                    painter = painterResource(id = R.drawable.english_language),
                    contentDescription = "English Question",
                    modifier = Modifier
                        .size(width = 300.dp, height = 80.dp)
                )
                
                Spacer(modifier = Modifier.height(10.dp))
                
                Image(
                    painter = painterResource(id = R.drawable.arabic_language),
                    contentDescription = "Arabic Question",
                    modifier = Modifier
                        .size(width = 300.dp, height = 80.dp)
                )
                
                Spacer(modifier = Modifier.height(30.dp))
                
                // Language Selection Buttons
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // English Button
                    Image(
                        painter = painterResource(id = R.drawable.en_button),
                        contentDescription = "Select English",
                        modifier = Modifier
                            .size(200.dp)
                            .clickable {
                                onLanguageSelected("en")
                                navController.navigate(Destination.Questions.route) {
                                    popUpTo(Destination.LanguageSelection.route) { inclusive = true }
                                }
                            }
                    )
                    
                    Spacer(modifier = Modifier.width(10.dp)) // Small gap between buttons
                    
                    // Arabic Button
                    Image(
                        painter = painterResource(id = R.drawable.ar_button),
                        contentDescription = "Select Arabic",
                        modifier = Modifier
                            .size(200.dp)
                            .clickable {
                                onLanguageSelected("ar")
                                navController.navigate(Destination.Questions.route) {
                                    popUpTo(Destination.LanguageSelection.route) { inclusive = true }
                                }
                            }
                    )
                }
                
                // Hidden buttons for accessibility (if needed)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            onLanguageSelected("en")
                            navController.navigate(Destination.Questions.route) {
                                popUpTo(Destination.LanguageSelection.route) { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .alpha(0f)
                            .size(0.dp)
                    ) {
                        Text("English")
                    }
                    
                    Button(
                        onClick = {
                            onLanguageSelected("ar")
                            navController.navigate(Destination.Questions.route) {
                                popUpTo(Destination.LanguageSelection.route) { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .alpha(0f)
                            .size(0.dp)
                    ) {
                        Text("العربية")
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
} 
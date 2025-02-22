package com.pegasus.kinder.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pegasus.kinder.R
import com.pegasus.kinder.navigation.Destination
import com.pegasus.kinder.components.BackgroundImage
import com.pegasus.kinder.components.HomeButton
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.pegasus.kinder.components.BackButton
import androidx.compose.foundation.clickable

@Composable
fun UserInfoScreen(
    navController: NavController,
    language: String,
    consent1: Boolean,
    consent2: Boolean
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var isNameFieldFocused by remember { mutableStateOf(false) }
    var isAgeFieldFocused by remember { mutableStateOf(false) }

    BackgroundImage {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                BackButton(navController)
                HomeButton(navController, language)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Name field with swappable background
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(
                            id = if (isNameFieldFocused || name.isNotEmpty()) {
                                R.drawable.blank_field
                            } else if (language == "en") {
                                R.drawable.name_field
                            } else {
                                R.drawable.ar_name
                            }
                        ),
                        contentDescription = if (language == "en") "Name field" else "حقل الاسم",
                        modifier = Modifier
                            .size(width = 400.dp, height = 100.dp)
                            .padding(4.dp)
                    )
                    
                    BasicTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier
                            .width(380.dp)
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .onFocusChanged { focusState ->
                                isNameFieldFocused = focusState.isFocused
                            },
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(Color.Black)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Age field with swappable background
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(
                            id = if (isAgeFieldFocused || age.isNotEmpty()) {
                                R.drawable.blank_field
                            } else if (language == "en") {
                                R.drawable.age_field
                            } else {
                                R.drawable.ar_age
                            }
                        ),
                        contentDescription = if (language == "en") "Age field" else "حقل العمر",
                        modifier = Modifier
                            .size(width = 400.dp, height = 100.dp)
                            .padding(4.dp)
                    )
                    
                    BasicTextField(
                        value = age,
                        onValueChange = { age = it },
                        modifier = Modifier
                            .width(380.dp)
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .onFocusChanged { focusState ->
                                isAgeFieldFocused = focusState.isFocused
                            },
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        cursorBrush = SolidColor(Color.Black)
                    )
                }

                // Continue button
                if (name.isNotEmpty() && age.isNotEmpty()) {
                    Image(
                        painter = painterResource(
                            id = if (language == "en") {
                                R.drawable.next_button
                            } else {
                                R.drawable.ar_next
                            }
                        ),
                        contentDescription = if (language == "en") "Continue" else "متابعة",
                        modifier = Modifier
                            .size(200.dp)
                            .clickable {
                                navController.navigate(
                                    Destination.Recording.createRecordingRoute(
                                        language = language,
                                        name = name,
                                        age = age,
                                        consent1 = consent1,
                                        consent2 = consent2
                                    )
                                )
                            }
                    )
                }
            }
        }
    }
} 
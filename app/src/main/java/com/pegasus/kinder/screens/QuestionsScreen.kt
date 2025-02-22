package com.pegasus.kinder.screens

import android.util.Log
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
import com.pegasus.kinder.navigation.Destination
import androidx.compose.foundation.layout.Arrangement
import com.pegasus.kinder.components.BackgroundImage
import androidx.compose.foundation.clickable
import com.pegasus.kinder.R
import com.pegasus.kinder.components.HomeButton
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.focus.onFocusChanged
import com.pegasus.kinder.components.BackButton

@Composable
fun QuestionsScreen(
    navController: NavController,
    language: String
) {
    var consent1 by remember { mutableStateOf<Boolean?>(null) }
    var consent2 by remember { mutableStateOf<Boolean?>(null) }

    val questions = if (language == "en") {
        listOf(
            "Do you give consent to be recorded?",
            "Do you give consent to use your recorded film on our social media channels?"
        )
    } else {
        listOf(
            "هل توافق على التسجيل؟",
            "هل توافق على استخدام الفيديو المسجل على قنوات التواصل الاجتماعي الخاصة بنا؟"
        )
    }

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
                // First Question with its buttons
                Image(
                    painter = painterResource(
                        id = if (language == "en") {
                            R.drawable.question1
                        } else {
                            R.drawable.ar_question1
                        }
                    ),
                    contentDescription = if (language == "en") "First Question" else "السؤال الأول",
                    modifier = Modifier
                        .padding(bottom = 0.dp)
                        .size(
                            width = if (language == "en") 500.dp else 300.dp,
                            height = 100.dp
                        )
                )

                // Yes/No Buttons for first question
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Yes Button
                    Image(
                        painter = painterResource(
                            id = if (consent1 == true) {
                                if (language == "en") R.drawable.green_yes else R.drawable.ar_green_yes
                            } else if (language == "en") {
                                R.drawable.yes_button
                            } else {
                                R.drawable.ar_yes
                            }
                        ),
                        contentDescription = if (language == "en") "Yes" else "نعم",
                        modifier = Modifier
                            .size(200.dp)
                            .clickable {
                                consent1 = true
                                Log.d("QuestionsScreen", "Question 1 answer set to: $consent1")
                            }
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    // No Button
                    Image(
                        painter = painterResource(
                            id = if (consent1 == false) {
                                if (language == "en") R.drawable.red_no else R.drawable.ar_red_no
                            } else if (language == "en") {
                                R.drawable.no_button
                            } else {
                                R.drawable.ar_no
                            }
                        ),
                        contentDescription = if (language == "en") "No" else "لا",
                        modifier = Modifier
                            .size(200.dp)
                            .clickable {
                                consent1 = false
                                Log.d("QuestionsScreen", "Question 1 answer set to: $consent1")
                            }
                    )
                }

                // Second Question with its buttons
                Image(
                    painter = painterResource(
                        id = if (language == "en") {
                            R.drawable.question2
                        } else {
                            R.drawable.ar_question2
                        }
                    ),
                    contentDescription = if (language == "en") "Second Question" else "السؤال الثاني",
                    modifier = Modifier
                        .padding(bottom = 0.dp)
                        .size(width = 700.dp, height = 100.dp)
                )

                // Yes/No Buttons for second question
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Yes Button
                    Image(
                        painter = painterResource(
                            id = if (consent2 == true) {
                                if (language == "en") R.drawable.green_yes else R.drawable.ar_green_yes
                            } else if (language == "en") {
                                R.drawable.yes_button
                            } else {
                                R.drawable.ar_yes
                            }
                        ),
                        contentDescription = if (language == "en") "Yes" else "نعم",
                        modifier = Modifier
                            .size(200.dp)
                            .clickable {
                                consent2 = true
                                Log.d("QuestionsScreen", "Question 2 answer set to: $consent2")
                            }
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    // No Button
                    Image(
                        painter = painterResource(
                            id = if (consent2 == false) {
                                if (language == "en") R.drawable.red_no else R.drawable.ar_red_no
                            } else if (language == "en") {
                                R.drawable.no_button
                            } else {
                                R.drawable.ar_no
                            }
                        ),
                        contentDescription = if (language == "en") "No" else "لا",
                        modifier = Modifier
                            .size(200.dp)
                            .clickable {
                                consent2 = false
                                Log.d("QuestionsScreen", "Question 2 answer set to: $consent2")
                            }
                    )
                }

                // Continue Button - only visible when both questions are answered
                if (consent1 != null && consent2 != null) {
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
                                Log.d("QuestionsScreen", "Navigating to UserInfo with consent1: $consent1, consent2: $consent2")
                                navController.navigate("user_info/${consent1}/${consent2}/$language")
                            }
                    )
                }
            }
        }
    }
} 
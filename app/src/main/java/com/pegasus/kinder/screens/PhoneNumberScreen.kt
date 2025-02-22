package com.pegasus.kinder.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pegasus.kinder.R
import com.pegasus.kinder.components.BackgroundImage
import com.pegasus.kinder.components.HomeButton
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import com.pegasus.kinder.utils.CsvHelper
import com.pegasus.kinder.components.BackButton

@Composable
fun PhoneNumberScreen(
    navController: NavController,
    language: String
) {
    val context = LocalContext.current
    val csvHelper = remember { CsvHelper(context) }
    var phoneNumber by remember { mutableStateOf("") }
    var isPhoneFieldFocused by remember { mutableStateOf(false) }

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
                // Phone number field with swappable background
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(
                            id = if (isPhoneFieldFocused || phoneNumber.isNotEmpty()) {
                                R.drawable.blank_field
                            } else if (language == "en") {
                                R.drawable.phone_field
                            } else {
                                R.drawable.ar_phone
                            }
                        ),
                        contentDescription = if (language == "en") "Phone number field" else "حقل رقم الهاتف",
                        modifier = Modifier
                            .size(width = 400.dp, height = 100.dp)
                            .padding(4.dp)
                    )
                    
                    BasicTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        modifier = Modifier
                            .width(380.dp)
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .onFocusChanged { focusState ->
                                isPhoneFieldFocused = focusState.isFocused
                            },
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        cursorBrush = SolidColor(Color.Black)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Add submit button when phone number is entered
                if (phoneNumber.isNotEmpty()) {
                    Button(
                        onClick = {
                            // Save the phone number
                            csvHelper.saveResponses(
                                language = language,
                                name = "",  // These will be updated later with actual values
                                age = "",
                                consent1 = false,
                                consent2 = false,
                                videoFilename = null,
                                phoneNumber = phoneNumber
                            )
                            // Navigate back to start
                            navController.navigate("start") {
                                popUpTo("start") { inclusive = true }
                            }
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(if (language == "en") "Submit" else "إرسال")
                    }
                }
            }
        }
    }
}
 
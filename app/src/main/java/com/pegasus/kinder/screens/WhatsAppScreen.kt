package com.pegasus.kinder.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import com.pegasus.kinder.components.BackButton
import com.pegasus.kinder.navigation.Destination

@Composable
fun WhatsAppScreen(
    navController: NavController,
    language: String
) {
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
                Image(
                    painter = painterResource(
                        id = if (language == "en") {
                            R.drawable.receive_whatsapp
                        } else {
                            R.drawable.ar_receive_whatsapp
                        }
                    ),
                    contentDescription = if (language == "en") 
                        "Receive on WhatsApp" 
                    else 
                        "استلام على واتساب",
                    modifier = Modifier
                        .size(width = 300.dp, height = 100.dp)
                        .clickable {
                            navController.navigate(
                                Destination.PhoneNumber.createPhoneNumberRoute(language)
                            )
                        }
                )
            }
        }
    }
} 
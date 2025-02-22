package com.pegasus.kinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pegasus.kinder.navigation.Destination
import com.pegasus.kinder.screens.*
import com.pegasus.kinder.ui.theme.KinderTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.pegasus.kinder.screens.PhoneNumberScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            KinderTheme {
                var selectedLanguage by remember { mutableStateOf("en") }
                
                val navController = rememberNavController()
                
                NavHost(
                    navController = navController,
                    startDestination = Destination.Start.route
                ) {
                    composable(Destination.Start.route) {
                        StartScreen(navController = navController)
                    }
                    
                    composable(Destination.LanguageSelection.route) {
                        LanguageSelectionScreen(
                            navController = navController,
                            onLanguageSelected = { selectedLanguage = it }
                        )
                    }
                    
                    composable(Destination.Questions.route) {
                        QuestionsScreen(
                            navController = navController,
                            language = selectedLanguage
                        )
                    }
                    
                    composable(
                        route = "user_info/{consent1}/{consent2}/{language}",
                        arguments = listOf(
                            navArgument("consent1") { type = NavType.BoolType },
                            navArgument("consent2") { type = NavType.BoolType },
                            navArgument("language") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val consent1 = backStackEntry.arguments?.getBoolean("consent1") ?: false
                        val consent2 = backStackEntry.arguments?.getBoolean("consent2") ?: false
                        
                        UserInfoScreen(
                            language = selectedLanguage,
                            consent1 = consent1,
                            consent2 = consent2,
                            navController = navController
                        )
                    }
                    
                    composable(
                        route = "recording/{language}/{name}/{age}/{consent1}/{consent2}",
                        arguments = listOf(
                            navArgument("language") { type = NavType.StringType },
                            navArgument("name") { type = NavType.StringType },
                            navArgument("age") { type = NavType.StringType },
                            navArgument("consent1") { type = NavType.BoolType },
                            navArgument("consent2") { type = NavType.BoolType }
                        )
                    ) { backStackEntry ->
                        val language = backStackEntry.arguments?.getString("language") ?: ""
                        val name = backStackEntry.arguments?.getString("name") ?: ""
                        val age = backStackEntry.arguments?.getString("age") ?: ""
                        val consent1 = backStackEntry.arguments?.getBoolean("consent1") ?: false
                        val consent2 = backStackEntry.arguments?.getBoolean("consent2") ?: false

                        RecordingScreen(
                            language = language,
                            navController = navController,
                            name = name,
                            age = age,
                            consent1 = consent1,
                            consent2 = consent2
                        )
                    }

                    composable(
                        route = "phone_number/{language}",
                        arguments = listOf(
                            navArgument("language") { 
                                type = NavType.StringType
                                defaultValue = "en"
                            }
                        )
                    ) { backStackEntry ->
                        val language = backStackEntry.arguments?.getString("language") ?: "en"
                        PhoneNumberScreen(
                            navController = navController,
                            language = language
                        )
                    }

                    composable(
                        route = "whatsapp/{language}",
                        arguments = listOf(
                            navArgument("language") { 
                                type = NavType.StringType
                                defaultValue = "en"
                            }
                        )
                    ) { backStackEntry ->
                        val language = backStackEntry.arguments?.getString("language") ?: "en"
                        WhatsAppScreen(
                            navController = navController,
                            language = language
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KinderTheme {
        Greeting("Android")
    }
}
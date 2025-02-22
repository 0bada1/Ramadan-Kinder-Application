package com.pegasus.kinder.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pegasus.kinder.screens.UserInfoScreen
import com.pegasus.kinder.screens.RecordingScreen
import com.pegasus.kinder.screens.PhoneNumberScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Destination.Start.route) {
        // ... other composables ...

        composable(
            route = "user_info/{consent1}/{consent2}/{language}",
            arguments = listOf(
                navArgument("consent1") { 
                    type = NavType.BoolType
                },
                navArgument("consent2") { 
                    type = NavType.BoolType
                },
                navArgument("language") { 
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments
            Log.d("Navigation", "Raw arguments: $args")
            
            val consent1 = args?.getBoolean("consent1")
            val consent2 = args?.getBoolean("consent2")
            val language = args?.getString("language")
            
            Log.d("Navigation", "Parsed arguments - consent1: $consent1, consent2: $consent2, language: $language")
            
            if (consent1 != null && consent2 != null && language != null) {
                UserInfoScreen(
                    language = language,
                    consent1 = consent1,
                    consent2 = consent2,
                    navController = navController
                )
            } else {
                Log.e("Navigation", "Missing required arguments")
            }
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
            Log.d("Navigation", "PhoneNumber screen - language: $language")
            PhoneNumberScreen(
                navController = navController,
                language = language
            )
        }
    }
} 
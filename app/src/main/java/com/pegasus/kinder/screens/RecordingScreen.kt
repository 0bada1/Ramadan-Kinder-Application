package com.pegasus.kinder.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.pegasus.kinder.components.BackgroundImage
import com.pegasus.kinder.R
import com.pegasus.kinder.utils.GoProHelper
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import androidx.compose.runtime.DisposableEffect
import com.pegasus.kinder.components.HomeButton
import com.pegasus.kinder.utils.CsvHelper
import com.pegasus.kinder.services.GoProService
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Color
import android.util.Log
import com.pegasus.kinder.navigation.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.content.Intent
import android.widget.Toast
import com.pegasus.kinder.components.BackButton

@Composable
fun RecordingScreen(
    language: String,
    navController: NavController,
    name: String,
    age: String,
    consent1: Boolean,
    consent2: Boolean
) {
    val context = LocalContext.current
    val csvHelper = remember { CsvHelper(context) }
    val coroutineScope = rememberCoroutineScope()

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
                // Recording question image
                Image(
                    painter = painterResource(
                        id = if (language == "en") {
                            R.drawable.recording_question
                        } else {
                            R.drawable.ar_recording_question
                        }
                    ),
                    contentDescription = if (language == "en") "Recording Question" else "سؤال التسجيل",
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .size(width = 700.dp, height = 100.dp)
                )

                // Start recording button
                Image(
                    painter = painterResource(
                        id = if (language == "en") {
                            R.drawable.start_recording
                        } else {
                            R.drawable.ar_start_recording
                        }
                    ),
                    contentDescription = if (language == "en") "Start Recording" else "بدء التسجيل",
                    modifier = Modifier
                        .size(width = 300.dp, height = 100.dp)
                        .clickable {
                            // Save data first
                            coroutineScope.launch {
                                csvHelper.saveResponses(
                                    language = language,
                                    name = name,
                                    age = age,
                                    consent1 = consent1,
                                    consent2 = consent2,
                                    videoFilename = null,
                                    phoneNumber = null
                                )
                            }
                            
                            // Launch GoPro app with context and language
                            launchQuikApp(context, language)
                            
                            // Navigate to WhatsApp screen first
                            navController.navigate(
                                Destination.WhatsApp.createWhatsAppRoute(language)
                            )
                        }
                )
            }
        }
    }
}

private fun launchQuikApp(context: android.content.Context, language: String) {
    try {
        val packageManager = context.packageManager
        val packageName = "com.gopro.smarty"

        try {
            // First check if the app is installed
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            Log.d("RecordingScreen", "GoPro app is installed: ${packageInfo.packageName}")
            Log.d("RecordingScreen", "App name: ${packageInfo.applicationInfo.loadLabel(packageManager)}")
            Log.d("RecordingScreen", "Launch activity: ${packageInfo.applicationInfo.targetSdkVersion}")
            
            // Try to get all activities that can be launched
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.setPackage(packageName)
            
            val resolveInfos = packageManager.queryIntentActivities(intent, 0)
            resolveInfos.forEach { resolveInfo ->
                Log.d("RecordingScreen", "Found launchable activity: ${resolveInfo.activityInfo.name}")
            }

            // Try to launch using the main activity
            if (resolveInfos.isNotEmpty()) {
                val mainActivity = resolveInfos[0].activityInfo
                val launchIntent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_LAUNCHER)
                    setClassName(packageName, mainActivity.name)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                Log.d("RecordingScreen", "Launching with intent: $launchIntent")
                context.startActivity(launchIntent)
            } else {
                // Fallback to default launch intent
                val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                if (launchIntent != null) {
                    Log.d("RecordingScreen", "Launching with default intent: $launchIntent")
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(launchIntent)
                } else {
                    Log.e("RecordingScreen", "Could not create launch intent")
                    Toast.makeText(
                        context,
                        if (language == "en") 
                            "Could not launch GoPro app" 
                        else 
                            "لا يمكن تشغيل تطبيق GoPro",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("RecordingScreen", "Package not found: $packageName", e)
            // Open Play Store since app is not installed
            val marketIntent = Intent(Intent.ACTION_VIEW).apply {
                data = android.net.Uri.parse("market://details?id=$packageName")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(marketIntent)
        }
    } catch (e: Exception) {
        Log.e("RecordingScreen", "Error in launchQuikApp", e)
        Toast.makeText(
            context,
            if (language == "en") 
                "Error launching GoPro app" 
            else 
                "خطأ في تشغيل تطبيق GoPro",
            Toast.LENGTH_LONG
        ).show()
    }
} 
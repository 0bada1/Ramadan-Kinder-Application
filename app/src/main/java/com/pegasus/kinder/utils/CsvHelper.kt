package com.pegasus.kinder.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class CsvHelper(private val context: Context) {
    private val fileName = "responses.csv"
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    
    companion object {
        private const val TAG = "CsvHelper"
    }

    fun getFile(): File {
        // Use the app's external files directory
        val directory = context.getExternalFilesDir(null)
            ?: throw IllegalStateException("External storage not available")
        
        Log.d(TAG, "Directory path: ${directory.absolutePath}")
        
        if (!directory.exists()) {
            directory.mkdirs()
            Log.d(TAG, "Created directory: ${directory.absolutePath}")
        }
        
        val file = File(directory, fileName)
        
        if (!file.exists()) {
            try {
                file.createNewFile()
                // Write CSV header
                FileWriter(file).use { writer ->
                    writer.append("Timestamp,Language,Name,Age,Consent1,Consent2,VideoFilename,PhoneNumber\n")
                }
                Log.d(TAG, "Created new file: ${file.absolutePath}")
            } catch (e: Exception) {
                Log.e(TAG, "Error creating file", e)
                throw e
            }
        }
        
        return file
    }

    fun saveResponses(
        language: String,
        name: String,
        age: String,
        consent1: Boolean,
        consent2: Boolean,
        videoFilename: String?,
        phoneNumber: String? = null  // Add phone number parameter with default value
    ) {
        try {
            val file = getFile()
            val timestamp = dateFormat.format(Date())
            
            // Convert boolean to Yes/No and log the values
            Log.d(TAG, "Raw consent values - consent1: $consent1, consent2: $consent2")
            val consent1Text = if (consent1) "Yes" else "No"
            val consent2Text = if (consent2) "Yes" else "No"
            Log.d(TAG, "Converted consent values - consent1: $consent1Text, consent2: $consent2Text")
            
            Log.d(TAG, "Saving data to file: ${file.absolutePath}")
            val data = "$timestamp,$language,\"$name\",$age,$consent1Text,$consent2Text,${videoFilename ?: ""},$phoneNumber\n"
            Log.d(TAG, "Data: $data")
            
            FileWriter(file, true).use { writer ->
                writer.append(data)
            }
            
            Log.d(TAG, "Data saved successfully")
            Log.d(TAG, "File exists: ${file.exists()}, File size: ${file.length()} bytes")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving data", e)
            throw e
        }
    }
} 
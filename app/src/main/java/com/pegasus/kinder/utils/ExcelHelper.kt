package com.pegasus.kinder.utils

import android.content.Context
import android.os.Environment
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ExcelHelper(private val context: Context) {
    private val fileName = "consent_responses.xlsx"
    private val filePath = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
        fileName
    )

    fun saveResponses(
        language: String,
        consent1: Boolean,
        consent2: Boolean,
        name: String,
        age: String
    ) {
        val workbook = if (filePath.exists()) {
            WorkbookFactory.create(FileInputStream(filePath))
        } else {
            XSSFWorkbook().apply {
                createSheet("Responses").apply {
                    createRow(0).apply {
                        createCell(0).setCellValue("Timestamp")
                        createCell(1).setCellValue("Language")
                        createCell(2).setCellValue("Recording Consent")
                        createCell(3).setCellValue("Social Media Consent")
                        createCell(4).setCellValue("Name")
                        createCell(5).setCellValue("Age")
                    }
                }
            }
        }

        val sheet = workbook.getSheet("Responses")
        val newRow = sheet.createRow(sheet.lastRowNum + 1)
        
        // Convert boolean to Yes/No
        val consent1Text = if (consent1) "Yes" else "No"
        val consent2Text = if (consent2) "Yes" else "No"
        
        newRow.apply {
            createCell(0).setCellValue(
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            )
            createCell(1).setCellValue(language)
            createCell(2).setCellValue(consent1Text)
            createCell(3).setCellValue(consent2Text)
            createCell(4).setCellValue(name)
            createCell(5).setCellValue(age)
        }

        FileOutputStream(filePath).use { fos ->
            workbook.write(fos)
        }
        workbook.close()
    }
} 
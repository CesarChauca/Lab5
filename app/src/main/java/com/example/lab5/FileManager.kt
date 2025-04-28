package com.example.lab5

import android.content.Context
import java.io.*

class FileManager(private val context: Context) {

    fun getRecentActivities(days: Int): List<StudyActivity> {
        return context.filesDir.listFiles()?.sortedByDescending { it.lastModified() }
            ?.take(days)
            ?.mapNotNull { file ->
                try {
                    parseActivityFile(file)
                } catch (e: Exception) {
                    null
                }
            } ?: emptyList()
    }

    private fun parseActivityFile(file: File): StudyActivity? {
        return try {
            BufferedReader(InputStreamReader(FileInputStream(file))).use { reader ->
                val content = reader.readText()
                val parts = content.split("|")
                if (parts.size == 4) {
                    StudyActivity(
                        nombre = parts[0],
                        tipo = parts[1],
                        fecha = parts[2],
                        progreso = parts[3].toIntOrNull() ?: 0
                    )
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }
}
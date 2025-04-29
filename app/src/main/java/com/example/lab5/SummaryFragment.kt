package com.example.lab5

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class SummaryFragment : Fragment() {

    private lateinit var totalActivitiesText: TextView
    private lateinit var completedActivitiesText: TextView
    private lateinit var averageProgressText: TextView
    private lateinit var mostUrgentActivityText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_summary, container, false)

        // Vincular TextViews
        totalActivitiesText = view.findViewById(R.id.totalActivitiesText)
        completedActivitiesText = view.findViewById(R.id.completedActivitiesText)
        averageProgressText = view.findViewById(R.id.averageProgressText)
        mostUrgentActivityText = view.findViewById(R.id.mostUrgentActivityText)

        // Obtener últimos 7 archivos
        val lastFiles = getLastFiles(7)

        // Analizar actividades
        val activities = lastFiles.mapNotNull { parseActivity(it) }

        totalActivitiesText.text = activities.size.toString()
        completedActivitiesText.text = activities.count { it.progress == 100 }.toString()

        val averageProgress = if (activities.isNotEmpty()) {
            activities.sumOf { it.progress } / activities.size
        } else 0
        averageProgressText.text = "$averageProgress%"

        val mostUrgent = activities.minByOrNull { it.dueDate }
        mostUrgentActivityText.text = mostUrgent?.name ?: "Ninguna"

        return view
    }

    private fun getLastFiles(count: Int): List<String> {
        val fileList = requireContext().filesDir.listFiles()?.sortedByDescending { it.name }
        return fileList?.take(count)?.map { it.readText() } ?: emptyList()
    }

    private fun parseActivity(jsonString: String): ActivityData? {
        return try {
            val json = JSONObject(jsonString)
            val name = json.getString("name")
            val progress = json.getInt("progress")
            val dueDateStr = json.getString("dueDate")
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dueDate = dateFormat.parse(dueDateStr)
            if (dueDate != null) ActivityData(name, progress, dueDate) else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    data class ActivityData(
        val name: String,
        val progress: Int,
        val dueDate: Date
    )
}
package com.example.lab5

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.lab5.HistoryFragment
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

class SummaryFragment : Fragment() {

    private lateinit var textViewTotal: TextView
    private lateinit var textViewCompleted: TextView
    private lateinit var textViewAverage: TextView
    private lateinit var textViewUrgent: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_summary, container, false)

        textViewTotal = view.findViewById(R.id.textView_total)
        textViewCompleted = view.findViewById(R.id.textView_completed)
        textViewAverage = view.findViewById(R.id.textView_average)
        textViewUrgent = view.findViewById(R.id.textView_urgent)

        calculateSummary()

        return view
    }

    private fun calculateSummary() {
        val activities = readActivitiesFromStorage(requireContext())
            .sortedBy { parseDate(it.fecha) } // Ordenamos por fecha de entrega

        val lastActivities = activities.take(7)

        val total = lastActivities.size
        val completed = lastActivities.count { it.progreso == 100 }
        val averageProgress = if (lastActivities.isNotEmpty()) {
            lastActivities.map { it.progreso }.average()
        } else 0.0

        val urgentActivity = lastActivities.minByOrNull { parseDate(it.fecha) }

        textViewTotal.text = "Total de actividades: $total"
        textViewCompleted.text = "Completadas: $completed"
        textViewAverage.text = "Progreso promedio: ${averageProgress.toInt()}%"
        textViewUrgent.text = "Más urgente: ${urgentActivity?.nombre ?: "Ninguna"}"
    }

    fun readActivitiesFromStorage(context: Context): List<StudyActivity> {
        val activityList = mutableListOf<StudyActivity>()

        val files = context.fileList() // Obtiene todos los nombres de archivo en almacenamiento interno

        for (fileName in files) {
            if (fileName.endsWith(".txt")) {
                try {
                    val fileInput = context.openFileInput(fileName)
                    val reader = BufferedReader(InputStreamReader(fileInput))
                    val lines = reader.readLines()
                    reader.close()

                    // Guardar el archivo en el sgte orden:
                    // Línea 0: nombre
                    // Línea 1: tipo
                    // Línea 2: fecha de entrega
                    // Línea 3: progreso
                    if (lines.size >= 4) {
                        val nombre = lines[0]
                        val tipo = lines[1]
                        val fecha = lines[2]
                        val progreso = lines[3].toIntOrNull() ?: 0

                        val activity = StudyActivity(
                            nombre = nombre,
                            tipo = tipo,
                            fecha = fecha,
                            progreso = progreso
                        )

                        activityList.add(activity)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    //manejo de errores
                }
            }
        }

        return activityList
    }

    private fun parseDate(dateString: String): Date {
        return try {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateString) ?: Date()
        } catch (e: Exception) {
            Date() // Si falla, devuelve fecha actual
        }
    }
}
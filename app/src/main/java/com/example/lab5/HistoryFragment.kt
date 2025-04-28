package com.example.lab5

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.InputStreamReader

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        recyclerView = view.findViewById(R.id.recyclerView_history)

        // Leer actividades desde almacenamiento
        val activities = readActivitiesFromStorage(requireContext())

        adapter = HistoryAdapter(activities)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        return view
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

}

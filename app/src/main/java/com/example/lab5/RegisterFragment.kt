package com.example.lab5

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RegisterFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var typeSpinner: Spinner
    private lateinit var dateButton: Button
    private lateinit var dateTextView: TextView
    private lateinit var progressSeekBar: SeekBar
    private lateinit var saveButton: Button
    private lateinit var rootView: ConstraintLayout

    private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        rootView = view.findViewById(R.id.rootView_register)

        nameEditText = view.findViewById(R.id.editText_name)
        typeSpinner = view.findViewById(R.id.spinner_type)
        dateButton = view.findViewById(R.id.button_select_date)
        dateTextView = view.findViewById(R.id.textView_date)
        progressSeekBar = view.findViewById(R.id.seekBar_progress)
        saveButton = view.findViewById(R.id.button_save_activity)

        setupSpinner()
        setupDatePicker()
        setupSaveButton()

        return view
    }

    private fun setupSpinner() {
        val types = arrayOf("Tarea", "Práctica", "Examen")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = adapter
    }

    private fun setupDatePicker() {
        dateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                dateTextView.text = selectedDate
            }, year, month, day)

            datePickerDialog.show()
        }
    }

    private fun setupSaveButton() {
        saveButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val type = typeSpinner.selectedItem.toString()
            val progress = progressSeekBar.progress

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedDate.isEmpty()) {
                Toast.makeText(requireContext(), "Debes seleccionar una fecha de entrega", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveActivity(name, type, selectedDate, progress)
            Toast.makeText(requireContext(), "Actividad guardada con éxito", Toast.LENGTH_SHORT).show()

            clearForm()
        }
    }

    private fun saveActivity(name: String, type: String, date: String, progress: Int) {
        try {
            val safeName = name.replace(" ", "_")
            val safeDate = date.replace("/", "_")
            val fileName = "${safeName}_${safeDate}.txt"
            val fileContent = "$name\n$type\n$date\n$progress"
            val file = File(requireContext().filesDir, fileName)
            file.writeText(fileContent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error al guardar la actividad", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearForm() {
        nameEditText.setText("")
        typeSpinner.setSelection(0)
        dateTextView.text = "Fecha no seleccionada"
        progressSeekBar.progress = 0
        selectedDate = ""
    }
}

package com.example.lab5

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.AdapterView // Agrega esta importación
import androidx.core.content.ContextCompat
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class RegisterFragment : Fragment() {

    private lateinit var emotionSpinner: Spinner
    private lateinit var noteEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        rootView = view

        emotionSpinner = view.findViewById(R.id.spinner_emotions)
        noteEditText = view.findViewById(R.id.editText_note)
        saveButton = view.findViewById(R.id.btn_save)

        val emotions = arrayOf("Feliz", "Triste", "Enojado", "Sorpresa", "Neutral")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, emotions)
        emotionSpinner.adapter = adapter


        emotionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                changeBackgroundColor()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        saveButton.setOnClickListener {
            saveEmotion()
        }

        return view
    }

    private fun changeBackgroundColor() {
        val selectedEmotion = emotionSpinner.selectedItem.toString()

        when (selectedEmotion) {
            "Feliz" -> rootView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_feliz))
            "Triste" -> rootView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_triste))
            "Enojado" -> rootView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_enojado))
            "Sorpresa" -> rootView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_sorpresa))
            "Neutral" -> rootView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_neutral))
        }
    }

    private fun saveEmotion() {
        val emotion = emotionSpinner.selectedItem.toString()
        val note = noteEditText.text.toString()

        if (emotion.isNotEmpty() && note.isNotEmpty()) {
            val date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
            val filename = "$date.txt"
            val fileContent = "Emoción: $emotion\nNota: $note"

            context?.openFileOutput(filename, Context.MODE_PRIVATE)?.use {
                it.write(fileContent.toByteArray())
            }

            Toast.makeText(context, "Datos guardados con éxito", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}

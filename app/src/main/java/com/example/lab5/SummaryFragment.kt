package com.example.lab5

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.io.File

class SummaryFragment : Fragment() {

    private lateinit var summaryText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_summary, container, false)
        //summaryText = view.findViewById(R.id.text_summary)

        val lastFiles = getLastFiles(7)

        val emotionCounts = lastFiles.groupingBy { it }.eachCount()
        val mostFrequentEmotion = emotionCounts.maxByOrNull { it.value }?.key ?: "Ninguna"

        summaryText.text = "Emoción más frecuente: $mostFrequentEmotion"

        return view
    }

    private fun getLastFiles(count: Int): List<String> {
        val fileList = requireContext().filesDir.listFiles()?.sortedByDescending { it.name }
        return fileList?.take(count)?.map { it.readText() } ?: emptyList()
    }
}
package com.example.lab5

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = view.findViewById(R.id.recyclerView_history)

        val lastFiles = getLastFiles(7)

        adapter = HistoryAdapter(lastFiles)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        return view
    }
    private fun getLastFiles(count: Int): List<String> {

        val fileList = requireContext().filesDir.listFiles()?.sortedByDescending { it.lastModified() }

        return fileList?.take(count)?.map { it.name } ?: emptyList()
    }
}

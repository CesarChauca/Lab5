package com.example.lab5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private val activityList: List<StudyActivity>) :
    RecyclerView.Adapter<HistoryAdapter.StudyActivityViewHolder>() {

    class StudyActivityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.cardItem)
        val textViewIcon: TextView = view.findViewById(R.id.textView_icono)
        val textViewName: TextView = view.findViewById(R.id.textView_nombre)
        val textViewType: TextView = view.findViewById(R.id.textView_tipo)
        val textViewDate: TextView = view.findViewById(R.id.textView_fecha)
        val textViewProgress: TextView = view.findViewById(R.id.textView_progreso)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false)
        return StudyActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudyActivityViewHolder, position: Int) {
        val activity = activityList[position]

        // Asignar los valores
        holder.textViewName.text = activity.nombre
        holder.textViewType.text = activity.tipo
        holder.textViewDate.text = activity.fecha
        holder.textViewProgress.text = "Progreso: ${activity.progreso}%"

        // Asignar el icono según tipo
        holder.textViewIcon.text = when (activity.tipo) {
            "Tarea" -> "📚"
            "Práctica" -> "📝"
            "Examen" -> "🧪"
            else -> "📚"
        }

        // Cambiar el color de fondo según progreso
        val context = holder.cardView.context
        when {
            activity.progreso < 50 -> holder.cardView.setCardBackgroundColor(context.getColor(android.R.color.holo_red_light))
            activity.progreso in 50..90 -> holder.cardView.setCardBackgroundColor(context.getColor(android.R.color.holo_orange_light))
            activity.progreso == 100 -> holder.cardView.setCardBackgroundColor(context.getColor(android.R.color.holo_green_light))
        }
    }

    override fun getItemCount(): Int {
        return activityList.size
    }
}
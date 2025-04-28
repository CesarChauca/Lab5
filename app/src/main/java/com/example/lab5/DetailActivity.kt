package com.example.lab5

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Aquí puedes obtener los extras del intent
        val activityName = intent.getStringExtra("ACTIVITY_NAME") ?: ""
        val activityDate = intent.getStringExtra("ACTIVITY_DATE") ?: ""

        // Usa estos datos para mostrar la información detallada
    }
}
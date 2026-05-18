package com.example.medbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var doctorAdapter: DoctorAdapter

    private val doctorList = listOf(

        Doctor(
            "Dr. Sarah Johnson",
            "Cardiologist",
            "⭐ 4.9",
            "10 years experience"
        ),

        Doctor(
            "Dr. Michael Smith",
            "Dentist",
            "⭐ 4.8",
            "7 years experience"
        ),

        Doctor(
            "Dr. Emily Brown",
            "Pediatrician",
            "⭐ 4.7",
            "5 years experience"
        ),

        Doctor(
            "Dr. David Wilson",
            "Neurologist",
            "⭐ 4.9",
            "12 years experience"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        recyclerView =
            findViewById(R.id.recyclerViewDoctors)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        doctorAdapter =
            DoctorAdapter(doctorList)

        recyclerView.adapter =
            doctorAdapter
    }
}
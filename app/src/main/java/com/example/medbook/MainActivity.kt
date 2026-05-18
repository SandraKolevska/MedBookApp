package com.example.medbook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var doctorAdapter: DoctorAdapter
    private lateinit var myAppointmentsBtn: Button

    private val doctorList = listOf(

        Doctor(
            "Dr. Sarah Johnson",
            "Cardiologist",
            "⭐ 4.9",
            "10 years experience",
            R.drawable.doctor2
        ),

        Doctor(
            "Dr. Michael Smith",
            "Dentist",
            "⭐ 4.8",
            "7 years experience",
            R.drawable.doctor3
        ),

        Doctor(
            "Dr. Emily Brown",
            "Pediatrician",
            "⭐ 4.7",
            "5 years experience",
            R.drawable.doctor4
        ),

        Doctor(
            "Dr. David Wilson",
            "Neurologist",
            "⭐ 4.9",
            "12 years experience",
            R.drawable.doctor5
        ),

        Doctor(
            "Dr. Olivia Taylor",
            "Dermatologist",
            "⭐ 4.8",
            "8 years experience",
            R.drawable.doctor6
        ),

        Doctor(
            "Dr. James Anderson",
            "Orthopedic",
            "⭐ 4.7",
            "9 years experience",
            R.drawable.doctor7
        ),

        Doctor(
            "Dr. Sophia Martinez",
            "Gynecologist",
            "⭐ 4.9",
            "11 years experience",
            R.drawable.doctor8
        ),

        Doctor(
            "Dr. Daniel Thomas",
            "Psychiatrist",
            "⭐ 4.6",
            "6 years experience",
            R.drawable.doctor9
        ),

        Doctor(
            "Dr. Isabella White",
            "Ophthalmologist",
            "⭐ 4.8",
            "10 years experience",
            R.drawable.doctor10
        ),

        Doctor(
            "Dr. William Harris",
            "ENT Specialist",
            "⭐ 4.7",
            "7 years experience",
            R.drawable.doctor11
        ),

        Doctor(
            "Dr. Mia Clark",
            "Radiologist",
            "⭐ 4.9",
            "13 years experience",
            R.drawable.doctor12
        ),

        Doctor(
            "Dr. Benjamin Lewis",
            "General Surgeon",
            "⭐ 5.0",
            "15 years experience",
            R.drawable.doctor14
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        myAppointmentsBtn =
            findViewById(R.id.myAppointmentsBtn)

        myAppointmentsBtn.setOnClickListener {

            startActivity(
                Intent(this, MyAppointmentsActivity::class.java)
            )
        }

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
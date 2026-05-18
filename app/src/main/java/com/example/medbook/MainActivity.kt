package com.example.medbook

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var doctorAdapter: DoctorAdapter
    private lateinit var myAppointmentsBtn: Button
    private lateinit var searchDoctorsInput: EditText

    private lateinit var allDoctorsBtn: Button
    private lateinit var cardiologistBtn: Button
    private lateinit var dentistBtn: Button
    private lateinit var neurologistBtn: Button
    private lateinit var pediatricianBtn: Button
    private lateinit var dermatologistBtn: Button
    private lateinit var orthopedicBtn: Button
    private lateinit var gynecologistBtn: Button
    private lateinit var psychiatristBtn: Button
    private lateinit var ophthalmologistBtn: Button
    private lateinit var entBtn: Button
    private lateinit var radiologistBtn: Button
    private lateinit var surgeonBtn: Button

    private var filteredDoctorList =
        mutableListOf<Doctor>()

    private val doctorList = mutableListOf(

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
            "Dr. Christopher Taylor",
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
            "Dr. Alexander Martinez",
            "Gynecologist",
            "⭐ 4.9",
            "11 years experience",
            R.drawable.doctor8
        ),

        Doctor(
            "Dr. Emma Thomas",
            "Psychiatrist",
            "⭐ 4.6",
            "6 years experience",
            R.drawable.doctor9
        ),

        Doctor(
            "Dr. Robert White",
            "Ophthalmologist",
            "⭐ 4.8",
            "10 years experience",
            R.drawable.doctor10
        ),

        Doctor(
            "Dr. Sophia Harris",
            "ENT Specialist",
            "⭐ 4.7",
            "7 years experience",
            R.drawable.doctor11
        ),

        Doctor(
            "Dr. David Clark",
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

        searchDoctorsInput =
            findViewById(R.id.searchDoctorsInput)

        allDoctorsBtn =
            findViewById(R.id.allDoctorsBtn)

        cardiologistBtn =
            findViewById(R.id.cardiologistBtn)

        dentistBtn =
            findViewById(R.id.dentistBtn)

        neurologistBtn =
            findViewById(R.id.neurologistBtn)

        pediatricianBtn =
            findViewById(R.id.pediatricianBtn)

        dermatologistBtn =
            findViewById(R.id.dermatologistBtn)

        orthopedicBtn =
            findViewById(R.id.orthopedicBtn)

        gynecologistBtn =
            findViewById(R.id.gynecologistBtn)

        psychiatristBtn =
            findViewById(R.id.psychiatristBtn)

        ophthalmologistBtn =
            findViewById(R.id.ophthalmologistBtn)

        entBtn =
            findViewById(R.id.entBtn)

        radiologistBtn =
            findViewById(R.id.radiologistBtn)

        surgeonBtn =
            findViewById(R.id.surgeonBtn)

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

        filteredDoctorList.addAll(doctorList)

        recyclerView.adapter =
            doctorAdapter

        // SEARCH SYSTEM
        searchDoctorsInput.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    val searchText =
                        s.toString().lowercase()

                    val filteredList =
                        filteredDoctorList.filter {

                            it.name.lowercase()
                                .contains(searchText)

                                    ||

                                    it.specialization.lowercase()
                                        .contains(searchText)
                        }

                    doctorAdapter =
                        DoctorAdapter(filteredList)

                    recyclerView.adapter =
                        doctorAdapter
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )

        // CATEGORY FILTERS

        allDoctorsBtn.setOnClickListener {

            doctorAdapter =
                DoctorAdapter(doctorList)

            recyclerView.adapter =
                doctorAdapter
        }

        cardiologistBtn.setOnClickListener {

            val filtered =
                doctorList.filter {

                    it.specialization.contains(
                        "Cardiologist"
                    )
                }

            doctorAdapter =
                DoctorAdapter(filtered)

            recyclerView.adapter =
                doctorAdapter
        }

        dentistBtn.setOnClickListener {

            val filtered =
                doctorList.filter {

                    it.specialization.contains(
                        "Dentist"
                    )
                }

            doctorAdapter =
                DoctorAdapter(filtered)

            recyclerView.adapter =
                doctorAdapter
        }

        neurologistBtn.setOnClickListener {

            val filtered =
                doctorList.filter {

                    it.specialization.contains(
                        "Neurologist"
                    )
                }

            doctorAdapter =
                DoctorAdapter(filtered)

            recyclerView.adapter =
                doctorAdapter
        }

        pediatricianBtn.setOnClickListener {

            val filtered =
                doctorList.filter {

                    it.specialization.contains(
                        "Pediatrician"
                    )
                }

            doctorAdapter =
                DoctorAdapter(filtered)

            recyclerView.adapter =
                doctorAdapter
        }

        dermatologistBtn.setOnClickListener {

            val filtered =
                doctorList.filter {

                    it.specialization.contains(
                        "Dermatologist"
                    )
                }

            doctorAdapter =
                DoctorAdapter(filtered)

            recyclerView.adapter =
                doctorAdapter
        }

        orthopedicBtn.setOnClickListener {

            val filtered =
                doctorList.filter {

                    it.specialization.contains(
                        "Orthopedic"
                    )
                }

            doctorAdapter =
                DoctorAdapter(filtered)

            recyclerView.adapter =
                doctorAdapter
        }

        gynecologistBtn.setOnClickListener {

            val filtered =
                doctorList.filter {

                    it.specialization.contains(
                        "Gynecologist"
                    )
                }

            doctorAdapter =
                DoctorAdapter(filtered)

            recyclerView.adapter =
                doctorAdapter
        }

        psychiatristBtn.setOnClickListener {

            val filtered =
                doctorList.filter {

                    it.specialization.contains(
                        "Psychiatrist"
                    )
                }

            doctorAdapter =
                DoctorAdapter(filtered)

            recyclerView.adapter =
                doctorAdapter
        }

        ophthalmologistBtn.setOnClickListener {

            val filtered =
                doctorList.filter {

                    it.specialization.contains(
                        "Ophthalmologist"
                    )
                }

            doctorAdapter =
                DoctorAdapter(filtered)

            recyclerView.adapter =
                doctorAdapter
        }

        entBtn.setOnClickListener {

            val filtered =
                doctorList.filter {

                    it.specialization.contains(
                        "ENT"
                    )
                }

            doctorAdapter =
                DoctorAdapter(filtered)

            recyclerView.adapter =
                doctorAdapter
        }

        radiologistBtn.setOnClickListener {

            val filtered =
                doctorList.filter {

                    it.specialization.contains(
                        "Radiologist"
                    )
                }

            doctorAdapter =
                DoctorAdapter(filtered)

            recyclerView.adapter =
                doctorAdapter
        }

        surgeonBtn.setOnClickListener {

            val filtered =
                doctorList.filter {

                    it.specialization.contains(
                        "Surgeon"
                    )
                }

            doctorAdapter =
                DoctorAdapter(filtered)

            recyclerView.adapter =
                doctorAdapter
        }
    }
}
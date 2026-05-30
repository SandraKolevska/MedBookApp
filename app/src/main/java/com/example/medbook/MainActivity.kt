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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var doctorAdapter: DoctorAdapter
    private lateinit var searchDoctorsInput: EditText

    private lateinit var auth: FirebaseAuth

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

    private lateinit var bottomNavigationView:
            BottomNavigationView

    private var filteredDoctorList =
        mutableListOf<Doctor>()

    private val doctorList = mutableListOf(

        Doctor(
            "Dr. Sarah Johnson",
            "Cardiologist",
            "⭐ 4.9",
            "10 years experience",
            R.drawable.doctor2,

            "$50",

            "Experienced cardiologist specialized in heart disease prevention, ECG diagnostics and patient care.",

            listOf(
                "08:00 AM",
                "08:30 AM",
                "09:00 AM",
                "09:30 AM",
                "10:00 AM",
                "10:30 AM",
                "11:00 AM",
                "11:30 AM",
                "12:00 PM",
                "12:30 PM",
                "01:00 PM",
                "01:30 PM",
                "02:00 PM",
                "02:30 PM",
                "03:00 PM",
                "03:30 PM"
            ),

            listOf(
                Calendar.SUNDAY
            )
        ),

        Doctor(
            "Dr. Michael Smith",
            "Dentist",
            "⭐ 4.8",
            "7 years experience",
            R.drawable.doctor3,

            "$35",

            "Professional dentist with extensive experience in cosmetic dentistry and oral health treatments.",

            listOf(
                "10:00 AM",
                "10:30 AM",
                "11:00 AM",
                "11:30 AM",
                "12:00 PM",
                "12:30 PM",
                "01:00 PM",
                "01:30 PM",
                "02:00 PM",
                "02:30 PM",
                "03:00 PM",
                "03:30 PM",
                "04:00 PM",
                "04:30 PM",
                "05:00 PM",
                "05:30 PM"
            ),

            listOf(
                Calendar.WEDNESDAY,
                Calendar.SUNDAY
            )
        ),

        Doctor(
            "Dr. Emily Brown",
            "Pediatrician",
            "⭐ 4.7",
            "5 years experience",
            R.drawable.doctor4,

            "$30",

            "Friendly pediatrician dedicated to children's health, preventive care and family consultations.",

            listOf(
                "09:00 AM",
                "09:30 AM",
                "10:00 AM",
                "10:30 AM",
                "11:00 AM",
                "11:30 AM",
                "12:00 PM",
                "12:30 PM",
                "01:00 PM",
                "01:30 PM"
            ),

            listOf(
                Calendar.SATURDAY,
                Calendar.SUNDAY
            )
        ),

        Doctor(
            "Dr. David Wilson",
            "Neurologist",
            "⭐ 4.9",
            "12 years experience",
            R.drawable.doctor5,

            "$70",

            "Highly experienced neurologist focused on brain disorders, migraines and neurological diagnostics.",

            listOf(
                "11:00 AM",
                "11:30 AM",
                "12:00 PM",
                "12:30 PM",
                "01:00 PM",
                "01:30 PM",
                "02:00 PM",
                "02:30 PM",
                "03:00 PM",
                "03:30 PM",
                "04:00 PM",
                "04:30 PM"
            ),

            listOf(
                Calendar.SUNDAY
            )
        ),

        Doctor(
            "Dr. Christopher Taylor",
            "Dermatologist",
            "⭐ 4.8",
            "8 years experience",
            R.drawable.doctor6,

            "$40",

            "Dermatology specialist experienced in skin care treatments, acne therapy and cosmetic procedures.",

            listOf(
                "08:00 AM",
                "08:30 AM",
                "09:00 AM",
                "09:30 AM",
                "10:00 AM",
                "10:30 AM",
                "11:00 AM",
                "11:30 AM"
            ),

            listOf(
                Calendar.MONDAY
            )
        ),

        Doctor(
            "Dr. James Anderson",
            "Orthopedic",
            "⭐ 4.7",
            "9 years experience",
            R.drawable.doctor7,

            "$55",

            "Orthopedic specialist helping patients recover from injuries, fractures and joint problems.",

            listOf(
                "01:00 PM",
                "01:30 PM",
                "02:00 PM",
                "02:30 PM",
                "03:00 PM",
                "03:30 PM",
                "04:00 PM",
                "04:30 PM",
                "05:00 PM"
            ),

            listOf(
                Calendar.FRIDAY
            )
        ),

        Doctor(
            "Dr. Alexander Martinez",
            "Gynecologist",
            "⭐ 4.9",
            "11 years experience",
            R.drawable.doctor8,

            "$45",

            "Experienced gynecologist focused on women's health, pregnancy care and preventive examinations.",

            listOf(
                "09:00 AM",
                "09:30 AM",
                "10:00 AM",
                "10:30 PM",
                "11:00 AM",
                "11:30 AM",
                "12:00 PM",
                "12:30 PM"
            ),

            listOf(
                Calendar.SUNDAY
            )
        ),

        Doctor(
            "Dr. Emma Thomas",
            "Psychiatrist",
            "⭐ 4.6",
            "6 years experience",
            R.drawable.doctor9,

            "$60",

            "Compassionate psychiatrist helping patients with anxiety, stress management and mental wellness.",

            listOf(
                "02:00 PM",
                "02:30 PM",
                "03:00 PM",
                "03:30 PM",
                "04:00 PM",
                "04:30 PM",
                "05:00 PM"
            ),

            listOf(
                Calendar.THURSDAY
            )
        ),

        Doctor(
            "Dr. Robert White",
            "Ophthalmologist",
            "⭐ 4.8",
            "10 years experience",
            R.drawable.doctor10,

            "$45",

            "Eye specialist providing vision care, diagnostics and treatment for various eye conditions.",

            listOf(
                "08:00 AM",
                "08:30 AM",
                "09:00 AM",
                "09:30 AM",
                "10:00 AM",
                "10:30 AM",
                "11:00 AM"
            ),

            listOf(
                Calendar.SUNDAY
            )
        ),

        Doctor(
            "Dr. Sophia Harris",
            "ENT Specialist",
            "⭐ 4.7",
            "7 years experience",
            R.drawable.doctor11,

            "$40",

            "ENT specialist treating ear, nose and throat conditions with modern medical approaches.",

            listOf(
                "12:00 PM",
                "12:30 PM",
                "01:00 PM",
                "01:30 PM",
                "02:00 PM",
                "02:30 PM",
                "03:00 PM"
            ),

            listOf(
                Calendar.TUESDAY
            )
        ),

        Doctor(
            "Dr. David Clark",
            "Radiologist",
            "⭐ 4.9",
            "13 years experience",
            R.drawable.doctor12,

            "$65",

            "Expert radiologist specialized in MRI, CT scan diagnostics and medical imaging interpretation.",

            listOf(
                "10:00 AM",
                "10:30 AM",
                "11:00 AM",
                "11:30 AM",
                "12:00 PM",
                "12:30 PM",
                "01:00 PM"
            ),

            listOf(
                Calendar.SATURDAY
            )
        ),

        Doctor(
            "Dr. Benjamin Lewis",
            "General Surgeon",
            "⭐ 5.0",
            "15 years experience",
            R.drawable.doctor14,

            "$80",

            "Highly skilled surgeon with extensive experience in complex surgeries and patient recovery care.",

            listOf(
                "07:00 AM",
                "07:30 AM",
                "08:00 AM",
                "08:30 AM",
                "09:00 AM",
                "09:30 AM",
                "10:00 AM",
                "10:30 AM",
                "11:00 AM",
                "11:30 AM"
            ),

            listOf(
                Calendar.FRIDAY,
                Calendar.SUNDAY
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

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

        bottomNavigationView =
            findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_home -> {

                    true
                }

                R.id.nav_appointments -> {

                    if (auth.currentUser != null) {

                        startActivity(
                            Intent(
                                this,
                                AppointmentsActivity::class.java
                            )
                        )
                    }

                    true
                }

                R.id.nav_favorites -> {

                    if (auth.currentUser != null) {

                        startActivity(
                            Intent(
                                this,
                                FavoritesActivity::class.java
                            )
                        )
                    }

                    true
                }

                R.id.nav_profile -> {

                    if (auth.currentUser != null) {

                        startActivity(
                            Intent(
                                this,
                                ProfileActivity::class.java
                            )
                        )
                    }

                    true
                }

                else -> false
            }
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

        allDoctorsBtn.setOnClickListener {

            doctorAdapter =
                DoctorAdapter(doctorList)

            recyclerView.adapter =
                doctorAdapter
        }

        cardiologistBtn.setOnClickListener {
            filterDoctors("Cardiologist")
        }

        dentistBtn.setOnClickListener {
            filterDoctors("Dentist")
        }

        neurologistBtn.setOnClickListener {
            filterDoctors("Neurologist")
        }

        pediatricianBtn.setOnClickListener {
            filterDoctors("Pediatrician")
        }

        dermatologistBtn.setOnClickListener {
            filterDoctors("Dermatologist")
        }

        orthopedicBtn.setOnClickListener {
            filterDoctors("Orthopedic")
        }

        gynecologistBtn.setOnClickListener {
            filterDoctors("Gynecologist")
        }

        psychiatristBtn.setOnClickListener {
            filterDoctors("Psychiatrist")
        }

        ophthalmologistBtn.setOnClickListener {
            filterDoctors("Ophthalmologist")
        }

        entBtn.setOnClickListener {
            filterDoctors("ENT Specialist")
        }

        radiologistBtn.setOnClickListener {
            filterDoctors("Radiologist")
        }

        surgeonBtn.setOnClickListener {
            filterDoctors("General Surgeon")
        }
    }

    private fun filterDoctors(
        specialization: String
    ) {

        val filteredList =
            doctorList.filter {

                it.specialization ==
                        specialization
            }

        doctorAdapter =
            DoctorAdapter(filteredList)

        recyclerView.adapter =
            doctorAdapter
    }
}

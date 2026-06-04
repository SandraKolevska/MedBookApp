package com.example.medbook

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.TextView

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recyclerView:
            RecyclerView

    private lateinit var doctorAdapter:
            DoctorAdapter

    private lateinit var firestore:
            FirebaseFirestore

    private lateinit var auth:
            FirebaseAuth

    private val favoriteDoctors =
        mutableListOf<Doctor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_favorites
        )

        val backBtn =
            findViewById<TextView>(R.id.backBtn)

        backBtn.setOnClickListener {
            finish()
        }

        supportActionBar?.title =
            getString(R.string.my_favorites)

        supportActionBar?.setDisplayHomeAsUpEnabled(
            true
        )

        firestore =
            FirebaseFirestore.getInstance()

        auth =
            FirebaseAuth.getInstance()

        recyclerView =
            findViewById(R.id.favoritesRecyclerView)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        doctorAdapter =
            DoctorAdapter(favoriteDoctors)

        recyclerView.adapter =
            doctorAdapter

        loadFavorites()
    }

    private fun loadFavorites() {

        if (auth.currentUser == null) {

            Toast.makeText(
                this,
                getString(R.string.please_login_first),
                Toast.LENGTH_LONG
            ).show()

            return
        }

        firestore.collection("favorites")
            .whereEqualTo(
                "userId",
                auth.currentUser!!.uid
            )
            .get()

            .addOnSuccessListener { documents ->

                favoriteDoctors.clear()

                for (document in documents) {

                    val doctorName =
                        document.getString(
                            "doctorName"
                        ) ?: ""

                    val doctor =
                        when (doctorName) {

                            "Dr. Sarah Johnson" -> Doctor(
                                "Dr. Sarah Johnson",
                                "Cardiologist",
                                "⭐ 4.9",
                                "10 years experience",
                                R.drawable.doctor2,

                                "$50",

                                "Experienced cardiologist specialized in heart disease prevention, ECG diagnostics and patient care.",

                                listOf(),
                                listOf()
                            )

                            "Dr. Michael Smith" -> Doctor(
                                "Dr. Michael Smith",
                                "Dentist",
                                "⭐ 4.8",
                                "7 years experience",
                                R.drawable.doctor3,

                                "$35",

                                "Professional dentist with extensive experience in cosmetic dentistry and oral health treatments.",

                                listOf(),
                                listOf()
                            )

                            "Dr. Emily Brown" -> Doctor(
                                "Dr. Emily Brown",
                                "Pediatrician",
                                "⭐ 4.7",
                                "5 years experience",
                                R.drawable.doctor4,

                                "$30",

                                "Friendly pediatrician dedicated to children's health, preventive care and family consultations.",

                                listOf(),
                                listOf()
                            )

                            else -> null
                        }

                    if (doctor != null) {

                        favoriteDoctors.add(
                            doctor
                        )
                    }
                }

                doctorAdapter.notifyDataSetChanged()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
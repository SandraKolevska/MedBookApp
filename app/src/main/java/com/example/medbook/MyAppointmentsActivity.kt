package com.example.medbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class MyAppointmentsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var appointmentAdapter: AppointmentAdapter

    private lateinit var firestore: FirebaseFirestore

    private val appointmentList = mutableListOf<Appointment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.activity_my_appointments)

        firestore = FirebaseFirestore.getInstance()

        recyclerView =
            findViewById(R.id.appointmentsRecyclerView)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        appointmentAdapter =
            AppointmentAdapter(appointmentList)

        recyclerView.adapter =
            appointmentAdapter

        loadAppointments()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun loadAppointments() {

        firestore.collection("appointments")
            .get()
            .addOnSuccessListener { result ->

                appointmentList.clear()

                for (document in result) {

                    val appointment =
                        document.toObject(Appointment::class.java)

                    appointmentList.add(appointment)
                }

                appointmentAdapter.notifyDataSetChanged()
            }
    }
}
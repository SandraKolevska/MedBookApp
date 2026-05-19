package com.example.medbook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()

        // BLOCK GUEST USERS
        if (auth.currentUser == null) {

            startActivity(
                Intent(this, LoginActivity::class.java)
            )

            finish()

            return
        }

        setContentView(R.layout.activity_profile)

        firestore = FirebaseFirestore.getInstance()

        val userEmailText =
            findViewById<TextView>(R.id.userEmailText)

        val totalAppointmentsText =
            findViewById<TextView>(R.id.totalAppointmentsText)

        val appointmentsContainer =
            findViewById<LinearLayout>(R.id.appointmentsContainer)

        val logoutBtn =
            findViewById<Button>(R.id.logoutBtn)

        val currentUser =
            auth.currentUser

        userEmailText.text =
            currentUser?.email

        firestore.collection("appointments")
            .whereEqualTo(
                "userId",
                currentUser?.uid
            )
            .get()

            .addOnSuccessListener { documents ->

                totalAppointmentsText.text =
                    "Total Appointments: ${documents.size()}"

                appointmentsContainer.removeAllViews()

                for (document in documents) {

                    val doctorName =
                        document.getString("doctorName")

                    val date =
                        document.getString("date")

                    val slot =
                        document.getString("slot")

                    val appointmentText =
                        TextView(this)

                    appointmentText.text =

                        "Doctor: $doctorName\n" +
                                "Date: $date\n" +
                                "Time: $slot"

                    appointmentText.textSize = 18f

                    appointmentText.setPadding(
                        0,
                        0,
                        0,
                        40
                    )

                    appointmentsContainer.addView(
                        appointmentText
                    )
                }
            }

        logoutBtn.setOnClickListener {

            auth.signOut()

            startActivity(
                Intent(this, LoginActivity::class.java)
            )

            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
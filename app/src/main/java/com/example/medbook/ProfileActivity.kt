package com.example.medbook

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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

        val favoriteDoctorsContainer =
            findViewById<LinearLayout>(R.id.favoriteDoctorsContainer)

        val logoutBtn =
            findViewById<Button>(R.id.logoutBtn)

        val currentUser =
            auth.currentUser

        userEmailText.text =
            currentUser?.email

        // LOAD APPOINTMENTS
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

                    val cardLayout =
                        LinearLayout(this)

                    cardLayout.orientation =
                        LinearLayout.VERTICAL

                    cardLayout.setPadding(
                        40,
                        40,
                        40,
                        40
                    )

                    cardLayout.setBackgroundColor(
                        Color.WHITE
                    )

                    val cardParams =
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                    cardParams.setMargins(
                        0,
                        0,
                        0,
                        32
                    )

                    cardLayout.layoutParams =
                        cardParams

                    val doctorText =
                        TextView(this)

                    doctorText.text =
                        doctorName

                    doctorText.textSize = 22f

                    doctorText.setTypeface(
                        null,
                        android.graphics.Typeface.BOLD
                    )

                    val dateText =
                        TextView(this)

                    dateText.text =
                        "Date: $date"

                    dateText.textSize = 18f

                    dateText.setPadding(
                        0,
                        16,
                        0,
                        0
                    )

                    val slotText =
                        TextView(this)

                    slotText.text =
                        "Time: $slot"

                    slotText.textSize = 18f

                    slotText.setPadding(
                        0,
                        8,
                        0,
                        24
                    )

                    val cancelBtn =
                        Button(this)

                    cancelBtn.text =
                        "Cancel Appointment"

                    cancelBtn.gravity =
                        Gravity.CENTER

                    cancelBtn.setOnClickListener {

                        firestore.collection("appointments")
                            .document(document.id)
                            .delete()

                            .addOnSuccessListener {

                                Toast.makeText(
                                    this,
                                    "Appointment canceled",
                                    Toast.LENGTH_SHORT
                                ).show()

                                recreate()
                            }
                    }

                    cardLayout.addView(
                        doctorText
                    )

                    cardLayout.addView(
                        dateText
                    )

                    cardLayout.addView(
                        slotText
                    )

                    cardLayout.addView(
                        cancelBtn
                    )

                    appointmentsContainer.addView(
                        cardLayout
                    )
                }
            }

        // LOAD FAVORITES
        firestore.collection("favorites")
            .whereEqualTo(
                "userId",
                currentUser?.uid
            )
            .get()

            .addOnSuccessListener { documents ->

                favoriteDoctorsContainer.removeAllViews()

                for (document in documents) {

                    val doctorName =
                        document.getString("doctorName")

                    val favoriteCard =
                        LinearLayout(this)

                    favoriteCard.orientation =
                        LinearLayout.VERTICAL

                    favoriteCard.setPadding(
                        40,
                        40,
                        40,
                        40
                    )

                    favoriteCard.setBackgroundColor(
                        Color.WHITE
                    )

                    val params =
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                    params.setMargins(
                        0,
                        0,
                        0,
                        24
                    )

                    favoriteCard.layoutParams =
                        params

                    val favoriteDoctorText =
                        TextView(this)

                    favoriteDoctorText.text =
                        "❤️ $doctorName"

                    favoriteDoctorText.textSize = 20f

                    favoriteDoctorText.setTypeface(
                        null,
                        android.graphics.Typeface.BOLD
                    )

                    favoriteCard.addView(
                        favoriteDoctorText
                    )

                    favoriteDoctorsContainer.addView(
                        favoriteCard
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
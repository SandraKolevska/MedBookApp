package com.example.medbook

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AppointmentsActivity : AppCompatActivity() {

    private lateinit var firestore:
            FirebaseFirestore

    private lateinit var auth:
            FirebaseAuth

    private lateinit var appointmentsContainer:
            LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_appointments
        )

        supportActionBar?.title =
            "Appointments"

        firestore =
            FirebaseFirestore.getInstance()

        auth =
            FirebaseAuth.getInstance()

        appointmentsContainer =
            findViewById(R.id.appointmentsContainer)

        loadAppointments()
    }

    private fun loadAppointments() {

        appointmentsContainer.removeAllViews()

        firestore.collection("appointments")
            .whereEqualTo(
                "userId",
                auth.currentUser?.uid
            )
            .get()

            .addOnSuccessListener { documents ->

                if (documents.isEmpty) {

                    val emptyText =
                        TextView(this)

                    emptyText.text =
                        "No appointments yet"

                    emptyText.textSize = 18f

                    appointmentsContainer.addView(
                        emptyText
                    )

                    return@addOnSuccessListener
                }

                for (document in documents) {

                    val doctorName =
                        document.getString(
                            "doctorName"
                        )

                    val specialization =
                        document.getString(
                            "specialization"
                        )

                    val date =
                        document.getString(
                            "date"
                        )

                    val slot =
                        document.getString(
                            "slot"
                        )

                    val appointmentCard =
                        LinearLayout(this)

                    appointmentCard.orientation =
                        LinearLayout.VERTICAL

                    appointmentCard.setPadding(
                        40,
                        40,
                        40,
                        40
                    )

                    appointmentCard.setBackgroundColor(
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
                        30
                    )

                    appointmentCard.layoutParams =
                        params

                    val doctorText =
                        TextView(this)

                    doctorText.text =
                        doctorName

                    doctorText.textSize = 22f

                    doctorText.setTypeface(
                        null,
                        android.graphics.Typeface.BOLD
                    )

                    val specializationText =
                        TextView(this)

                    specializationText.text =
                        specialization

                    specializationText.textSize = 18f

                    specializationText.setTextColor(
                        Color.GRAY
                    )

                    val dateText =
                        TextView(this)

                    dateText.text =
                        "📅 $date"

                    dateText.textSize = 18f

                    dateText.setPadding(
                        0,
                        20,
                        0,
                        0
                    )

                    val slotText =
                        TextView(this)

                    slotText.text =
                        "🕒 $slot"

                    slotText.textSize = 18f

                    val cancelBtn =
                        Button(this)

                    cancelBtn.text =
                        "Cancel Appointment"

                    cancelBtn.setPadding(
                        0,
                        20,
                        0,
                        0
                    )

                    cancelBtn.setOnClickListener {

                        AlertDialog.Builder(this)
                            .setTitle(
                                "Cancel Appointment"
                            )

                            .setMessage(
                                "Are you sure?"
                            )

                            .setPositiveButton(
                                "Yes"
                            ) { _, _ ->

                                firestore.collection(
                                    "appointments"
                                )
                                    .document(document.id)
                                    .delete()

                                    .addOnSuccessListener {

                                        Toast.makeText(
                                            this,
                                            "Appointment cancelled",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        loadAppointments()
                                    }
                            }

                            .setNegativeButton(
                                "No",
                                null
                            )

                            .show()
                    }

                    appointmentCard.addView(
                        doctorText
                    )

                    appointmentCard.addView(
                        specializationText
                    )

                    appointmentCard.addView(
                        dateText
                    )

                    appointmentCard.addView(
                        slotText
                    )

                    appointmentCard.addView(
                        cancelBtn
                    )

                    appointmentsContainer.addView(
                        appointmentCard
                    )
                }
            }
    }
}
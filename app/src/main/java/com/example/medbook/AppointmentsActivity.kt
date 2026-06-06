package com.example.medbook

import android.content.Intent
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppointmentsActivity : AppCompatActivity() {

    private lateinit var firestore:
            FirebaseFirestore

    private lateinit var auth:
            FirebaseAuth

    private lateinit var appointmentsContainer:
            LinearLayout

    private fun translateSpecialization(
        specialization: String?
    ): String {

        val isMk =
            resources.configuration
                .locales[0]
                .language == "mk"

        if (!isMk) {
            return specialization ?: ""
        }

        return when (specialization) {

            "Cardiologist" -> "Кардиолог"
            "Dentist" -> "Стоматолог"
            "Pediatrician" -> "Педијатар"
            "Neurologist" -> "Невролог"
            "Dermatologist" -> "Дерматолог"
            "Orthopedic" -> "Ортопед"
            "Gynecologist" -> "Гинеколог"
            "Psychiatrist" -> "Психијатар"
            "Ophthalmologist" -> "Офталмолог"
            "ENT Specialist" -> "ОРЛ Специјалист"
            "Radiologist" -> "Радиолог"
            "General Surgeon" -> "Општ Хирург"

            else -> specialization ?: ""
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_appointments
        )

        val backBtn =
            findViewById<TextView>(R.id.backBtn)

        backBtn.setOnClickListener {
            finish()
        }

        supportActionBar?.title =
            getString(R.string.appointments_title)

        supportActionBar?.setDisplayHomeAsUpEnabled(
            true
        )

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
                        getString(R.string.no_appointments)

                    emptyText.textSize = 18f

                    appointmentsContainer.addView(
                        emptyText
                    )

                    return@addOnSuccessListener
                }

                for (document in documents) {

                    val date =
                        document.getString(
                            "date"
                        ) ?: continue

                    val formatter =
                        SimpleDateFormat(
                            "d/M/yyyy",
                            Locale.getDefault()
                        )

                    val appointmentDate =
                        formatter.parse(date)

                    val today =
                        formatter.parse(
                            formatter.format(Date())
                        )

                    if (
                        appointmentDate == null ||
                        appointmentDate.before(today)
                    ) {
                        continue
                    }

                    val doctorName =
                        document.getString(
                            "doctorName"
                        )

                    val specialization =
                        document.getString(
                            "specialization"
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

                    appointmentCard.setBackgroundResource(
                        R.drawable.appointment_card
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
                    appointmentCard.elevation = 10f

                    val doctorText =
                        TextView(this)

                    doctorText.text =
                        doctorName

                    doctorText.textSize = 24f

                    doctorText.setTypeface(
                        null,
                        android.graphics.Typeface.BOLD
                    )

                    val specializationText =
                        TextView(this)

                    specializationText.text =
                        translateSpecialization(
                            specialization
                        )

                    specializationText.textSize = 18f

                    specializationText.setTextColor(
                        Color.parseColor("#64748B")
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

                    val joinBtn =
                        Button(this)

                    joinBtn.text =
                        getString(R.string.join_consultation)

                    joinBtn.setBackgroundResource(
                        R.drawable.rounded_button
                    )

                    joinBtn.setTextColor(
                        Color.WHITE
                    )

                    joinBtn.setOnClickListener {

                        val intent =
                            Intent(
                                this,
                                ConsultationActivity::class.java
                            )

                        intent.putExtra(
                            "doctorName",
                            doctorName
                        )

                        intent.putExtra(
                            "doctorSpecialization",
                            specialization
                        )

                        intent.putExtra(
                            "doctorImage",
                            getDoctorImage(
                                doctorName
                            )
                        )

                        startActivity(intent)
                    }

                    val cancelBtn =
                        Button(this)

                    cancelBtn.text =
                        getString(R.string.cancel_appointment)

                    cancelBtn.setBackgroundResource(
                        R.drawable.rounded_input
                    )

                    cancelBtn.setTextColor(
                        Color.parseColor("#14B8A6")
                    )

                    cancelBtn.setPadding(
                        0,
                        20,
                        0,
                        0
                    )

                    cancelBtn.setOnClickListener {

                        AlertDialog.Builder(this)
                            .setTitle(
                                getString(R.string.cancel_appointment_title)
                            )

                            .setMessage(
                                getString(R.string.confirm_cancel)
                            )

                            .setPositiveButton(
                                getString(R.string.yes)
                            ) { _, _ ->

                                firestore.collection(
                                    "appointments"
                                )
                                    .document(document.id)
                                    .delete()

                                    .addOnSuccessListener {

                                        Toast.makeText(
                                            this,
                                            getString(R.string.appointment_cancelled),
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        loadAppointments()
                                    }
                            }

                            .setNegativeButton(
                                getString(R.string.no),
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

                    val joinParams =
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                    joinParams.setMargins(
                        0,
                        20,
                        0,
                        12
                    )

                    joinBtn.layoutParams = joinParams

                    appointmentCard.addView(
                        joinBtn
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
    private fun getDoctorImage(
        doctorName: String?
    ): Int {

        return when (doctorName) {

            "Dr. Sarah Johnson" ->
                R.drawable.doctor2

            "Dr. Michael Smith" ->
                R.drawable.doctor3

            "Dr. Emily Brown" ->
                R.drawable.doctor4

            "Dr. David Wilson" ->
                R.drawable.doctor5

            "Dr. Christopher Taylor" ->
                R.drawable.doctor6

            "Dr. James Anderson" ->
                R.drawable.doctor7

            "Dr. Alexander Martinez" ->
                R.drawable.doctor8

            "Dr. Emma Thomas" ->
                R.drawable.doctor9

            "Dr. Robert White" ->
                R.drawable.doctor10

            "Dr. Sophia Harris" ->
                R.drawable.doctor11

            "Dr. David Clark" ->
                R.drawable.doctor12

            "Dr. Benjamin Lewis" ->
                R.drawable.doctor14

            else ->
                R.drawable.doctor2
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
package com.example.medbook

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class DoctorDetailsActivity : AppCompatActivity() {

    private var selectedSlot = ""
    private var selectedDate = ""

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.activity_doctor_details)

        firestore = FirebaseFirestore.getInstance()

        val doctorImage =
            findViewById<ImageView>(R.id.detailsDoctorImage)

        val doctorName =
            findViewById<TextView>(R.id.detailsDoctorName)

        val doctorSpecialization =
            findViewById<TextView>(R.id.detailsDoctorSpecialization)

        val doctorRating =
            findViewById<TextView>(R.id.detailsDoctorRating)

        val doctorExperience =
            findViewById<TextView>(R.id.detailsDoctorExperience)

        val doctorFee =
            findViewById<TextView>(R.id.detailsDoctorFee)

        val selectDateBtn =
            findViewById<Button>(R.id.selectDateBtn)

        val selectedDateText =
            findViewById<TextView>(R.id.selectedDateText)

        val slotsContainer =
            findViewById<GridLayout>(R.id.slotsContainer)

        val bookAppointmentBtn =
            findViewById<Button>(R.id.bookAppointmentBtn)

        val name =
            intent.getStringExtra("doctorName")

        val specialization =
            intent.getStringExtra("doctorSpecialization")

        val rating =
            intent.getStringExtra("doctorRating")

        val experience =
            intent.getStringExtra("doctorExperience")

        val image =
            intent.getIntExtra("doctorImage", 0)

        doctorName.text = name
        doctorSpecialization.text = specialization
        doctorRating.text = rating
        doctorExperience.text = experience

        doctorFee.text = "$25"

        doctorImage.setImageResource(image)

        // CALENDAR DATE PICKER
        selectDateBtn.setOnClickListener {

            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->

                    selectedDate =
                        "$selectedDay/${selectedMonth + 1}/$selectedYear"

                    selectedDateText.text =
                        "Selected Date: $selectedDate"

                    loadSlots(
                        slotsContainer,
                        name
                    )
                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }

        // BOOK APPOINTMENT
        bookAppointmentBtn.setOnClickListener {

            if (
                selectedDate.isNotEmpty() &&
                selectedSlot.isNotEmpty()
            ) {

                val appointment = hashMapOf(

                    "doctorName" to name,
                    "specialization" to specialization,
                    "date" to selectedDate,
                    "slot" to selectedSlot
                )

                firestore.collection("appointments")
                    .add(appointment)

                    .addOnSuccessListener {

                        Toast.makeText(
                            this,
                            "Appointment saved successfully",
                            Toast.LENGTH_LONG
                        ).show()

                        loadSlots(
                            slotsContainer,
                            name
                        )
                    }

                    .addOnFailureListener {

                        Toast.makeText(
                            this,
                            "Failed to save appointment",
                            Toast.LENGTH_LONG
                        ).show()
                    }

            } else {

                Toast.makeText(
                    this,
                    "Please select date and time",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun loadSlots(
        slotsContainer: GridLayout,
        doctorName: String?
    ) {

        slotsContainer.removeAllViews()

        val slotTimes = listOf(
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
        )

        for (slot in slotTimes) {

            val slotButton = Button(this)

            slotButton.text = slot

            val params = GridLayout.LayoutParams()

            params.width =
                GridLayout.LayoutParams.WRAP_CONTENT

            params.height =
                GridLayout.LayoutParams.WRAP_CONTENT

            params.columnSpec =
                GridLayout.spec(GridLayout.UNDEFINED)

            params.setMargins(
                8,
                8,
                8,
                8
            )

            slotButton.layoutParams = params

            slotButton.textSize = 14f

            slotButton.setPadding(
                10,
                10,
                10,
                10
            )

            firestore.collection("appointments")
                .whereEqualTo("doctorName", doctorName)
                .whereEqualTo("date", selectedDate)
                .whereEqualTo("slot", slot)
                .get()

                .addOnSuccessListener { documents ->

                    if (!documents.isEmpty) {

                        slotButton.isEnabled = false

                        slotButton.setBackgroundColor(
                            Color.LTGRAY
                        )

                        slotButton.setTextColor(
                            Color.DKGRAY
                        )
                    }
                }

            slotButton.setOnClickListener {

                if (slotButton.isEnabled) {

                    selectedSlot = slot

                    Toast.makeText(
                        this,
                        "Selected: $slot",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            slotsContainer.addView(slotButton)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
package com.example.medbook

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DoctorDetailsActivity : AppCompatActivity() {

    private var selectedSlot = ""
    private var selectedDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_doctor_details)

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

        // DATE BUTTONS
        val date1Btn =
            findViewById<Button>(R.id.date1Btn)

        val date2Btn =
            findViewById<Button>(R.id.date2Btn)

        val date3Btn =
            findViewById<Button>(R.id.date3Btn)

        val date4Btn =
            findViewById<Button>(R.id.date4Btn)

        // SLOT BUTTONS
        val slot1Btn =
            findViewById<Button>(R.id.slot1Btn)

        val slot2Btn =
            findViewById<Button>(R.id.slot2Btn)

        val slot3Btn =
            findViewById<Button>(R.id.slot3Btn)

        val slot4Btn =
            findViewById<Button>(R.id.slot4Btn)

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

        // DATE SELECTION
        date1Btn.setOnClickListener {

            selectedDate = "May 18"

            resetDateButtons(
                date1Btn,
                date2Btn,
                date3Btn,
                date4Btn
            )

            date1Btn.setBackgroundColor(
                Color.parseColor("#4CAF50")
            )
        }

        date2Btn.setOnClickListener {

            selectedDate = "May 19"

            resetDateButtons(
                date1Btn,
                date2Btn,
                date3Btn,
                date4Btn
            )

            date2Btn.setBackgroundColor(
                Color.parseColor("#4CAF50")
            )
        }

        date3Btn.setOnClickListener {

            selectedDate = "May 20"

            resetDateButtons(
                date1Btn,
                date2Btn,
                date3Btn,
                date4Btn
            )

            date3Btn.setBackgroundColor(
                Color.parseColor("#4CAF50")
            )
        }

        date4Btn.setOnClickListener {

            selectedDate = "May 21"

            resetDateButtons(
                date1Btn,
                date2Btn,
                date3Btn,
                date4Btn
            )

            date4Btn.setBackgroundColor(
                Color.parseColor("#4CAF50")
            )
        }

        // SLOT SELECTION
        slot1Btn.setOnClickListener {

            selectedSlot = "09:00 AM"

            resetSlotButtons(
                slot1Btn,
                slot2Btn,
                slot3Btn,
                slot4Btn
            )

            slot1Btn.setBackgroundColor(
                Color.parseColor("#2196F3")
            )
        }

        slot2Btn.setOnClickListener {

            selectedSlot = "10:00 AM"

            resetSlotButtons(
                slot1Btn,
                slot2Btn,
                slot3Btn,
                slot4Btn
            )

            slot2Btn.setBackgroundColor(
                Color.parseColor("#2196F3")
            )
        }

        slot3Btn.setOnClickListener {

            selectedSlot = "11:00 AM"

            resetSlotButtons(
                slot1Btn,
                slot2Btn,
                slot3Btn,
                slot4Btn
            )

            slot3Btn.setBackgroundColor(
                Color.parseColor("#2196F3")
            )
        }

        slot4Btn.setOnClickListener {

            selectedSlot = "12:00 PM"

            resetSlotButtons(
                slot1Btn,
                slot2Btn,
                slot3Btn,
                slot4Btn
            )

            slot4Btn.setBackgroundColor(
                Color.parseColor("#2196F3")
            )
        }

        // BOOK APPOINTMENT
        bookAppointmentBtn.setOnClickListener {

            if (
                selectedDate.isNotEmpty() &&
                selectedSlot.isNotEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Appointment booked on $selectedDate at $selectedSlot",
                    Toast.LENGTH_LONG
                ).show()

            } else {

                Toast.makeText(
                    this,
                    "Please select date and time",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun resetDateButtons(
        btn1: Button,
        btn2: Button,
        btn3: Button,
        btn4: Button
    ) {

        btn1.setBackgroundColor(Color.LTGRAY)
        btn2.setBackgroundColor(Color.LTGRAY)
        btn3.setBackgroundColor(Color.LTGRAY)
        btn4.setBackgroundColor(Color.LTGRAY)
    }

    private fun resetSlotButtons(
        btn1: Button,
        btn2: Button,
        btn3: Button,
        btn4: Button
    ) {

        btn1.setBackgroundColor(Color.LTGRAY)
        btn2.setBackgroundColor(Color.LTGRAY)
        btn3.setBackgroundColor(Color.LTGRAY)
        btn4.setBackgroundColor(Color.LTGRAY)
    }
}
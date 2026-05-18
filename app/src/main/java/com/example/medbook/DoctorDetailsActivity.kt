package com.example.medbook

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DoctorDetailsActivity : AppCompatActivity() {

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

        doctorImage.setImageResource(image)
    }
}
package com.example.medbook

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView

class ConsultationActivity : AppCompatActivity() {

    private lateinit var timerText: TextView

    private var seconds = 0

    private var isMicOn = true

    private var isCameraOn = true

    private val handler = Handler(
        Looper.getMainLooper()
    )

    private val timerRunnable =
        object : Runnable {

            override fun run() {

                val minutes =
                    seconds / 60

                val remainingSeconds =
                    seconds % 60

                timerText.text =
                    String.format(
                        "%02d:%02d",
                        minutes,
                        remainingSeconds
                    )

                seconds++

                handler.postDelayed(
                    this,
                    1000
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_consultation
        )

        val backBtn =
            findViewById<TextView>(R.id.backBtn)

        backBtn.setOnClickListener {
            finish()
        }

        timerText =
            findViewById(R.id.callTimer)

        val doctorNameText =
            findViewById<TextView>(
                R.id.doctorNameText
            )
        val doctorImageView =
            findViewById<ImageView>(
                R.id.doctorVideoImage
            )

        val doctorSpecializationText =
            findViewById<TextView>(
                R.id.doctorSpecializationText
            )

        val micBtn =
            findViewById<Button>(
                R.id.micBtn
            )

        val cameraBtn =
            findViewById<Button>(
                R.id.cameraBtn
            )

        val endCallBtn =
            findViewById<Button>(
                R.id.endCallBtn
            )

        val doctorName =
            intent.getStringExtra(
                "doctorName"
            )

        val doctorSpecialization =
            intent.getStringExtra(
                "doctorSpecialization"
            )
        val doctorImage =
            intent.getIntExtra(
                "doctorImage",
                R.drawable.doctor2
            )

        doctorNameText.text =
            doctorName

        doctorSpecializationText.text =
            translateSpecialization(
                doctorSpecialization
            )

        doctorImageView.setImageResource(
            doctorImage
        )

        micBtn.setOnClickListener {

            isMicOn = !isMicOn

            if (isMicOn) {

                micBtn.text =
                    getString(
                        R.string.mic
                    )

            } else {

                micBtn.text =
                    getString(
                        R.string.muted
                    )
            }
        }

        cameraBtn.setOnClickListener {

            isCameraOn = !isCameraOn

            if (isCameraOn) {

                cameraBtn.text =
                    getString(
                        R.string.camera
                    )

            } else {

                cameraBtn.text =
                    getString(
                        R.string.camera_off
                    )
            }
        }

        endCallBtn.setOnClickListener {

            finish()
        }

        handler.post(
            timerRunnable
        )
    }
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
    override fun onDestroy() {
        super.onDestroy()

        handler.removeCallbacks(
            timerRunnable
        )
    }

}
package com.example.medbook

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

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

        supportActionBar?.title =
            "Online Consultation"

        supportActionBar?.setDisplayHomeAsUpEnabled(
            true
        )

        timerText =
            findViewById(R.id.callTimer)

        val doctorNameText =
            findViewById<TextView>(
                R.id.doctorNameText
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

        doctorNameText.text =
            "Dr. Sarah Johnson"

        doctorSpecializationText.text =
            "Cardiologist"

        micBtn.setOnClickListener {

            isMicOn = !isMicOn

            if (isMicOn) {

                micBtn.text =
                    "🎤 Mic"

            } else {

                micBtn.text =
                    "🔇 Muted"
            }
        }

        cameraBtn.setOnClickListener {

            isCameraOn = !isCameraOn

            if (isCameraOn) {

                cameraBtn.text =
                    "📷 Camera"

            } else {

                cameraBtn.text =
                    "🚫 Camera Off"
            }
        }

        endCallBtn.setOnClickListener {

            finish()
        }

        handler.post(
            timerRunnable
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        handler.removeCallbacks(
            timerRunnable
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
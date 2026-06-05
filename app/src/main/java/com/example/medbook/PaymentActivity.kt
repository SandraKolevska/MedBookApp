package com.example.medbook

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_payment
        )

        val doctorText =
            findViewById<TextView>(
                R.id.doctorText
            )

        val dateText =
            findViewById<TextView>(
                R.id.dateText
            )

        val timeText =
            findViewById<TextView>(
                R.id.timeText
            )

        val priceText =
            findViewById<TextView>(
                R.id.priceText
            )

        val cardHolderInput =
            findViewById<EditText>(
                R.id.cardHolderInput
            )

        val cardNumberInput =
            findViewById<EditText>(
                R.id.cardNumberInput
            )

        val expiryInput =
            findViewById<EditText>(
                R.id.expiryInput
            )

        val cvvInput =
            findViewById<EditText>(
                R.id.cvvInput
            )

        val payBtn =
            findViewById<Button>(
                R.id.payBtn
            )

        val doctorName =
            intent.getStringExtra(
                "doctorName"
            )

        val appointmentDate =
            intent.getStringExtra(
                "appointmentDate"
            )

        val appointmentSlot =
            intent.getStringExtra(
                "appointmentSlot"
            )

        val doctorPrice =
            intent.getStringExtra(
                "doctorPrice"
            )

        val doctorSpecialization =
            intent.getStringExtra(
                "doctorSpecialization"
            )

        doctorText.text =
            "${getString(R.string.doctor_label)}: $doctorName"

        dateText.text =
            "${getString(R.string.date_label)}: $appointmentDate"

        timeText.text =
            "${getString(R.string.time_label)}: $appointmentSlot"

        priceText.text =
            "${getString(R.string.price_label)}: $doctorPrice"

        val backBtn =
            findViewById<TextView>(R.id.backBtn)

        backBtn.setOnClickListener {
            finish()
        }

        payBtn.setOnClickListener {

            val cardHolder =
                cardHolderInput.text.toString().trim()

            val cardNumber =
                cardNumberInput.text.toString().trim()

            val expiry =
                expiryInput.text.toString().trim()

            val cvv =
                cvvInput.text.toString().trim()

            if (
                cardHolder.isEmpty() ||
                cardNumber.isEmpty() ||
                expiry.isEmpty() ||
                cvv.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    getString(
                        R.string.fill_all_fields
                    ),
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (
                cardNumber.length != 16
            ) {

                Toast.makeText(
                    this,
                    getString(
                        R.string.invalid_card_number
                    ),
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }
            if (
                cvv.length != 3
            ) {

                Toast.makeText(
                    this,
                    getString(
                        R.string.invalid_cvv
                    ),
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }
            if (
                !expiry.matches(
                    Regex("\\d{2}/\\d{2}")
                )
            ) {

                Toast.makeText(
                    this,
                    getString(
                        R.string.invalid_expiry
                    ),
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }
            AlertDialog.Builder(this)

                .setTitle(
                    getString(
                        R.string.payment_successful
                    )
                )

                .setMessage(
                    getString(
                        R.string.appointment_booked_successfully
                    )
                )

                .setPositiveButton(
                    "OK"
                ) { dialog, _ ->

                    val appointment =
                        hashMapOf(

                            "doctorName" to doctorName,

                            "specialization" to doctorSpecialization,

                            "date" to appointmentDate,

                            "slot" to appointmentSlot,

                            "userId" to FirebaseAuth
                                .getInstance()
                                .currentUser
                                ?.uid
                        )

                    FirebaseFirestore
                        .getInstance()
                        .collection("appointments")
                        .add(appointment)

                    showAppointmentNotification(
                        doctorName ?: "Doctor"
                    )

                    dialog.dismiss()

                    startActivity(
                        Intent(
                            this,
                            AppointmentsActivity::class.java
                        )
                    )

                    finish()
                }

                .show()
        }
    }
    private fun showAppointmentNotification(
        doctorName: String
    ) {

        val channelId = "medbook_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId,
                getString(
                    R.string.medbook_notifications
                ),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager =
                getSystemService(
                    NotificationManager::class.java
                )

            manager.createNotificationChannel(
                channel
            )
        }

        val builder =
            NotificationCompat.Builder(
                this,
                channelId
            )

                .setSmallIcon(
                    android.R.drawable.ic_dialog_info
                )

                .setContentTitle(
                    getString(R.string.appointment_confirmed)
                )

                .setContentText(
                    getString(
                        R.string.appointment_notification_text,
                        doctorName
                    )
                )

                .setPriority(
                    NotificationCompat.PRIORITY_DEFAULT
                )

        val notificationManager =
            getSystemService(
                NotificationManager::class.java
            )

        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            builder.build()
        )
    }
}
package com.example.medbook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PaymentActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_payment)

        firestore = FirebaseFirestore.getInstance()

        auth = FirebaseAuth.getInstance()

        val cardNameInput =
            findViewById<EditText>(R.id.cardNameInput)

        val cardNumberInput =
            findViewById<EditText>(R.id.cardNumberInput)

        val expiryInput =
            findViewById<EditText>(R.id.expiryInput)

        val cvvInput =
            findViewById<EditText>(R.id.cvvInput)

        val payBtn =
            findViewById<Button>(R.id.payBtn)

        val doctorName =
            intent.getStringExtra("doctorName")

        val specialization =
            intent.getStringExtra("specialization")

        val date =
            intent.getStringExtra("date")

        val slot =
            intent.getStringExtra("slot")

        payBtn.setOnClickListener {

            val cardName =
                cardNameInput.text.toString()

            val cardNumber =
                cardNumberInput.text.toString()

            val expiry =
                expiryInput.text.toString()

            val cvv =
                cvvInput.text.toString()

            if (
                cardName.isEmpty() ||
                cardNumber.isEmpty() ||
                expiry.isEmpty() ||
                cvv.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                val appointment = hashMapOf(

                    "doctorName" to doctorName,

                    "specialization" to specialization,

                    "date" to date,

                    "slot" to slot,

                    "userId" to auth.currentUser!!.uid
                )

                firestore.collection("appointments")
                    .add(appointment)

                    .addOnSuccessListener {

                        val notification = Notification(

                            auth.currentUser!!.uid,

                            "🔔 Appointment booked with $doctorName"
                        )

                        firestore.collection("notifications")
                            .add(notification)

                        Toast.makeText(
                            this,
                            "Payment Successful & Appointment Booked",
                            Toast.LENGTH_LONG
                        ).show()

                        startActivity(
                            Intent(this, ProfileActivity::class.java)
                        )

                        finish()
                    }

                    .addOnFailureListener {

                        Toast.makeText(
                            this,
                            "Failed to save appointment",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
        }
    }
}
package com.example.medbook

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
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

        val favoriteDoctorsContainer =
            findViewById<LinearLayout>(R.id.favoriteDoctorsContainer)

        val notificationsContainer =
            findViewById<LinearLayout>(R.id.notificationsContainer)

        val logoutBtn =
            findViewById<Button>(R.id.logoutBtn)

        val currentUser =
            auth.currentUser

        userEmailText.text =
            currentUser?.email

        // TOTAL APPOINTMENTS COUNT
        firestore.collection("appointments")
            .whereEqualTo(
                "userId",
                currentUser?.uid
            )
            .get()

            .addOnSuccessListener { documents ->

                totalAppointmentsText.text =
                    "Total Appointments: ${documents.size()}"
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

        // LOAD NOTIFICATIONS
        firestore.collection("notifications")
            .whereEqualTo(
                "userId",
                currentUser?.uid
            )
            .get()

            .addOnSuccessListener { documents ->

                notificationsContainer.removeAllViews()

                for (document in documents) {

                    val message =
                        document.getString("message")

                    val notificationCard =
                        LinearLayout(this)

                    notificationCard.orientation =
                        LinearLayout.VERTICAL

                    notificationCard.setPadding(
                        32,
                        32,
                        32,
                        32
                    )

                    notificationCard.setBackgroundColor(
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

                    notificationCard.layoutParams =
                        params

                    val messageText =
                        TextView(this)

                    messageText.text =
                        message

                    messageText.textSize = 18f

                    notificationCard.addView(
                        messageText
                    )

                    notificationsContainer.addView(
                        notificationCard
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
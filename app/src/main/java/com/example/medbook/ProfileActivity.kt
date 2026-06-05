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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

        val backBtn = findViewById<TextView>(R.id.backBtn)

        backBtn.setOnClickListener {
            finish()
        }

        firestore = FirebaseFirestore.getInstance()

        val userEmailText =
            findViewById<TextView>(R.id.userEmailText)

        val totalAppointmentsText =
            findViewById<TextView>(R.id.totalAppointmentsText)

        val favoriteDoctorsContainer =
            findViewById<LinearLayout>(R.id.favoriteDoctorsContainer)

        val notificationsContainer =
            findViewById<LinearLayout>(R.id.notificationsContainer)

        val historyTitle =
            findViewById<TextView>(R.id.historyTitle)

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
                    getString(
                        R.string.total_appointments_count,
                        documents.size()
                    )
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

        // LOAD APPOINTMENT HISTORY

        firestore.collection("appointments")
            .whereEqualTo(
                "userId",
                currentUser?.uid
            )
            .get()

            .addOnSuccessListener { documents ->

                notificationsContainer.removeAllViews()

                var hasHistory = false

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
                        !appointmentDate.before(today)
                    ) {
                        continue
                    }

                    val doctorName =
                        document.getString("doctorName")



                    val slot =
                        document.getString("slot")

                    val historyCard =
                        LinearLayout(this)

                    historyCard.orientation =
                        LinearLayout.VERTICAL

                    historyCard.setPadding(
                        40,
                        40,
                        40,
                        40
                    )

                    historyCard.setBackgroundColor(
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

                    historyCard.layoutParams =
                        params

                    val doctorText =
                        TextView(this)

                    doctorText.text =
                        doctorName

                    doctorText.textSize = 20f

                    doctorText.setTypeface(
                        null,
                        android.graphics.Typeface.BOLD
                    )

                    val dateText =
                        TextView(this)

                    dateText.text =
                        "📅 $date"

                    dateText.textSize = 18f

                    val slotText =
                        TextView(this)

                    slotText.text =
                        "🕒 $slot"

                    slotText.textSize = 18f

                    historyCard.addView(
                        doctorText
                    )

                    historyCard.addView(
                        dateText
                    )

                    historyCard.addView(
                        slotText
                    )

                    notificationsContainer.addView(
                        historyCard
                    )
                    hasHistory = true
                }
                if (!hasHistory) {

                    val emptyText =
                        TextView(this)

                    emptyText.text =
                        getString(
                            R.string.no_appointment_history
                        )

                    emptyText.textSize = 18f

                    notificationsContainer.addView(
                        emptyText
                    )
                }
            }

        var historyExpanded = false

        historyTitle.setOnClickListener {

            historyExpanded = !historyExpanded

            if (historyExpanded) {

                notificationsContainer.visibility =
                    android.view.View.VISIBLE

                historyTitle.text =
                    getString(
                        R.string.appointment_history_open
                    )

            } else {

                notificationsContainer.visibility =
                    android.view.View.GONE

                historyTitle.text =
                    getString(
                        R.string.appointment_history_closed
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
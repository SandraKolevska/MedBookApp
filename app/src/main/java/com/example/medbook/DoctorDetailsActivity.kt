package com.example.medbook

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.Calendar
import java.util.GregorianCalendar

class DoctorDetailsActivity : AppCompatActivity() {

    private var selectedSlot = ""
    private var selectedDate = ""

    private lateinit var firestore: FirebaseFirestore

    private lateinit var auth: FirebaseAuth

    private lateinit var doctorSlots: ArrayList<String>

    private lateinit var unavailableDays: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.activity_doctor_details)

        firestore = FirebaseFirestore.getInstance()

        auth = FirebaseAuth.getInstance()

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

        val doctorDescription =
            findViewById<TextView>(R.id.detailsDoctorDescription)

        val selectDateBtn =
            findViewById<Button>(R.id.selectDateBtn)

        val selectedDateText =
            findViewById<TextView>(R.id.selectedDateText)

        val slotsContainer =
            findViewById<GridLayout>(R.id.slotsContainer)

        val bookAppointmentBtn =
            findViewById<Button>(R.id.bookAppointmentBtn)

        val ratingBar =
            findViewById<RatingBar>(R.id.ratingBar)

        val reviewInput =
            findViewById<EditText>(R.id.reviewInput)

        val submitReviewBtn =
            findViewById<Button>(R.id.submitReviewBtn)

        val reviewsContainer =
            findViewById<LinearLayout>(R.id.reviewsContainer)

        val favoriteBtn =
            findViewById<Button>(R.id.favoriteBtn)

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

        val fee =
            intent.getStringExtra("doctorPrice")

        val description =
            intent.getStringExtra("doctorDescription")

        doctorSlots =
            intent.getStringArrayListExtra("doctorSlots")
                ?: arrayListOf()

        unavailableDays =
            intent.getIntegerArrayListExtra(
                "doctorUnavailableDays"
            ) ?: arrayListOf()

        doctorName.text = name
        doctorSpecialization.text = specialization
        doctorRating.text = rating
        doctorExperience.text = experience

        doctorFee.text = fee

        doctorDescription.text =
            description

        doctorImage.setImageResource(image)

        checkFavoriteStatus(
            name,
            favoriteBtn
        )

        loadReviews(
            name,
            reviewsContainer
        )

        favoriteBtn.setOnClickListener {

            if (
                auth.currentUser == null ||
                auth.currentUser!!.isAnonymous
            ) {

                Toast.makeText(
                    this,
                    "Please login with an account",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            firestore.collection("favorites")
                .whereEqualTo(
                    "doctorName",
                    name
                )
                .whereEqualTo(
                    "userId",
                    auth.currentUser?.uid
                )
                .get()

                .addOnSuccessListener { documents ->

                    if (documents.isEmpty) {

                        val favorite = hashMapOf(

                            "doctorName" to name,

                            "userId" to auth.currentUser?.uid
                        )

                        firestore.collection("favorites")
                            .add(favorite)

                            .addOnSuccessListener {

                                favoriteBtn.text =
                                    "❤️ Remove Favorite"

                                Toast.makeText(
                                    this,
                                    "Added to favorites",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                    } else {

                        for (document in documents) {

                            firestore.collection("favorites")
                                .document(document.id)
                                .delete()
                        }

                        favoriteBtn.text =
                            "♡ Add to Favorites"

                        Toast.makeText(
                            this,
                            "Removed from favorites",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        selectDateBtn.setOnClickListener {

            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->

                    val selectedCalendar =
                        GregorianCalendar(
                            selectedYear,
                            selectedMonth,
                            selectedDay
                        )

                    val dayOfWeek =
                        selectedCalendar.get(
                            Calendar.DAY_OF_WEEK
                        )

                    if (
                        unavailableDays.contains(dayOfWeek)
                    ) {

                        Toast.makeText(
                            this,
                            "Doctor is unavailable on selected day",
                            Toast.LENGTH_LONG
                        ).show()

                        return@DatePickerDialog
                    }

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
                auth.currentUser == null ||
                auth.currentUser!!.isAnonymous
            ) {

                Toast.makeText(
                    this,
                    "Please login to book appointments",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            if (
                selectedDate.isNotEmpty() &&
                selectedSlot.isNotEmpty()
            ) {

                val appointment = hashMapOf(

                    "doctorName" to name,
                    "specialization" to specialization,
                    "date" to selectedDate,
                    "slot" to selectedSlot,
                    "userId" to auth.currentUser!!.uid
                )

                firestore.collection("appointments")
                    .add(appointment)

                    .addOnSuccessListener {

                        AlertDialog.Builder(this)
                            .setTitle("Appointment Confirmed")

                            .setMessage(
                                "Doctor: $name\n\n" +
                                        "Date: $selectedDate\n\n" +
                                        "Time: $selectedSlot"
                            )

                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }

                            .show()

                        val notification = Notification(

                            auth.currentUser!!.uid,

                            "🔔 Appointment booked with $name"
                        )

                        firestore.collection("notifications")
                            .add(notification)

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

        // SUBMIT REVIEW
        submitReviewBtn.setOnClickListener {

            if (
                auth.currentUser == null ||
                auth.currentUser!!.isAnonymous
            ) {

                Toast.makeText(
                    this,
                    "Please login to leave reviews",
                    Toast.LENGTH_LONG
                ).show()

                return@setOnClickListener
            }

            val reviewText =
                reviewInput.text.toString().trim()

            val stars =
                ratingBar.rating.toInt()

            if (
                reviewText.isEmpty() ||
                stars == 0
            ) {

                Toast.makeText(
                    this,
                    "Add rating and review",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            firestore.collection("reviews")
                .whereEqualTo(
                    "doctorName",
                    name
                )
                .whereEqualTo(
                    "userEmail",
                    auth.currentUser?.email
                )
                .get()

                .addOnSuccessListener { documents ->

                    if (!documents.isEmpty) {

                        Toast.makeText(
                            this,
                            "You already reviewed this doctor",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {

                        val review = Review(

                            auth.currentUser?.email ?: "",

                            name ?: "",

                            stars,

                            reviewText
                        )

                        firestore.collection("reviews")
                            .add(review)

                            .addOnSuccessListener {

                                Toast.makeText(
                                    this,
                                    "Review submitted",
                                    Toast.LENGTH_SHORT
                                ).show()

                                reviewInput.text.clear()

                                ratingBar.rating = 0f

                                loadReviews(
                                    name,
                                    reviewsContainer
                                )
                            }
                    }
                }
        }
    }

    private fun checkFavoriteStatus(
        doctorName: String?,
        favoriteBtn: Button
    ) {

        if (auth.currentUser == null) {
            return
        }

        firestore.collection("favorites")
            .whereEqualTo(
                "doctorName",
                doctorName
            )
            .whereEqualTo(
                "userId",
                auth.currentUser?.uid
            )
            .get()

            .addOnSuccessListener { documents: QuerySnapshot ->

                if (documents.isEmpty) {

                    favoriteBtn.text =
                        "♡ Add to Favorites"

                } else {

                    favoriteBtn.text =
                        "❤️ Remove Favorite"
                }
            }
    }

    private fun loadReviews(
        doctorName: String?,
        reviewsContainer: LinearLayout
    ) {

        reviewsContainer.removeAllViews()

        firestore.collection("reviews")
            .whereEqualTo(
                "doctorName",
                doctorName
            )
            .get()

            .addOnSuccessListener { documents ->

                for (document in documents) {

                    val userEmail =
                        document.getString("userEmail")

                    val rating =
                        document.getLong("rating")

                    val reviewText =
                        document.getString("reviewText")

                    val reviewCard =
                        LinearLayout(this)

                    reviewCard.orientation =
                        LinearLayout.VERTICAL

                    reviewCard.setPadding(
                        32,
                        32,
                        32,
                        32
                    )

                    reviewCard.setBackgroundColor(
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

                    reviewCard.layoutParams =
                        params

                    val emailText =
                        TextView(this)

                    emailText.text =
                        userEmail

                    emailText.textSize = 18f

                    emailText.setTypeface(
                        null,
                        android.graphics.Typeface.BOLD
                    )

                    val starsText =
                        TextView(this)

                    starsText.text =
                        "⭐".repeat(rating?.toInt() ?: 0)

                    starsText.textSize = 18f

                    starsText.setPadding(
                        0,
                        8,
                        0,
                        8
                    )

                    val reviewTextView =
                        TextView(this)

                    reviewTextView.text =
                        reviewText

                    reviewTextView.textSize = 16f

                    reviewCard.addView(
                        emailText
                    )

                    reviewCard.addView(
                        starsText
                    )

                    reviewCard.addView(
                        reviewTextView
                    )

                    reviewsContainer.addView(
                        reviewCard
                    )
                }
            }
    }

    private fun loadSlots(
        slotsContainer: GridLayout,
        doctorName: String?
    ) {

        slotsContainer.removeAllViews()

        for (slot in doctorSlots) {

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
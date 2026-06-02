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
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

import com.example.medbook.room.DatabaseProvider
import com.example.medbook.room.entity.RecentDoctorEntity
import com.example.medbook.room.entity.AppointmentHistoryEntity
import com.example.medbook.room.entity.FavoriteDoctorEntity

class DoctorDetailsActivity : AppCompatActivity() {

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

    private fun translateExperience(
        experience: String?
    ): String {

        val isMk =
            resources.configuration
                .locales[0]
                .language == "mk"

        if (!isMk) {
            return experience ?: ""
        }

        return experience
            ?.replace(
                " years experience",
                " години искуство"
            )
            ?.replace(
                " year experience",
                " година искуство"
            )
            ?: ""
    }

    private fun translateDescription(
        description: String?
    ): String {

        val isMk =
            resources.configuration
                .locales[0]
                .language == "mk"

        if (!isMk) {
            return description ?: ""
        }

        return when (description) {

            "Experienced cardiologist specialized in heart disease prevention, ECG diagnostics and patient care." ->
                "Искусен кардиолог специјализиран за превенција на срцеви заболувања, ЕКГ дијагностика и грижа за пациентите."

            "Professional dentist with extensive experience in cosmetic dentistry and oral health treatments." ->
                "Професионален стоматолог со долгогодишно искуство во естетска стоматологија и третмани за орално здравје."

            "Friendly pediatrician dedicated to children's health, preventive care and family consultations." ->
                "Пријателски настроен педијатар посветен на здравјето на децата, превентивната грижа и семејните консултации."

            "Highly experienced neurologist focused on brain disorders, migraines and neurological diagnostics." ->
                "Високо искусен невролог специјализиран за мозочни заболувања, мигрени и невролошка дијагностика."

            "Dermatology specialist experienced in skin care treatments, acne therapy and cosmetic procedures." ->
                "Дерматолог специјализиран за третмани на кожа, терапија за акни и естетски процедури."

            "Orthopedic specialist helping patients recover from injuries, fractures and joint problems." ->
                "Ортопед кој им помага на пациентите во закрепнување од повреди, скршеници и проблеми со зглобовите."

            "Experienced gynecologist focused on women's health, pregnancy care and preventive examinations." ->
                "Искусен гинеколог специјализиран за здравјето на жените, следење на бременост и превентивни прегледи."

            "Compassionate psychiatrist helping patients with anxiety, stress management and mental wellness." ->
                "Психијатар кој им помага на пациентите во справување со анксиозност, стрес и ментална благосостојба."

            "Eye specialist providing vision care, diagnostics and treatment for various eye conditions." ->
                "Офталмолог кој обезбедува грижа за видот, дијагностика и третман на различни очни заболувања."

            "ENT specialist treating ear, nose and throat conditions with modern medical approaches." ->
                "ОРЛ специјалист за лекување на заболувања на уво, нос и грло со современи медицински пристапи."

            "Expert radiologist specialized in MRI, CT scan diagnostics and medical imaging interpretation." ->
                "Радиолог специјализиран за МРИ, КТ дијагностика и интерпретација на медицински снимки."

            "Highly skilled surgeon with extensive experience in complex surgeries and patient recovery care." ->
                "Високо квалификуван хирург со долгогодишно искуство во сложени операции и постоперативна грижа за пациентите."

            else -> description ?: ""
        }
    }

    private var selectedSlot = ""
    private var selectedDate = ""

    private lateinit var firestore: FirebaseFirestore

    private lateinit var auth: FirebaseAuth

    private lateinit var doctorSlots: ArrayList<String>

    private lateinit var unavailableDays: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS
                    ),
                    100
                )
            }
        }

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
        doctorSpecialization.text =
            translateSpecialization(
                specialization
            )

        doctorExperience.text =
            translateExperience(
                experience
            )

        doctorDescription.text =
            translateDescription(
                description
            )

        doctorImage.setImageResource(image)

        lifecycleScope.launch {

            DatabaseProvider
                .getDatabase(this@DoctorDetailsActivity)
                .recentDoctorDao()
                .insertRecentDoctor(

                    RecentDoctorEntity(

                        doctorName = name ?: "",

                        specialization = specialization ?: "",

                        viewedAt =
                            System.currentTimeMillis()
                    )
                )
        }

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
                    getString(R.string.login_with_account),
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

                                lifecycleScope.launch {

                                    DatabaseProvider
                                        .getDatabase(this@DoctorDetailsActivity)
                                        .favoriteDoctorDao()
                                        .insertFavorite(

                                            FavoriteDoctorEntity(

                                                name = name ?: "",

                                                specialization =
                                                    specialization ?: "",

                                                rating =
                                                    rating ?: "",

                                                experience =
                                                    experience ?: "",

                                                imageResId = image,

                                                price =
                                                    fee ?: "",

                                                description =
                                                    description ?: ""
                                            )
                                        )
                                }

                                val bundle = Bundle()

                                bundle.putString(
                                    "doctor_name",
                                    name
                                )

                                bundle.putString(
                                    "specialization",
                                    specialization
                                )

                                com.google.firebase.analytics.FirebaseAnalytics
                                    .getInstance(this)
                                    .logEvent(
                                        "favorite_added",
                                        bundle
                                    )

                                favoriteBtn.text =
                                    getString(R.string.remove_favorite)

                                Toast.makeText(
                                    this,
                                    getString(R.string.added_to_favorites),
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
                            getString(R.string.add_favorite)

                        Toast.makeText(
                            this,
                            getString(R.string.removed_from_favorites),
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
                            getString(R.string.doctor_unavailable),
                            Toast.LENGTH_LONG
                        ).show()

                        return@DatePickerDialog
                    }

                    selectedDate =
                        "$selectedDay/${selectedMonth + 1}/$selectedYear"

                    selectedDateText.text =
                        getString(
                            R.string.selected_date,
                            selectedDate
                        )

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
                    getString(R.string.login_to_book),
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

                        lifecycleScope.launch {

                            DatabaseProvider
                                .getDatabase(this@DoctorDetailsActivity)
                                .appointmentHistoryDao()
                                .insertAppointment(

                                    AppointmentHistoryEntity(

                                        doctorName = name ?: "",

                                        appointmentDate = selectedDate,

                                        appointmentTime = selectedSlot,

                                        status = "BOOKED",

                                        createdAt =
                                            System.currentTimeMillis()
                                    )
                                )
                        }

                        val bundle = Bundle()

                        bundle.putString(
                            "doctor_name",
                            name
                        )

                        bundle.putString(
                            "specialization",
                            specialization
                        )

                        com.google.firebase.analytics.FirebaseAnalytics
                            .getInstance(this)
                            .logEvent(
                                "appointment_booked",
                                bundle
                            )

                        showAppointmentNotification(
                            name ?: "Doctor"
                        )

                        AlertDialog.Builder(this)
                            .setTitle(
                                getString(R.string.appointment_confirmed)
                            )

                            .setMessage(
                                getString(
                                    R.string.appointment_details,
                                    name,
                                    selectedDate,
                                    selectedSlot
                                )
                            )

                            .setPositiveButton(
                                getString(R.string.ok)
                            )
                            { dialog, _ ->
                                dialog.dismiss()
                            }

                            .show()

                        val notificationMessage =

                            if (
                                resources.configuration.locales[0]
                                    .language == "mk"
                            ) {

                                "🔔 Закажан термин кај $name"

                            } else {

                                "🔔 Appointment booked with $name"
                            }

                        val notification = Notification(

                            auth.currentUser!!.uid,

                            notificationMessage
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
                            getString(R.string.failed_save_appointment),
                            Toast.LENGTH_LONG
                        ).show()
                    }

            } else {

                Toast.makeText(
                    this,
                    getString(R.string.select_date_time),
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
                    getString(R.string.login_leave_review),
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
                    getString(R.string.add_rating_review),
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
                            getString(R.string.already_reviewed),
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
                                    getString(R.string.review_submitted),
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
                        getString(R.string.add_favorite)

                } else {

                    favoriteBtn.text =
                        getString(R.string.remove_favorite)
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
                        getString(
                            R.string.selected_slot,
                            slot
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            slotsContainer.addView(slotButton)
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
package com.example.medbook.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointment_history")
data class AppointmentHistoryEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val doctorName: String,

    val appointmentDate: String,

    val appointmentTime: String,

    val status: String,

    val createdAt: Long
)
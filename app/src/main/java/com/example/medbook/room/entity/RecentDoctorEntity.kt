package com.example.medbook.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_doctors")
data class RecentDoctorEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val doctorName: String,

    val specialization: String,

    val viewedAt: Long
)
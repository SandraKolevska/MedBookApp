package com.example.medbook.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_doctors")
data class FavoriteDoctorEntity(

    @PrimaryKey
    val name: String,

    val specialization: String,

    val rating: String,

    val experience: String,

    val imageResId: Int,

    val price: String,

    val description: String
)
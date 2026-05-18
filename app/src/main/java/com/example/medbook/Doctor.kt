package com.example.medbook

data class Doctor(

    val name: String,
    val specialization: String,
    val rating: String,
    val experience: String,
    val imageResId: Int,

    val slots: List<String>,

    val unavailableDays: List<Int>
)
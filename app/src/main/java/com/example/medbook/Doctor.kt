package com.example.medbook

data class Doctor(

    val name: String,

    val specialization: String,

    val rating: String,

    val experience: String,

    val imageResId: Int,

    val price: String,

    val description: String,

    val slots: List<String>,

    val unavailableDays: List<Int>
)
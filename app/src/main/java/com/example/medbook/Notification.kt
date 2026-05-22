package com.example.medbook

data class Notification(

    val userId: String = "",

    val message: String = "",

    val timestamp: Long = System.currentTimeMillis()
)
package com.example.medbook.room

import androidx.room.Database
import androidx.room.RoomDatabase

import com.example.medbook.room.dao.AppointmentHistoryDao
import com.example.medbook.room.dao.FavoriteDoctorDao
import com.example.medbook.room.dao.RecentDoctorDao

import com.example.medbook.room.entity.AppointmentHistoryEntity
import com.example.medbook.room.entity.FavoriteDoctorEntity
import com.example.medbook.room.entity.RecentDoctorEntity

@Database(
    entities = [
        FavoriteDoctorEntity::class,
        RecentDoctorEntity::class,
        AppointmentHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDoctorDao():
            FavoriteDoctorDao

    abstract fun recentDoctorDao():
            RecentDoctorDao

    abstract fun appointmentHistoryDao():
            AppointmentHistoryDao
}
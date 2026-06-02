package com.example.medbook.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.medbook.room.entity.AppointmentHistoryEntity

@Dao
interface AppointmentHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(
        appointment: AppointmentHistoryEntity
    )

    @Query(
        "SELECT * FROM appointment_history ORDER BY createdAt DESC"
    )
    suspend fun getAppointmentHistory():
            List<AppointmentHistoryEntity>

    @Query("DELETE FROM appointment_history")
    suspend fun clearHistory()
}
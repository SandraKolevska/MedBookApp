package com.example.medbook.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.medbook.room.entity.RecentDoctorEntity

@Dao
interface RecentDoctorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentDoctor(
        doctor: RecentDoctorEntity
    )

    @Query(
        "SELECT * FROM recent_doctors ORDER BY viewedAt DESC"
    )
    suspend fun getRecentDoctors():
            List<RecentDoctorEntity>

    @Query("DELETE FROM recent_doctors")
    suspend fun clearRecentDoctors()
}
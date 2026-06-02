package com.example.medbook.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.medbook.room.entity.FavoriteDoctorEntity

@Dao
interface FavoriteDoctorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(
        doctor: FavoriteDoctorEntity
    )

    @Query("SELECT * FROM favorite_doctors")
    suspend fun getAllFavorites():
            List<FavoriteDoctorEntity>

    @Query(
        "DELETE FROM favorite_doctors WHERE name = :doctorName"
    )
    suspend fun deleteFavorite(
        doctorName: String
    )
}
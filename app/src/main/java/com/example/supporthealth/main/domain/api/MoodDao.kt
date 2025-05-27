package com.example.supporthealth.main.domain.api

import androidx.room.*
import com.example.supporthealth.main.domain.models.MoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(mood: MoodEntity)

    @Query("SELECT * FROM mood WHERE date = :date AND dayPart = :dayPart LIMIT 1")
    suspend fun getMoodByDatePart(date: String, dayPart: String): MoodEntity?

    @Query("SELECT * FROM mood ORDER BY date DESC")
    fun getAllMoods(): Flow<List<MoodEntity>>

    @Update
    suspend fun updateMood(mood: MoodEntity)

    @Delete
    suspend fun deleteMood(mood: MoodEntity)
}
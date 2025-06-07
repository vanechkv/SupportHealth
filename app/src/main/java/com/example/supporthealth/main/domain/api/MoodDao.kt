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

    @Query("SELECT * FROM mood WHERE id = :moodId")
    suspend fun getMoodById(moodId: Long): MoodEntity

    @Query("SELECT * FROM mood ORDER BY date DESC")
    fun getAllMoods(): Flow<List<MoodEntity>>

    @Query("SELECT * FROM mood WHERE date = :targetDate ORDER BY date DESC")
    fun getMoodsByDate(targetDate: String): Flow<List<MoodEntity>>

    @Update
    suspend fun updateMood(mood: MoodEntity)

    @Query("""
    SELECT * FROM mood
    WHERE date BETWEEN :startDate AND :endDate
    ORDER BY date ASC
""")
    fun getMoodsBetweenDates(startDate: String, endDate: String): Flow<List<MoodEntity>>

    @Delete
    suspend fun deleteMood(mood: MoodEntity)
}
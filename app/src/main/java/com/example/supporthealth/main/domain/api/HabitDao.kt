package com.example.supporthealth.main.domain.api

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.supporthealth.main.domain.models.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: HabitEntity): Long

    @Update
    suspend fun update(habit: HabitEntity)

    @Delete
    suspend fun delete(habit: HabitEntity)

    @Query("SELECT * FROM habit WHERE id = :id")
    fun getById(id: Long): Flow<HabitEntity>

    @Query("SELECT * FROM habit ORDER BY timestamp ASC")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("DELETE FROM habit")
    suspend fun deleteAll()
}
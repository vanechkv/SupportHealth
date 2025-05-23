package com.example.supporthealth.main.domain.api

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.supporthealth.main.domain.models.StepEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(step: StepEntity): Long

    @Update
    suspend fun update(step: StepEntity)

    @Delete
    suspend fun delete(step: StepEntity)

    @Query("SELECT * FROM steps WHERE date = :date LIMIT 1")
    suspend fun getStepsByDate(date: String): StepEntity?

    @Query("SELECT * FROM steps ORDER BY date DESC")
    suspend fun getAllSteps(): List<StepEntity>

    @Query("DELETE FROM steps")
    suspend fun clearAll()

    @Query("SELECT * FROM steps ORDER BY date DESC")
    fun observeAllSteps(): Flow<MutableList<StepEntity>>

    @Query("SELECT * FROM steps WHERE date = :date LIMIT 1")
    fun getStepByDate(date: String): Flow<StepEntity>
}
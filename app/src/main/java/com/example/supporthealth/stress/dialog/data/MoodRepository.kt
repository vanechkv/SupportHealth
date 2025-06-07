package com.example.supporthealth.stress.dialog.data

import com.example.supporthealth.main.domain.api.MoodDao
import com.example.supporthealth.main.domain.models.MoodEntity
import com.example.supporthealth.stress.dialog.domain.DayPart
import com.example.supporthealth.stress.dialog.domain.MoodData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MoodRepository(
    private val moodDao: MoodDao
) {

    suspend fun insertMood(date: String, dayPart: DayPart, moodData: MoodData) {
        val moodEntity = MoodEntity(
            date = date,
            dayPart = dayPart,
            moodLevel = moodData.mood,
            energyLevel = moodData.energy
        )
        moodDao.insertMood(moodEntity)
    }

    suspend fun getMoodById(moodId: Long): MoodEntity {
        return moodDao.getMoodById(moodId)
    }

    suspend fun getMoodByDate(date: String, part: String): MoodEntity? {
        return moodDao.getMoodByDatePart(date, part)
    }

    fun getAllMoods(): Flow<List<MoodEntity>> {
        return moodDao.getAllMoods()
    }

    suspend fun updateMood(mood: MoodEntity) {
        moodDao.updateMood(mood)
    }

    suspend fun deleteMood(mood: MoodEntity) {
        moodDao.deleteMood(mood)
    }
}
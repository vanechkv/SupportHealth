package com.example.supporthealth.stress.main.data

import com.example.supporthealth.main.domain.api.MoodDao
import com.example.supporthealth.main.domain.models.MoodEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MoodRepository(
    private val moodDao: MoodDao
) {

    suspend fun insertMood(moodData: MoodEntity) {
        val now = LocalDateTime.now()
        val date = now.toLocalDate().format(DateTimeFormatter.ISO_DATE)
        val hour = now.hour
        val partOfDay = getDayPartFromHour(hour)

        val moodEntity = MoodEntity(
            date = date,
            dayPart = partOfDay,
            moodLevel = moodData.moodLevel,
            energyLevel = moodData.energyLevel
        )
        moodDao.insertMood(moodEntity)
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

    private fun getDayPartFromHour(hour: Int): String {
        return when (hour) {
            in 5..11 -> "утро"
            in 12..16 -> "день"
            in 17..22 -> "вечер"
            else -> "ночь"
        }
    }
}
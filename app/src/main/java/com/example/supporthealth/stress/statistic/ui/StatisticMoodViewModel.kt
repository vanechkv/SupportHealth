package com.example.supporthealth.stress.statistic.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.supporthealth.R
import com.example.supporthealth.main.domain.api.MoodDao
import com.example.supporthealth.main.domain.models.MoodEntity

class StatisticMoodViewModel(
    private val moodDao: MoodDao
) : ViewModel() {

    fun observeMoodData(startDate: String, endDate: String): LiveData<List<MoodEntity>> {
        return moodDao.getMoodsBetweenDates(startDate, endDate).asLiveData()
    }

    fun getMoodDescription(level: Int): String {
        return when (level) {
            0 -> "Ужасное"
            1 -> "Плохое"
            2 -> "Не очень"
            3 -> "Нормальное"
            4 -> "Хорошее"
            5 -> "Отличное"
            6 -> "Восхитительное"
            else -> "Неизвестно"
        }
    }

    fun getMoodColorResId(level: Int): Int {
        return when (level) {
            0 -> R.color.mood_terrible
            1 -> R.color.mood_bad
            2 -> R.color.mood_okay
            3 -> R.color.mood_normal
            4 -> R.color.mood_good
            5 -> R.color.mood_great
            6 -> R.color.mood_greatest
            else -> R.color.mood_default
        }
    }

    fun getMoodEmojiResId(level: Int): Int {
        return when (level) {
            0 -> R.drawable.ic_sentiment_very_dissatisfied
            1 -> R.drawable.ic_sentiment_nogood
            2 -> R.drawable.ic_sentiment_dissatisfied
            3 -> R.drawable.ic_sentiment_neutral
            4 -> R.drawable.ic_sentiment_satisfied
            5 -> R.drawable.ic_sentiment_well
            6 -> R.drawable.ic_sentiment_very_satisfied
            else -> R.drawable.ic_sentiment_neutral
        }
    }

    fun getEnergyDescription(level: Int): String {
        return when (level) {
            0 -> "Полностью выжат"
            1 -> "Очень мало энергии"
            2 -> "Мало энергии"
            3 -> "Энергия в норме"
            4 -> "Много энергии"
            5 -> "Очень много энергии"
            6 -> "Гиперактивность"
            else -> "Неизвестно"
        }
    }

    fun getEnergyColorResId(level: Int): Int {
        return when (level) {
            0 -> R.color.energy_0
            1 -> R.color.energy_1
            2 -> R.color.energy_2
            3 -> R.color.energy_3
            4 -> R.color.energy_4
            5 -> R.color.energy_5
            6 -> R.color.energy_6
            else -> R.color.mood_default
        }
    }
}

package com.example.supporthealth.stress.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supporthealth.R
import com.example.supporthealth.main.domain.models.MoodEntity
import com.example.supporthealth.stress.main.data.MoodRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StressViewModel(
    private val moodRepository: MoodRepository
) : ViewModel() {

    fun saveMoodData(mood: Int, energy: Int) {
        viewModelScope.launch {
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = now.format(formatter)
            val hour = now.hour
            val part = getDayPartFromHour(hour)

            val moodEntity = MoodEntity(
                date = date,
                dayPart = part,
                moodLevel = mood,
                energyLevel = energy
            )

            moodRepository.insertMood(moodEntity)
        }
    }

    private fun getDayPartFromHour(hour: Int): String {
        return when (hour) {
            in 5..11 -> "утро"
            in 12..16 -> "день"
            in 17..22 -> "вечер"
            else -> "ночь"
        }
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
            2 -> R.drawable.ic_sentiment_very_dissatisfied
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

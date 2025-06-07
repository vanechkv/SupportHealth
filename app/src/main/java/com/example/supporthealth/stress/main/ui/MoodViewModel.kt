package com.example.supporthealth.stress.main.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.supporthealth.R
import com.example.supporthealth.main.domain.api.MoodDao
import com.example.supporthealth.main.domain.models.MoodEntity

class MoodViewModel(
    private val moodDao: MoodDao
) : ViewModel() {

    fun observeMoodByDate(date: String): LiveData<List<MoodEntity>> {
        return moodDao.getMoodsByDate(date).asLiveData()
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
}
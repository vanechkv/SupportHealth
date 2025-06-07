package com.example.supporthealth.main.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import com.example.supporthealth.stress.dialog.domain.DayPart

@Entity(
    tableName = "mood",
    indices = [Index(value = ["date", "dayPart"], unique = true)]
)
data class MoodEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val dayPart: DayPart,
    val moodLevel: Int,
    val energyLevel: Int
)
package com.example.supporthealth.main.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "habit"
)
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val date: String,
    val attempt: Int,
    val target: Int,
    val record: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val attemptStartTimeMillis: Long = System.currentTimeMillis()
)

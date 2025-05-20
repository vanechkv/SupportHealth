package com.example.supporthealth.main.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "steps")
data class StepEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val steps: Int,
    val target: Int,
    val calories: Int,
    val distant: Float,
    val time: Int,
    val floors: Int
)

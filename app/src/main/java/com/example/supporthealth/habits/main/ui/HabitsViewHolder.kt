package com.example.supporthealth.habits.main.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.supporthealth.R
import com.example.supporthealth.main.domain.models.HabitEntity

class HabitsViewHolder(parent: ViewGroup, private val onHabitClick: (HabitEntity) -> Unit) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.habits_view, parent, false)
) {
    private val title: TextView = itemView.findViewById(R.id.title)
    private val target: TextView = itemView.findViewById(R.id.target_value)
    private val attempt: TextView = itemView.findViewById(R.id.attempt_value)
    private val record: TextView = itemView.findViewById(R.id.record_value)
    private val donut: DonutHabitView = itemView.findViewById(R.id.donut_habit)

    private var currentHabit: HabitEntity? = null

    init {
        itemView.setOnClickListener {
            currentHabit?.let(onHabitClick)
        }
    }

    fun bind(habit: HabitEntity) {
        currentHabit = habit
        title.text = habit.name
        target.text = habit.target.toString()
        attempt.text = habit.attempt.toString()
        record.text = habit.record.toString()
        donut.strokeWidth = 42f
        updateProgress(System.currentTimeMillis())
    }

    fun updateProgress(now: Long) {
        val habit = currentHabit ?: return
        val startTimeMillis = habit.attemptStartTimeMillis
        if (now < startTimeMillis) {
            donut.updateSteps(0f, 1f)
        } else {
            val elapsed = now - startTimeMillis
            val targetDays = habit.target
            val elapsedDays = elapsed.toFloat() / (24 * 60 * 60 * 1000)
            donut.updateSteps(elapsedDays, targetDays.toFloat())
        }
    }
}
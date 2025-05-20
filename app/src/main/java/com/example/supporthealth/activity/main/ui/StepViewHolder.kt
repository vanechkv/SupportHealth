package com.example.supporthealth.activity.main.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.supporthealth.R
import com.example.supporthealth.main.domain.models.StepEntity
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class StepViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.statistic_activity_view, parent, false)
) {
    private val donutProgress: CircularProgressBar = itemView.findViewById(R.id.donut_progress)
    private val steps: TextView = itemView.findViewById(R.id.steps)
    private val targetSteps: TextView = itemView.findViewById(R.id.target_steps)

    fun bind(step: StepEntity) {
        donutProgress.progressMax = step.target.toFloat()
        donutProgress.progress = step.steps.coerceAtMost(step.target).toFloat()
        steps.text = pluralizeSteps(step.steps)
        targetSteps.text = step.target.toString()
    }

    private fun pluralizeSteps(steps: Int): String {
        val rem100 = steps % 100
        val rem10 = steps % 10
        return when {
            rem100 in 11..14 -> "$steps шагов"
            rem10 == 1 -> "$steps шаг"
            rem10 in 2..4 -> "$steps шага"
            else -> "$steps шагов"
        }
    }
}
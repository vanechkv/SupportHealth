package com.example.supporthealth.activity.main.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.supporthealth.R
import com.example.supporthealth.main.domain.models.StepEntity
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class StepViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.statistic_activity_view, parent, false)
) {
    private val donutProgress: DonutActivityView = itemView.findViewById(R.id.static_donut)
    private val steps: TextView = itemView.findViewById(R.id.steps)
    private val targetSteps: TextView = itemView.findViewById(R.id.target_steps)
    private val iconComplete: ImageView = itemView.findViewById(R.id.icon_complete)

    fun bind(step: StepEntity) {
        donutProgress.updateSteps(step.steps, step.target)
        steps.text = pluralizeSteps(step.steps)
        targetSteps.text = step.target.toString()
        iconComplete.isVisible = step.steps >= step.target
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
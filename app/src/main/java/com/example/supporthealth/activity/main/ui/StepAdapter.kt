package com.example.supporthealth.activity.main.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.supporthealth.main.domain.models.StepEntity

class StepAdapter(
    private val steps: MutableList<StepEntity>
): RecyclerView.Adapter<StepViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        return StepViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return steps.size
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(steps[position])
    }

    fun updateSteps(newSteps: List<StepEntity>) {
        steps.clear()
        steps.addAll(newSteps)
        notifyDataSetChanged()
    }
}
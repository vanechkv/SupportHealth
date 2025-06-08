package com.example.supporthealth.habits.main.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.supporthealth.main.domain.models.HabitEntity

class HabitsAdapter(
    private val habits: List<HabitEntity>,
    private val onHabitClick: (HabitEntity) -> Unit
) : RecyclerView.Adapter<HabitsViewHolder>() {

    private var recyclerView: RecyclerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsViewHolder {
        return HabitsViewHolder(parent, onHabitClick)
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    override fun onBindViewHolder(holder: HabitsViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun onAttachedToRecyclerView(rv: RecyclerView) {
        super.onAttachedToRecyclerView(rv)
        recyclerView = rv
    }

    override fun onDetachedFromRecyclerView(rv: RecyclerView) {
        super.onDetachedFromRecyclerView(rv)
        recyclerView = null
    }

    fun updateProgressForVisibleItems(now: Long) {
        recyclerView?.let { rv ->
            for (i in 0 until itemCount) {
                val holder = rv.findViewHolderForAdapterPosition(i) as? HabitsViewHolder
                holder?.updateProgress(now)
            }
        }
    }
}
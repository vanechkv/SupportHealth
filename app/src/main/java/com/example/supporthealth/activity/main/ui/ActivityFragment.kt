package com.example.supporthealth.activity.main.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.supporthealth.R
import com.example.supporthealth.databinding.FragmentActivityBinding
import com.example.supporthealth.main.domain.models.StepEntity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityFragment : Fragment() {

    companion object {
        fun newInstance() = ActivityFragment()
    }

    private val viewModel: ActivityViewModel by viewModel()
    private lateinit var binding: FragmentActivityBinding
    private lateinit var adapter: StepAdapter

    private var allSteps: List<StepEntity> = emptyList()
    private var currentDayIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBarChar.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_activity_to_statisticActivityFragment)
        }

        adapter = StepAdapter(mutableListOf())
        binding.recyclerStep.adapter = adapter

        viewModel.observeStep().observe(viewLifecycleOwner) { steps ->
            allSteps = steps.sortedByDescending { it.date }
            currentDayIndex = 0
            showCurrentDay()
            updateDaySwitchButtons()
        }

        binding.buttonBackDay.setOnClickListener {
            if (currentDayIndex < allSteps.lastIndex) {
                currentDayIndex++
                showCurrentDay()
                updateDaySwitchButtons()
            }
        }
        binding.buttonForwardDay.setOnClickListener {
            if (currentDayIndex > 0) {
                currentDayIndex--
                showCurrentDay()
                updateDaySwitchButtons()
            }
        }
    }

    private fun showCurrentDay() {
        if (allSteps.isEmpty()) return
        val step = allSteps.getOrNull(currentDayIndex) ?: return
        adapter.updateSteps(listOf(step))
        binding.calendarDay.text = formatDateForUser(step.date)
        binding.caloriesValue.text = "${step.calories} ккал"
        binding.distantValue.text = "${step.steps} м"
        binding.timeValue.text = "${step.time} мин"
        binding.floorsValue.text = step.floors.toString()
    }

    private fun updateDaySwitchButtons() {
        binding.buttonBackDay.isVisible = currentDayIndex < allSteps.lastIndex
        binding.buttonForwardDay.isVisible = currentDayIndex > 0
    }

    private fun formatDateForUser(dateStr: String): String {
        val today = java.time.LocalDate.now()
        val yesterday = today.minusDays(1)
        val tomorrow = today.plusDays(1)
        val parsed = java.time.LocalDate.parse(dateStr)
        return when (parsed) {
            yesterday -> "Вчера"
            today -> "Сегодня"
            tomorrow -> "Завтра"
            else -> {
                val dayOfWeek = when (parsed.dayOfWeek) {
                    java.time.DayOfWeek.MONDAY -> "Пн"
                    java.time.DayOfWeek.TUESDAY -> "Вт"
                    java.time.DayOfWeek.WEDNESDAY -> "Ср"
                    java.time.DayOfWeek.THURSDAY -> "Чт"
                    java.time.DayOfWeek.FRIDAY -> "Пт"
                    java.time.DayOfWeek.SATURDAY -> "Сб"
                    java.time.DayOfWeek.SUNDAY -> "Вс"
                }
                "$dayOfWeek, ${parsed.dayOfMonth}"
            }
        }
    }
}
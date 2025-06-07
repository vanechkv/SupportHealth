package com.example.supporthealth.stress.main.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.supporthealth.R
import com.example.supporthealth.databinding.FragmentStressBinding
import com.example.supporthealth.main.domain.models.MoodEntity
import com.example.supporthealth.stress.dialog.domain.DayPart
import com.example.supporthealth.stress.dialog.ui.MoodDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StressFragment : Fragment(R.layout.fragment_stress) {

    private var _binding: FragmentStressBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoodViewModel by viewModel()

    private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private var currentDate = LocalDate.now()
    private var currentMoods: List<MoodEntity> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStressBinding.bind(view)

        binding.night.setOnClickListener {
            val mood = currentMoods.find { it.dayPart == DayPart.NIGHT }
            showDialog(mood?.id, DayPart.NIGHT)
        }
        binding.morning.setOnClickListener {
            val mood = currentMoods.find { it.dayPart == DayPart.MORNING }
            showDialog(mood?.id, DayPart.MORNING)
        }
        binding.day.setOnClickListener {
            val mood = currentMoods.find { it.dayPart == DayPart.DAY }
            showDialog(mood?.id, DayPart.DAY)
        }
        binding.evening.setOnClickListener {
            val mood = currentMoods.find { it.dayPart == DayPart.EVENING }
            showDialog(mood?.id, DayPart.EVENING)
        }

        binding.buttonBarChar.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_stress_to_statisticMoodFragment)
        }

        binding.buttonBackDay.setOnClickListener {
            currentDate = currentDate.minusDays(1)
            updateDate()
            observeMoods()
        }

        binding.buttonForwardDay.setOnClickListener {
            currentDate = currentDate.plusDays(1)
            updateDate()
            observeMoods()
        }

        currentMoods = emptyList()

        observeMoods()

        updateDate()
    }

    private fun observeMoods() {
        viewModel.observeMoodByDate(currentDate.format(dateFormat)).observe(viewLifecycleOwner) { moods ->
            currentMoods = moods

            val nightMood = moods.find { it.dayPart == DayPart.NIGHT }
            if (nightMood != null) {
                binding.iconNight.setImageResource(viewModel.getMoodEmojiResId(nightMood.moodLevel))
                binding.iconNight.setColorFilter(
                    ContextCompat.getColor(requireContext(), viewModel.getMoodColorResId(nightMood.moodLevel))
                )
            } else {
                binding.iconNight.setImageResource(R.drawable.ic_add_circle)
                binding.iconNight.clearColorFilter()
            }

            val morningMood = moods.find { it.dayPart == DayPart.MORNING }
            if (morningMood != null) {
                binding.iconMorning.setImageResource(viewModel.getMoodEmojiResId(morningMood.moodLevel))
                binding.iconMorning.setColorFilter(
                    ContextCompat.getColor(requireContext(), viewModel.getMoodColorResId(morningMood.moodLevel))
                )
            } else {
                binding.iconMorning.setImageResource(R.drawable.ic_add_circle)
                binding.iconMorning.clearColorFilter()
            }

            val dayMood = moods.find { it.dayPart == DayPart.DAY }
            if (dayMood != null) {
                binding.iconDay.setImageResource(viewModel.getMoodEmojiResId(dayMood.moodLevel))
                binding.iconDay.setColorFilter(
                    ContextCompat.getColor(requireContext(), viewModel.getMoodColorResId(dayMood.moodLevel))
                )
            } else {
                binding.iconDay.setImageResource(R.drawable.ic_add_circle)
                binding.iconDay.clearColorFilter()
            }

            val eveningMood = moods.find { it.dayPart == DayPart.EVENING }
            if (eveningMood != null) {
                binding.iconEvening.setImageResource(viewModel.getMoodEmojiResId(eveningMood.moodLevel))
                binding.iconEvening.setColorFilter(
                    ContextCompat.getColor(requireContext(), viewModel.getMoodColorResId(eveningMood.moodLevel))
                )
            } else {
                binding.iconEvening.setImageResource(R.drawable.ic_add_circle)
                binding.iconEvening.clearColorFilter()
            }
        }
    }

    private fun updateDate() {
        val formatted = currentDate.format(dateFormat)
        binding.calendarDay.text = formatDate(currentDate)
    }

    private fun formatDate(date: LocalDate): String {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val tomorrow = today.plusDays(1)

        return when (date) {
            yesterday -> "Вчера"
            today -> "Сегодня"
            tomorrow -> "Завтра"
            else -> {
                val dayOfWeek = when (date.dayOfWeek) {
                    java.time.DayOfWeek.MONDAY -> "Пн"
                    java.time.DayOfWeek.TUESDAY -> "Вт"
                    java.time.DayOfWeek.WEDNESDAY -> "Ср"
                    java.time.DayOfWeek.THURSDAY -> "Чт"
                    java.time.DayOfWeek.FRIDAY -> "Пт"
                    java.time.DayOfWeek.SATURDAY -> "Сб"
                    java.time.DayOfWeek.SUNDAY -> "Вс"
                }
                "$dayOfWeek, ${date.dayOfMonth}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialog(moodId: Long?, dayPart: DayPart) {
        val formatted = currentDate.format(dateFormat)
        val moodDialog = MoodDialogFragment.newInstance(moodId, formatted, dayPart)
        moodDialog.show(parentFragmentManager, "MoodDialog")
    }
}

package com.example.supporthealth.habits.dialog.ui

import android.app.DatePickerDialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.supporthealth.R
import com.example.supporthealth.app.scheduleGoalAlarm
import com.example.supporthealth.databinding.FragmentHabitListDialogListDialogBinding
import com.example.supporthealth.habits.main.ui.HabitsViewModel
import com.example.supporthealth.main.domain.models.HabitEntity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import kotlin.math.floor
import kotlin.math.log2
import kotlin.math.min

class HabitListDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentHabitListDialogListDialogBinding? = null
    private val viewModel: HabitsViewModel by viewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var habitId: Long? = null

    override fun onStart() {
        super.onStart()
        val dialog = dialog as? BottomSheetDialog ?: return
        val bottomSheet =
            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?: return
        val behavior = BottomSheetBehavior.from(bottomSheet)

        bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        bottomSheet.requestLayout()

        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHabitListDialogListDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        habitId = arguments?.getLong(ARG_HABIT_ID, -1L)

        if (habitId == null || habitId == -1L) {
            binding.title.text = "Добавление вредной\nпривычки"
        } else {
            binding.title.text = "Изменение вредной\nпривычки"
            loadHabit(habitId!!)
        }

        binding.buttonBack.setOnClickListener { dismiss() }

        binding.birthdayEditText.setOnClickListener {
            showDatePicker()
        }

        binding.saveButton.setOnClickListener {
            saveHabit()
        }
    }

    private fun loadHabit(id: Long) {

        viewModel.observeHabit(id).observe(viewLifecycleOwner) { habit ->
            habit?.let {
                binding.patronymicEditText.setText(it.name)
                binding.birthdayEditText.setText(it.date)
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val dateStr = binding.birthdayEditText.text.toString()
        if (dateStr.isNotEmpty()) {
            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val localDate = LocalDate.parse(dateStr, formatter)

                calendar.set(localDate.year, localDate.monthValue - 1, localDate.dayOfMonth)
            } catch (e: Exception) {
            }
        }

        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                binding.birthdayEditText.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }


    private fun saveHabit() {
        val name = binding.patronymicEditText.text.toString().trim()
        val date = binding.birthdayEditText.text.toString().trim()

        if (name.isEmpty()) {
            binding.patronymicEditText.error = "Введите название привычки"
            return
        }
        if (date.isEmpty()) {
            binding.birthdayEditText.error = "Выберите дату"
            return
        }

        val attemptStartMillis = dateStringToMillis(date)

        if (habitId == null || habitId == -1L) {
            val habit = HabitEntity(
                name = name,
                date = date,
                attempt = 1,
                target = calculateTargetFromDate(date),
                record = 0,
                attemptStartTimeMillis = attemptStartMillis
            )
            scheduleGoalAlarm(requireContext(), habit)
            viewModel.insertHabit(habit)
        } else {
            val habit = HabitEntity(
                id = habitId!!,
                name = name,
                date = date,
                attempt = 1,
                target = calculateTargetFromDate(date),
                record = 0,
                attemptStartTimeMillis = attemptStartMillis
            )
            scheduleGoalAlarm(requireContext(), habit)
            viewModel.updateHabit(habit)
        }

        dismiss()
    }

    private fun calculateTargetFromDate(dateStr: String): Int {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val startDate = LocalDate.parse(dateStr, formatter)
            val today = LocalDate.now()

            val daysPassed = ChronoUnit.DAYS.between(startDate, today).toInt().coerceAtLeast(0)

            var target = 1
            while (target <= daysPassed) {
                target = target shl 1
            }

            target

        } catch (e: Exception) {
            1
        }
    }

    companion object {

        private const val ARG_HABIT_ID = "arg_habit_id"

        fun newInstance(habitId: Long?): HabitListDialogFragment =
            HabitListDialogFragment().apply {
                arguments = Bundle().apply {
                    habitId?.let { putLong(ARG_HABIT_ID, it) }
                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun dateStringToMillis(dateString: String): Long {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDate = LocalDate.parse(dateString, formatter)
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}
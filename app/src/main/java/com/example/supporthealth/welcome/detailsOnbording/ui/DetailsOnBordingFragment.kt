package com.example.supporthealth.welcome.detailsOnbording.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.icu.util.LocaleData
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.navigation.fragment.findNavController
import com.example.supporthealth.R
import com.example.supporthealth.databinding.FragmentDetailsOnBordingBinding
import com.example.supporthealth.profile.details.domain.models.ActivityLevel
import com.example.supporthealth.profile.details.domain.models.Gender
import com.example.supporthealth.profile.details.domain.models.GoalType
import com.example.supporthealth.profile.details.domain.models.UserDetails
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class DetailsOnBordingFragment : Fragment() {

    companion object {
        fun newInstance() = DetailsOnBordingFragment()
    }

    private val viewModel: DetailsOnBordingViewModel by viewModel()
    private lateinit var binding: FragmentDetailsOnBordingBinding

    private var selectedDate: Calendar = Calendar.getInstance()
    private var selectedWeight: Int = 50
    private var selectedHeight: Int = 150

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsOnBordingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.weightEditText.setOnClickListener {
            showNumberPickerDialog(
                title = getString(R.string.select_value),
                unit = "кг",
                minValue = 2,
                maxValue = 362,
                currentValue = selectedWeight
            ) { selected ->
                selectedWeight = selected
                binding.weightEditText.setText(selected.toString())
            }
        }

        binding.heightEditText.setOnClickListener {
            showNumberPickerDialog(
                title = getString(R.string.select_value),
                unit = "см",
                minValue = 30,
                maxValue = 300,
                currentValue = selectedHeight
            ) { selected ->
                selectedHeight = selected
                binding.heightEditText.setText(selected.toString())
            }
        }

        binding.birthdayEditText.setOnClickListener {
            val year = selectedDate.get(Calendar.YEAR)
            val month = selectedDate.get(Calendar.MONTH)
            val day = selectedDate.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate.set(Calendar.YEAR, selectedYear)
                selectedDate.set(Calendar.MONTH, selectedMonth)
                selectedDate.set(Calendar.DAY_OF_MONTH, selectedDay)

                val formattedDate = "%02d.%02d.%d".format(selectedDay, selectedMonth + 1, selectedYear)
                binding.birthdayEditText.setText(formattedDate)
            }, year, month, day)

            datePickerDialog.show()
        }

        binding.mobilitySelector.setOnClickListener {
            val options = resources.getStringArray(R.array.mobility_array).toList()
            val current = binding.mobilitySelector.text.toString().takeIf { it.isNotBlank() }

            showChoiceDialog(
                title = getString(R.string.select_mobility),
                options = options,
                currentSelection = current
            ) { selected ->
                binding.mobilitySelector.text = selected
            }
        }

        binding.targetSelector.setOnClickListener {
            val options = resources.getStringArray(R.array.target_array).toList()
            val current = binding.targetSelector.text.toString().takeIf { it.isNotBlank() }

            showChoiceDialog(
                title = getString(R.string.select_target),
                options = options,
                currentSelection = current
            ) { selected ->
                binding.targetSelector.text = selected
            }
        }

        binding.buttonNext.setOnClickListener {
            findNavController().navigate(R.id.action_detailsOnBordingFragment_to_mainActivity)
            requireActivity().finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        val gender = when {
            binding.radioMale.isChecked -> Gender.MALE
            binding.radioFemale.isChecked -> Gender.FEMALE
            else -> Gender.MALE
        }

        val userData = UserDetails(
            surname = binding.surnameEditText.text.toString(),
            name = binding.nameEditText.text.toString(),
            patronymic = binding.patronymicEditText.text.toString(),
            gender = gender,
            height = selectedHeight,
            weight = selectedWeight,
            birthday = binding.birthdayEditText.text.toString(),
            mobility = binding.mobilitySelector.text.toString().toActivityLevel(),
            target = binding.targetSelector.text.toString().toGoalType()
        )

        viewModel.recalculateNorm(LocalDate.now().toString(),userData)
        viewModel.saveData(userData)

    }

    private fun showChoiceDialog(
        title: String,
        options: List<String>,
        currentSelection: String?,
        onSelected: (String) -> Unit
    ) {
        val selectedIndex = options.indexOf(currentSelection).takeIf { it >= 0 } ?: -1

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setSingleChoiceItems(options.toTypedArray(), selectedIndex) { dialog, which ->
                onSelected(options[which])
                dialog.dismiss()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showNumberPickerDialog(
        title: String,
        unit: String,
        minValue: Int,
        maxValue: Int,
        currentValue: Int,
        onValueSelected: (Int) -> Unit
    ) {
        val numberPicker = NumberPicker(requireContext()).apply {
            this.minValue = minValue
            this.maxValue = maxValue
            this.value = currentValue
            wrapSelectorWheel = false
        }

        AlertDialog.Builder(requireContext())
            .setTitle("$title ($unit)")
            .setView(numberPicker)
            .setPositiveButton("ОК") { _, _ ->
                onValueSelected(numberPicker.value)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    fun String.toActivityLevel(): ActivityLevel = when (this) {
        "Легкая активность" -> ActivityLevel.LOW
        "Средняя активность" -> ActivityLevel.MEDIUM
        "Высокая активность" -> ActivityLevel.HIGH
        else -> ActivityLevel.LOW
    }

    fun String.toGoalType(): GoalType = when (this) {
        "Сбросить вес" -> GoalType.LOSE
        "Удержать вес" -> GoalType.MAINTAIN
        "Нарастить мышцы" -> GoalType.GAIN
        else -> GoalType.MAINTAIN
    }
}
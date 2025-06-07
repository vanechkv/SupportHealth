package com.example.supporthealth.welcome.detailsOnbording.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.icu.util.LocaleData
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
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
    private var selectedTargetActivity: Int = 6000

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

        val letterFilter = object : InputFilter {
            override fun filter(
                source: CharSequence?,
                start: Int,
                end: Int,
                dest: Spanned?,
                dstart: Int,
                dend: Int
            ): CharSequence? {
                source ?: return null
                val regex = Regex("^[a-zA-Zа-яА-ЯёЁ]+$")
                if (source.isEmpty()) return null // allow delete
                return if (source.matches(regex)) null else ""
            }
        }

        binding.surnameEditText.filters = arrayOf(letterFilter)
        binding.nameEditText.filters = arrayOf(letterFilter)
        binding.patronymicEditText.filters = arrayOf(letterFilter)

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

        binding.back.setOnClickListener {
            findNavController().navigateUp()
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

        binding.targetNutritionSelector.setOnClickListener {
            val options = resources.getStringArray(R.array.target_array).toList()
            val current = binding.targetNutritionSelector.text.toString().takeIf { it.isNotBlank() }

            showChoiceDialog(
                title = getString(R.string.select_target),
                options = options,
                currentSelection = current
            ) { selected ->
                binding.targetNutritionSelector.text = selected
            }
        }

        binding.buttonNext.setOnClickListener {
            if (!validateFieldsWithErrors()) return@setOnClickListener

            findNavController().navigate(R.id.action_detailsOnBordingFragment_to_singUpFragment)

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
                targetNutrition = binding.targetNutritionSelector.text.toString().toGoalType(),
                targetActivity = selectedTargetActivity
            )

            viewModel.recalculateNorm(LocalDate.now().toString(),userData)
            viewModel.saveData(userData)
        }
    }

    private fun validateFieldsWithErrors(): Boolean {
        var isValid = true

        fun markError(field: android.widget.EditText, message: String) {
            field.error = message
            isValid = false
        }

        if (binding.surnameEditText.text.isNullOrBlank()) {
            markError(binding.surnameEditText, getString(R.string.error_required))
        } else {
            binding.surnameEditText.error = null
        }
        if (binding.nameEditText.text.isNullOrBlank()) {
            markError(binding.nameEditText, getString(R.string.error_required))
        } else {
            binding.nameEditText.error = null
        }
        if (binding.patronymicEditText.text.isNullOrBlank()) {
            markError(binding.patronymicEditText, getString(R.string.error_required))
        } else {
            binding.patronymicEditText.error = null
        }
        if (binding.weightEditText.text.isNullOrBlank()) {
            markError(binding.weightEditText, getString(R.string.error_required))
        } else {
            binding.weightEditText.error = null
        }
        if (binding.heightEditText.text.isNullOrBlank()) {
            markError(binding.heightEditText, getString(R.string.error_required))
        } else {
            binding.heightEditText.error = null
        }
        if (binding.birthdayEditText.text.isNullOrBlank()) {
            markError(binding.birthdayEditText, getString(R.string.error_required))
        } else {
            binding.birthdayEditText.error = null
        }
        if (binding.mobilitySelector.text.isNullOrBlank()) {
            binding.mobilitySelector.error = getString(R.string.error_required)
            isValid = false
        } else {
            binding.mobilitySelector.error = null
        }
        if (binding.targetNutritionSelector.text.isNullOrBlank()) {
            binding.targetNutritionSelector.error = getString(R.string.error_required)
            isValid = false
        } else {
            binding.targetNutritionSelector.error = null
        }
        val minSteps = 100
        val text = binding.activityTargetEditText.text?.toString()

        if (text.isNullOrBlank()) {
            markError(binding.activityTargetEditText, getString(R.string.error_required))
        } else {
            val steps = text.toIntOrNull()
            if (steps == null || steps < minSteps) {
                markError(
                    binding.activityTargetEditText,
                    getString(R.string.error_min_steps, minSteps)
                )
            } else {
                binding.activityTargetEditText.error = null
            }
        }
        if (!binding.radioMale.isChecked && !binding.radioFemale.isChecked) {
            binding.radioMale.error = getString(R.string.error_required)
            binding.radioFemale.error = getString(R.string.error_required)
            isValid = false
        } else {
            binding.radioMale.error = null
            binding.radioFemale.error = null
        }
        return isValid
    }

    override fun onDestroy() {
        super.onDestroy()
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
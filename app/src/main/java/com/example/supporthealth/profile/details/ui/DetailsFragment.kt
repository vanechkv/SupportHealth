package com.example.supporthealth.profile.details.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.supporthealth.R
import com.example.supporthealth.databinding.FragmentDetailsBinding
import com.example.supporthealth.profile.details.domain.models.ActivityLevel
import com.example.supporthealth.profile.details.domain.models.Gender
import com.example.supporthealth.profile.details.domain.models.GoalType
import com.example.supporthealth.profile.details.domain.models.UserDetails
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class DetailsFragment : Fragment() {

    companion object {
        fun newInstance() = DetailsFragment()
    }

    private val viewModel: DetailsViewModel by viewModel()

    private lateinit var binding: FragmentDetailsBinding

    private var selectedDate: Calendar = Calendar.getInstance()
    private var selectedWeight: Int = 50
    private var selectedHeight: Int = 150

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
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

        viewModel.loadData()

        viewModel.observeUser().observe(viewLifecycleOwner) { userData ->
            binding.surnameEditText.setText(userData?.surname)
            binding.nameEditText.setText(userData?.name)
            binding.patronymicEditText.setText(userData?.patronymic)
            binding.heightEditText.setText(userData?.height.toString())
            binding.weightEditText.setText(userData?.weight.toString())
            binding.birthdayEditText.setText(userData?.birthday)
            binding.mobilitySelector.text = userData?.mobility?.toUiString()
            binding.targetNutritionSelector.text = userData?.targetNutrition?.toUiString()
            binding.activityTargetEditText.setText(userData?.targetActivity.toString())

            if (userData?.weight != null) {
                selectedWeight = userData.weight
            }
            if (userData?.height != null) {
                selectedHeight = userData.height
            }

            when (userData?.gender) {
                Gender.MALE -> binding.radioMale.isChecked = true
                Gender.FEMALE -> binding.radioFemale.isChecked = true
                else -> binding.radioMale.isChecked
            }
        }

        binding.birthdayEditText.setOnClickListener {
            val year = selectedDate.get(Calendar.YEAR)
            val month = selectedDate.get(Calendar.MONTH)
            val day = selectedDate.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog =
                DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                    selectedDate.set(Calendar.YEAR, selectedYear)
                    selectedDate.set(Calendar.MONTH, selectedMonth)
                    selectedDate.set(Calendar.DAY_OF_MONTH, selectedDay)

                    val formattedDate =
                        "%02d.%02d.%d".format(selectedDay, selectedMonth + 1, selectedYear)
                    binding.birthdayEditText.setText(formattedDate)
                }, year, month, day)

            datePickerDialog.show()
        }

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

        binding.buttonBack.setOnClickListener {
            if (!validateFieldsWithErrors()) return@setOnClickListener
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (validateFieldsWithErrors()) {
                        isEnabled = false
                        findNavController().navigateUp()
                    } else {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Некоторые поля не заполнены")
                            .setMessage("Вы точно хотите выйти? Введённые данные не будут сохранены.")
                            .setPositiveButton("Выйти") { _, _ ->
                                isEnabled = false
                                findNavController().navigateUp()
                            }
                            .setNegativeButton("Остаться", null)
                            .show()
                    }
                }
            }
        )
    }

    override fun onPause() {
        super.onPause()

        if (!validateFieldsWithErrors()) return

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
            targetActivity = binding.activityTargetEditText.text.toString().toInt()
        )

        viewModel.recalculate(userData)
        viewModel.saveData(userData)
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

    fun ActivityLevel.toUiString(): String = when (this) {
        ActivityLevel.LOW -> "Легкая активность"
        ActivityLevel.MEDIUM -> "Средняя активность"
        ActivityLevel.HIGH -> "Высокая активность"
    }

    fun String.toGoalType(): GoalType = when (this) {
        "Сбросить вес" -> GoalType.LOSE
        "Удержать вес" -> GoalType.MAINTAIN
        "Нарастить мышцы" -> GoalType.GAIN
        else -> GoalType.MAINTAIN
    }

    fun GoalType.toUiString(): String = when (this) {
        GoalType.LOSE -> "Сбросить вес"
        GoalType.MAINTAIN -> "Удержать вес"
        GoalType.GAIN -> "Нарастить мышцы"
    }
}
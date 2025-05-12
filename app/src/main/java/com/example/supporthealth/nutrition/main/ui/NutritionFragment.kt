package com.example.supporthealth.nutrition.main.ui

import org.koin.androidx.viewmodel.ext.android.viewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.supporthealth.databinding.FragmentNutritionBinding
import com.example.supporthealth.main.domain.models.MealType
import com.example.supporthealth.nutrition.main.domain.models.MealState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NutritionFragment<TextView> : Fragment() {

    companion object {
        fun newInstance() = NutritionFragment<Any>()
    }

    private lateinit var binding: FragmentNutritionBinding

    private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private var currentDate = LocalDate.now()

    private val viewModel: NutritionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNutritionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeDailyNorm().observe(viewLifecycleOwner) { daily ->
            val norm = daily?.dailyNutrition

            binding.statisticView.apply {
                calories.text = "${norm?.consumedCalories} ккал"
                remainingCalories.text = "${norm?.recommendedCalories} ккал осталось"
                protein.text = "${norm?.consumedProteins} г"
                remainingProtein.text = "${norm?.recommendedProteins} г осталось"
                fat.text = "${norm?.consumedFats} г"
                remainingFat.text = "${norm?.recommendedFats} г осталось"
                carbohydrates.text = "${norm?.consumedCarbs} г"
                remainingCarbohydrates.text = "${norm?.recommendedCarbs} г осталось"
            }
        }

        viewModel.observeMealStates().observe(viewLifecycleOwner) { mealStates ->
            mealStates.forEach { state ->
                when (state.mealType) {
                    MealType.BREAKFAST -> setBreakfastState(state)
                    MealType.LUNCH -> setLunchState(state)
                    MealType.AFTERNOON_TEA -> setAfterNoonState(state)
                    MealType.DINNER -> setDinnerState(state)
                }
            }
        }

        viewModel.observeWater().observe(viewLifecycleOwner) { water ->
            if(water != null) {
                binding.waterView.recommendedWater.text = formatWaterMlToLiters(water.recommendedWaterMl)
                binding.waterView.title.text = formatWaterMlToLiters(water.consumedWaterMl)
            }
        }

        binding.waterView.buttonAdd.setOnClickListener {
            val formattedDate = currentDate.format(dateFormat)
            viewModel.addWater(formattedDate, 250)
        }

        binding.waterView.buttonDelete.setOnClickListener {
            val formattedDate = currentDate.format(dateFormat)
            viewModel.addWater(formattedDate, -250)
        }

        binding.eatingView.breakfast.setOnClickListener {
            openSearchScreen(MealType.BREAKFAST)
        }

        binding.eatingView.lunch.setOnClickListener {
            openSearchScreen(MealType.LUNCH)
        }

        binding.eatingView.afternoonTea.setOnClickListener {
            openSearchScreen(MealType.AFTERNOON_TEA)
        }

        binding.eatingView.dinner.setOnClickListener {
            openSearchScreen(MealType.DINNER)
        }

        binding.buttonBackDay.setOnClickListener {
            currentDate = currentDate.minusDays(1)
            updateDate()
        }

        binding.buttonForwardDay.setOnClickListener {
            currentDate = currentDate.plusDays(1)
            updateDate()
        }

        updateDate()
    }

    private fun formatWaterMlToLiters(ml: Int?): String {
        return "%.2f л".format((ml ?: 0) / 1000f)
    }

    private fun setBreakfastState(mealNorm: MealState) {
        if (mealNorm.consumedCalories > 0) {
            binding.eatingView.recommendedCaloriesBreakfast.isVisible = false
            binding.eatingView.caloriesBreakfast.isVisible = true
            binding.eatingView.caloriesBreakfast.text = mealNorm.consumedCalories.toString()
            binding.eatingView.separatorBreakfast.isVisible = true
            binding.eatingView.caloriesArgBreakfast.isVisible = true
            binding.eatingView.caloriesArgBreakfast.text = "${mealNorm.recommendedCalories} ккал"
        }else {
            binding.eatingView.recommendedCaloriesBreakfast.isVisible = true
            binding.eatingView.caloriesBreakfast.isVisible = false
            binding.eatingView.caloriesArgBreakfast.isVisible = false
            binding.eatingView.separatorBreakfast.isVisible = false
            binding.eatingView.recommendedCaloriesBreakfast.text = "${mealNorm.recommendedCalories} ккал"
        }
    }

    private fun setLunchState(mealNorm: MealState) {
        if (mealNorm.consumedCalories > 0) {
            binding.eatingView.recommendedCaloriesLunch.isVisible = false
            binding.eatingView.caloriesLunch.isVisible = true
            binding.eatingView.caloriesLunch.text = mealNorm.consumedCalories.toString()
            binding.eatingView.separatorLunch.isVisible = true
            binding.eatingView.caloriesArgLunch.isVisible = true
            binding.eatingView.caloriesArgLunch.text = "${mealNorm.recommendedCalories} ккал"
        }else {
            binding.eatingView.recommendedCaloriesLunch.isVisible = true
            binding.eatingView.caloriesLunch.isVisible = false
            binding.eatingView.caloriesArgLunch.isVisible = false
            binding.eatingView.separatorLunch.isVisible = false
            binding.eatingView.recommendedCaloriesLunch.text = "${mealNorm.recommendedCalories} ккал"
        }
    }

    private fun setAfterNoonState(mealNorm: MealState) {
        if (mealNorm.consumedCalories > 0) {
            binding.eatingView.recommendedCaloriesAfternoonTea.isVisible = false
            binding.eatingView.caloriesAfternoonTea.isVisible = true
            binding.eatingView.caloriesAfternoonTea.text = mealNorm.consumedCalories.toString()
            binding.eatingView.separatorAfternoonTea.isVisible = true
            binding.eatingView.caloriesArgAfternoonTea.isVisible = true
            binding.eatingView.caloriesArgAfternoonTea.text = "${mealNorm.recommendedCalories} ккал"
        }else {
            binding.eatingView.recommendedCaloriesAfternoonTea.isVisible = true
            binding.eatingView.caloriesAfternoonTea.isVisible = false
            binding.eatingView.caloriesArgAfternoonTea.isVisible = false
            binding.eatingView.separatorAfternoonTea.isVisible = false
            binding.eatingView.recommendedCaloriesAfternoonTea.text = "${mealNorm.recommendedCalories} ккал"
        }
    }

    private fun setDinnerState(mealNorm: MealState) {
        if (mealNorm.consumedCalories > 0) {
            binding.eatingView.recommendedCaloriesDinner.isVisible = false
            binding.eatingView.caloriesDinner.isVisible = true
            binding.eatingView.caloriesDinner.text = mealNorm.consumedCalories.toString()
            binding.eatingView.separatorDinner.isVisible = true
            binding.eatingView.caloriesArgDinner.isVisible = true
            binding.eatingView.caloriesArgDinner.text = "${mealNorm.recommendedCalories} ккал"
        }else {
            binding.eatingView.recommendedCaloriesDinner.isVisible = true
            binding.eatingView.caloriesDinner.isVisible = false
            binding.eatingView.caloriesArgDinner.isVisible = false
            binding.eatingView.separatorDinner.isVisible = false
            binding.eatingView.recommendedCaloriesDinner.text = "${mealNorm.recommendedCalories} ккал"
        }
    }

    private fun openSearchScreen(mealType: MealType) {
        val formattedDate = currentDate.format(dateFormat)
        val action = NutritionFragmentDirections
            .actionNutritionFragmentToSearchFragment(
                mealType = mealType,
                date = formattedDate
            )
        findNavController().navigate(action)
    }

    private fun updateDate() {
        val formatted = currentDate.format(dateFormat)
        binding.calendarDay.text = formatDate(currentDate)
        viewModel.loadFullDay(formatted)
        viewModel.loadWater(formatted)
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
}
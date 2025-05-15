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
import com.example.supporthealth.main.domain.models.MealEntity
import com.example.supporthealth.main.domain.models.MealType
import com.example.supporthealth.main.domain.models.NutritionFull
import com.example.supporthealth.nutrition.main.domain.models.Meal
import com.example.supporthealth.nutrition.main.domain.models.Nutrition
import com.example.supporthealth.nutrition.main.domain.models.Water
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

        viewModel.observeNutritionData().observe(viewLifecycleOwner) { nutrition ->
            if (nutrition != null) {
                setNutritionFull(nutrition)
            } else {
                viewModel.observeNutrition().observe(viewLifecycleOwner) { nutrition ->
                    setNutritionNorm(nutrition)
                }
                viewModel.observeWater().observe(viewLifecycleOwner) { water ->
                    setWaterNorm(water)
                }
                viewModel.observeMeals().observe(viewLifecycleOwner) { meals ->
                    meals.forEach { meal ->
                        when (meal.mealType) {
                            MealType.BREAKFAST -> setBreakfast(meal)
                            MealType.LUNCH -> setLunch(meal)
                            MealType.AFTERNOON_TEA -> setAfterNoon(meal)
                            MealType.DINNER -> setDinner(meal)
                        }
                    }
                }
            }
        }

        binding.waterView.buttonAdd.setOnClickListener {
            val formattedDate = currentDate.format(dateFormat)
            viewModel.updateWater(formattedDate, 250)
        }

        binding.waterView.buttonDelete.setOnClickListener {
            val formattedDate = currentDate.format(dateFormat)
            viewModel.updateWater(formattedDate, -250)
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

    private fun setBreakfast(meal: Meal) {
        binding.eatingView.recommendedCaloriesBreakfast.text =
            "${meal.calories} ккал"
        binding.eatingView.breakfast.setOnClickListener {
            openSearchScreen(meal.mealType)
        }

    }

    private fun setLunch(meal: Meal) {
        binding.eatingView.recommendedCaloriesLunch.text =
            "${meal.calories} ккал"
        binding.eatingView.lunch.setOnClickListener {
            openSearchScreen(meal.mealType)
        }

    }

    private fun setAfterNoon(meal: Meal) {
        binding.eatingView.recommendedCaloriesAfternoonTea.text =
            "${meal.calories} ккал"
        binding.eatingView.afternoonTea.setOnClickListener {
            openSearchScreen(meal.mealType)
        }

    }

    private fun setDinner(meal: Meal) {
        binding.eatingView.recommendedCaloriesDinner.text =
            "${meal.calories} ккал"
        binding.eatingView.dinner.setOnClickListener {
            openSearchScreen(meal.mealType)
        }

    }

    private fun setBreakfastData(meal: MealEntity) {
        if (meal.calories > 0) {
            binding.eatingView.apply {
                recommendedBreakfast.isVisible = false
                recommendedCaloriesBreakfast.isVisible = false
                caloriesBreakfast.isVisible = true
                caloriesBreakfast.text = meal.calories.toString()
                separatorBreakfast.isVisible = true
                caloriesArgBreakfast.isVisible = true
                caloriesArgBreakfast.text = "${meal.recommendedCalories} ккал"
                breakfast.setOnClickListener {
                    openEatingScreen(meal.id)
                }
            }
        } else {
            binding.eatingView.apply {
                recommendedBreakfast.isVisible = true
                recommendedCaloriesBreakfast.isVisible = true
                recommendedCaloriesBreakfast.text = "${meal.recommendedCalories} ккал"
                caloriesBreakfast.isVisible = false
                separatorBreakfast.isVisible = false
                caloriesArgBreakfast.isVisible = false
                breakfast.setOnClickListener {
                    openSearchScreen(meal.mealType)
                }
            }
        }
    }

    private fun setLunchData(meal: MealEntity) {
        if (meal.calories > 0) {
            binding.eatingView.apply {
                recommendedLunch.isVisible = false
                recommendedCaloriesLunch.isVisible = false
                caloriesLunch.isVisible = true
                caloriesLunch.text = meal.calories.toString()
                separatorLunch.isVisible = true
                caloriesArgLunch.isVisible = true
                caloriesArgLunch.text = "${meal.recommendedCalories} ккал"
                lunch.setOnClickListener {
                    openEatingScreen(meal.id)
                }
            }
        } else {
            binding.eatingView.apply {
                recommendedLunch.isVisible = true
                recommendedCaloriesLunch.isVisible = true
                recommendedCaloriesLunch.text = "${meal.recommendedCalories} ккал"
                caloriesLunch.isVisible = false
                separatorLunch.isVisible = false
                caloriesArgLunch.isVisible = false
                lunch.setOnClickListener {
                    openSearchScreen(meal.mealType)
                }
            }
        }
    }

    private fun setAfterNoonData(meal: MealEntity) {
        if (meal.calories > 0) {
            binding.eatingView.apply {
                recommendedAfternoonTea.isVisible = false
                recommendedCaloriesAfternoonTea.isVisible = false
                caloriesAfternoonTea.isVisible = true
                caloriesAfternoonTea.text = meal.calories.toString()
                separatorAfternoonTea.isVisible = true
                caloriesArgAfternoonTea.isVisible = true
                caloriesArgAfternoonTea.text = "${meal.recommendedCalories} ккал"
                afternoonTea.setOnClickListener {
                    openEatingScreen(meal.id)
                }
            }
        } else {
            binding.eatingView.apply {
                recommendedAfternoonTea.isVisible = true
                recommendedCaloriesAfternoonTea.isVisible = true
                recommendedCaloriesAfternoonTea.text = "${meal.recommendedCalories} ккал"
                caloriesAfternoonTea.isVisible = false
                separatorAfternoonTea.isVisible = false
                caloriesArgAfternoonTea.isVisible = false
                afternoonTea.setOnClickListener {
                    openSearchScreen(meal.mealType)
                }
            }
        }
    }

    private fun setDinnerData(meal: MealEntity) {
        if (meal.calories > 0) {
            binding.eatingView.apply {
                recommendedDinner.isVisible = false
                recommendedCaloriesDinner.isVisible = false
                caloriesDinner.isVisible = true
                caloriesDinner.text = meal.calories.toString()
                separatorDinner.isVisible = true
                caloriesArgDinner.isVisible = true
                caloriesArgDinner.text = "${meal.recommendedCalories} ккал"
                dinner.setOnClickListener {
                    openEatingScreen(meal.id)
                }
            }
        } else {
            binding.eatingView.apply {
                recommendedDinner.isVisible = true
                recommendedCaloriesDinner.isVisible = true
                recommendedCaloriesDinner.text = "${meal.recommendedCalories} ккал"
                caloriesDinner.isVisible = false
                separatorDinner.isVisible = false
                caloriesArgDinner.isVisible = false
                dinner.setOnClickListener {
                    openSearchScreen(meal.mealType)
                }
            }
        }
    }

    private fun openSearchScreen(mealType: MealType) {
        val formattedDate = currentDate.format(dateFormat)
        val action = NutritionFragmentDirections
            .actionNutritionFragmentToSearchFragment(
                meal = mealType,
                date = formattedDate
            )
        findNavController().navigate(action)
    }

    private fun openEatingScreen(mealId: Long) {
        val action = NutritionFragmentDirections
            .actionNavigationNutritionToEatingFragment(
                mealId = mealId
            )
        findNavController().navigate(action)
    }

    private fun updateDate() {
        val formatted = currentDate.format(dateFormat)
        binding.calendarDay.text = formatDate(currentDate)
        viewModel.loadDay(formatted)
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

    private fun setNutritionFull(nutritionFull: NutritionFull) {
        binding.statisticView.apply {
            calories.text = "${nutritionFull.nutrition.calories} ккал"
            remainingCalories.text = "${nutritionFull.nutrition.recommendedCalories} ккал осталось"
            protein.text = "${nutritionFull.nutrition.proteins} г"
            remainingProtein.text = "${nutritionFull.nutrition.recommendedProteins.toInt()} г осталось"
            fat.text = "${nutritionFull.nutrition.fats} г"
            remainingFat.text = "${nutritionFull.nutrition.recommendedFats} г осталось"
            carbohydrates.text = "${nutritionFull.nutrition.carbs} г"
            remainingCarbohydrates.text = "${nutritionFull.nutrition.recommendedCarbs} г осталось"
        }

        nutritionFull.meals.forEach { meal ->
            when (meal.mealType) {
                MealType.BREAKFAST -> setBreakfastData(meal)
                MealType.LUNCH -> setLunchData(meal)
                MealType.AFTERNOON_TEA -> setAfterNoonData(meal)
                MealType.DINNER -> setDinnerData(meal)
            }
        }

        binding.waterView.apply {
            recommendedWater.text =
                formatWaterMlToLiters(nutritionFull.water.recommendedWaterMl)
            title.text = formatWaterMlToLiters(nutritionFull.water.waterMl)
        }
    }

    private fun setNutritionNorm(nutrition: Nutrition) {
        binding.statisticView.apply {
            calories.text = "0 ккал"
            remainingCalories.text = "${nutrition.calories} ккал осталось"
            protein.text = "0 г"
            remainingProtein.text = "${nutrition.proteins} г осталось"
            fat.text = "0 г"
            remainingFat.text = "${nutrition.fats} г осталось"
            carbohydrates.text = "0 г"
            remainingCarbohydrates.text = "${nutrition.carbs} г осталось"
        }
    }

    private fun setWaterNorm(water: Water) {
        binding.waterView.apply {
            title.text = "0 л"
            recommendedWater.text = formatWaterMlToLiters(water.waterMl)
        }
    }
}
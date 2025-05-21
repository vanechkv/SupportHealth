package com.example.supporthealth.nutrition.main.ui

import android.graphics.Color
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.supporthealth.R
import com.example.supporthealth.databinding.FragmentNutritionBinding
import com.example.supporthealth.main.domain.models.MealEntity
import com.example.supporthealth.main.domain.models.MealType
import com.example.supporthealth.main.domain.models.NutritionFull
import com.example.supporthealth.nutrition.main.domain.models.Meal
import com.example.supporthealth.nutrition.main.domain.models.Nutrition
import com.example.supporthealth.nutrition.main.domain.models.Result
import com.example.supporthealth.nutrition.main.domain.models.Water
import com.google.android.material.color.MaterialColors
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class NutritionFragment : Fragment() {

    companion object {
        fun newInstance() = NutritionFragment()
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

        viewModel.observeMealResult().observe(viewLifecycleOwner) { resultMap ->
            resultMap?.forEach { (mealType, result) ->
                when (mealType) {
                    MealType.BREAKFAST -> resultBreakfast(result)
                    MealType.LUNCH -> resultLunch(result)
                    MealType.AFTERNOON_TEA -> resultAfternoonTea(result)
                    MealType.DINNER -> resultDinner(result)
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
        val liters = (ml ?: 0) / 1000f
        return when {
            liters == 0f -> "0 л"
            liters % 1f == 0f -> "${liters.toInt()} л"
            else -> {
                val value = String.format(Locale("ru"), "%.2f", liters).replace('.', ',')
                val trimmed = value.trimEnd('0').trimEnd(',')
                "$trimmed л"
            }
        }
    }

    private fun setBreakfast(meal: Meal) {
        binding.eatingView.apply {
            caloriesBreakfast.isVisible = false
            separatorBreakfast.isVisible = false
            caloriesArgBreakfast.isVisible = false
            resultBreakfast.isVisible = false
            imgResultBreakfast.isVisible = false
            recommendedBreakfast.isVisible = true
            recommendedCaloriesBreakfast.isVisible = true
            recommendedCaloriesBreakfast.text =
                "${meal.calories} ккал"
            buttonAddBreakfast.isVisible = true
            breakfast.setOnClickListener {
                openSearchScreen(meal.mealType)
            }
        }
    }

    private fun setLunch(meal: Meal) {
        binding.eatingView.apply {
            caloriesLunch.isVisible = false
            separatorLunch.isVisible = false
            caloriesArgLunch.isVisible = false
            resultLunch.isVisible = false
            imgResultLunch.isVisible = false
            recommendedLunch.isVisible = true
            recommendedCaloriesLunch.isVisible = true
            recommendedCaloriesLunch.text =
                "${meal.calories} ккал"
            buttonAddLunch.isVisible = true
            lunch.setOnClickListener {
                openSearchScreen(meal.mealType)
            }
        }
    }

    private fun setAfterNoon(meal: Meal) {
        binding.eatingView.apply {
            caloriesAfternoonTea.isVisible = false
            separatorAfternoonTea.isVisible = false
            caloriesArgAfternoonTea.isVisible = false
            resultAfternoonTea.isVisible = false
            imgResultAfternoonTea.isVisible = false
            recommendedAfternoonTea.isVisible = true
            recommendedCaloriesAfternoonTea.isVisible = true
            recommendedCaloriesAfternoonTea.text =
                "${meal.calories} ккал"
            buttonAddAfternoonTea.isVisible = true
            afternoonTea.setOnClickListener {
                openSearchScreen(meal.mealType)
            }
        }
    }

    private fun setDinner(meal: Meal) {
        binding.eatingView.apply {
            caloriesDinner.isVisible = false
            separatorDinner.isVisible = false
            caloriesArgDinner.isVisible = false
            resultDinner.isVisible = false
            imgResultDinner.isVisible = false
            recommendedDinner.isVisible = true
            recommendedCaloriesDinner.isVisible = true
            recommendedCaloriesDinner.text =
                "${meal.calories} ккал"
            buttonAddDinner.isVisible = true
            dinner.setOnClickListener {
                openSearchScreen(meal.mealType)
            }
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
                buttonAddBreakfast.isVisible = false
                viewModel.calculateMealResult(meal.id, meal.mealType)
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
                resultBreakfast.isVisible = false
                imgResultBreakfast.isVisible = false
                buttonAddBreakfast.isVisible = true
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
                buttonAddLunch.isVisible = false
                viewModel.calculateMealResult(meal.id, meal.mealType)
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
                resultLunch.isVisible = false
                imgResultLunch.isVisible = false
                buttonAddLunch.isVisible = true
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
                buttonAddAfternoonTea.isVisible = false
                viewModel.calculateMealResult(meal.id, meal.mealType)
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
                resultAfternoonTea.isVisible = false
                imgResultAfternoonTea.isVisible = false
                buttonAddAfternoonTea.isVisible = true
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
                buttonAddDinner.isVisible = false
                viewModel.calculateMealResult(meal.id, meal.mealType)
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
                resultDinner.isVisible = false
                imgResultDinner.isVisible = false
                buttonAddDinner.isVisible = true
                dinner.setOnClickListener {
                    openSearchScreen(meal.mealType)
                }
            }
        }
    }

    private fun resultBreakfast(result: Result) {
        when (result) {
            Result.NOT_ENOUGH -> setResultBreakfast(
                R.string.not_enough,
                R.drawable.ic_sentiment_neutral
            )

            Result.FINE -> setResultBreakfast(R.string.fine, R.drawable.ic_sentiment_satisfied)
            Result.GREAT -> setResultBreakfast(
                R.string.great,
                R.drawable.ic_sentiment_very_satisfied
            )

            Result.EXCESS -> setResultBreakfast(R.string.excess, R.drawable.ic_mood_bad)
        }
    }

    private fun setResultBreakfast(resultText: Int, resultImg: Int) {
        binding.eatingView.apply {
            resultBreakfast.isVisible = true
            imgResultBreakfast.isVisible = true
            resultBreakfast.setText(resultText)
            imgResultBreakfast.setImageResource(resultImg)
        }
    }

    private fun resultLunch(result: Result) {
        when (result) {
            Result.NOT_ENOUGH -> setResultLunch(
                R.string.not_enough,
                R.drawable.ic_sentiment_neutral
            )

            Result.FINE -> setResultLunch(R.string.fine, R.drawable.ic_sentiment_satisfied)
            Result.GREAT -> setResultLunch(R.string.great, R.drawable.ic_sentiment_very_satisfied)
            Result.EXCESS -> setResultLunch(R.string.excess, R.drawable.ic_mood_bad)
        }
    }

    private fun setResultLunch(resultText: Int, resultImg: Int) {
        binding.eatingView.apply {
            resultLunch.isVisible = true
            imgResultLunch.isVisible = true
            resultLunch.setText(resultText)
            imgResultLunch.setImageResource(resultImg)
        }
    }

    private fun resultAfternoonTea(result: Result) {
        when (result) {
            Result.NOT_ENOUGH -> setResultAfternoonTea(
                R.string.not_enough,
                R.drawable.ic_sentiment_neutral
            )

            Result.FINE -> setResultAfternoonTea(R.string.fine, R.drawable.ic_sentiment_satisfied)
            Result.GREAT -> setResultAfternoonTea(
                R.string.great,
                R.drawable.ic_sentiment_very_satisfied
            )

            Result.EXCESS -> setResultAfternoonTea(R.string.excess, R.drawable.ic_mood_bad)
        }
    }

    private fun setResultAfternoonTea(resultText: Int, resultImg: Int) {
        binding.eatingView.apply {
            resultAfternoonTea.isVisible = true
            imgResultAfternoonTea.isVisible = true
            resultAfternoonTea.setText(resultText)
            imgResultAfternoonTea.setImageResource(resultImg)
        }
    }

    private fun resultDinner(result: Result) {
        when (result) {
            Result.NOT_ENOUGH -> setResultDinner(
                R.string.not_enough,
                R.drawable.ic_sentiment_neutral
            )

            Result.FINE -> setResultDinner(R.string.fine, R.drawable.ic_sentiment_satisfied)
            Result.GREAT -> setResultDinner(R.string.great, R.drawable.ic_sentiment_very_satisfied)
            Result.EXCESS -> setResultDinner(R.string.excess, R.drawable.ic_mood_bad)
        }
    }

    private fun setResultDinner(resultText: Int, resultImg: Int) {
        binding.eatingView.apply {
            resultDinner.isVisible = true
            imgResultDinner.isVisible = true
            resultDinner.setText(resultText)
            imgResultDinner.setImageResource(resultImg)
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
        val formattedDate = currentDate.format(dateFormat)
        val action = NutritionFragmentDirections
            .actionNavigationNutritionToEatingFragment(
                mealId = mealId,
                date = formattedDate
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
            val calDiff = nutritionFull.nutrition.recommendedCalories - nutritionFull.nutrition.calories
            when {
                calDiff > 0 -> {
                    remainingCalories.text = "$calDiff ккал осталось"
                    remainingCalories.setTextColor(
                        MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSecondaryFixed, Color.BLACK)
                    )
                }
                calDiff == 0 -> {
                    remainingCalories.text = "Норма достигнута"
                    remainingCalories.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.see_foam_greene)
                    )
                }
                else -> {
                    remainingCalories.text = "${-calDiff} ккал сверх"
                    remainingCalories.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.red)
                    )
                }
            }

            protein.text = "${nutritionFull.nutrition.proteins.toInt()} г"
            val protDiff = nutritionFull.nutrition.recommendedProteins.toInt() - nutritionFull.nutrition.proteins.toInt()
            when {
                protDiff > 0 -> {
                    remainingProtein.text = "$protDiff г осталось"
                    remainingProtein.setTextColor(
                        MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSecondaryFixed, Color.BLACK)
                    )
                }
                protDiff == 0 -> {
                    remainingProtein.text = "Норма достигнута"
                    remainingProtein.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.see_foam_greene)
                    )
                }
                else -> {
                    remainingProtein.text = "${-protDiff} г сверх"
                    remainingProtein.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.red)
                    )
                }
            }

            fat.text = "${nutritionFull.nutrition.fats.toInt()} г"
            val fatDiff = nutritionFull.nutrition.recommendedFats.toInt() - nutritionFull.nutrition.fats.toInt()
            when {
                fatDiff > 0 -> {
                    remainingFat.text = "$fatDiff г осталось"
                    remainingFat.setTextColor(
                        MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSecondaryFixed, Color.BLACK)
                    )
                }
                fatDiff == 0 -> {
                    remainingFat.text = "Норма достигнута"
                    remainingFat.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.see_foam_greene)
                    )
                }
                else -> {
                    remainingFat.text = "${-fatDiff} г сверх"
                    remainingFat.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.red)
                    )
                }
            }

            carbohydrates.text = "${nutritionFull.nutrition.carbs.toInt()} г"
            val carbDiff = nutritionFull.nutrition.recommendedCarbs.toInt() - nutritionFull.nutrition.carbs.toInt()
            when {
                carbDiff > 0 -> {
                    remainingCarbohydrates.text = "$carbDiff г осталось"
                    remainingCarbohydrates.setTextColor(
                        MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSecondaryFixed, Color.BLACK)
                    )
                }
                carbDiff == 0 -> {
                    remainingCarbohydrates.text = "Норма достигнута"
                    remainingCarbohydrates.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.see_foam_greene)
                    )
                }
                else -> {
                    remainingCarbohydrates.text = "${-carbDiff} г сверх"
                    remainingCarbohydrates.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.red)
                    )
                }
            }
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
            remainingCalories.setTextColor(
                MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSecondaryFixed, Color.BLACK)
            )
            protein.text = "0 г"
            remainingProtein.text = "${nutrition.proteins.toInt()} г осталось"
            remainingProtein.setTextColor(
                MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSecondaryFixed, Color.BLACK)
            )
            fat.text = "0 г"
            remainingFat.text = "${nutrition.fats.toInt()} г осталось"
            remainingFat.setTextColor(
                MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSecondaryFixed, Color.BLACK)
            )
            carbohydrates.text = "0 г"
            remainingCarbohydrates.text = "${nutrition.carbs.toInt()} г осталось"
            remainingCarbohydrates.setTextColor(
                MaterialColors.getColor(requireContext(), com.google.android.material.R.attr.colorOnSecondaryFixed, Color.BLACK)
            )
        }
    }

    private fun setWaterNorm(water: Water) {
        binding.waterView.apply {
            title.text = "0 л"
            recommendedWater.text = formatWaterMlToLiters(water.waterMl)
        }
    }
}
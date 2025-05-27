package com.example.supporthealth.nutrition.statistic.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.supporthealth.R
import com.example.supporthealth.databinding.FragmentStatisticNutritionBinding
import com.example.supporthealth.main.domain.models.MealType
import com.example.supporthealth.main.domain.models.NutritionFull
import com.example.supporthealth.nutrition.main.domain.models.NutrientStat
import com.example.supporthealth.nutrition.main.domain.models.Nutrition
import com.example.supporthealth.nutrition.main.domain.models.Water
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.color.MaterialColors
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class StatisticNutritionFragment : Fragment() {

    companion object {
        fun newInstance() = StatisticNutritionFragment()
    }

    private val viewModel: StatisticNutritionViewModel by viewModel()
    private lateinit var binding: FragmentStatisticNutritionBinding

    private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private var currentDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticNutritionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        subscribeDay()
        binding.toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.day -> {
                        subscribeDay()
                    }

                    R.id.week -> {
                        subscribeWeek()
                    }

                    R.id.month -> {
                        subscribeMonth()
                    }
                }
            }
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

    private fun subscribeDay() {
        setVisible(false)
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
            }
        }
    }

    private fun subscribeWeek() {
        setVisible(true)
        viewModel.observeNutritionDataWeek().observe(viewLifecycleOwner) { nutrition ->
            if (nutrition != null) {
                setNutritionFullWeek(nutrition)
            } else {
                viewModel.observeNutrition().observe(viewLifecycleOwner) { nutrition ->
                    setNutritionNorm(nutrition)
                }
                viewModel.observeWater().observe(viewLifecycleOwner) { water ->
                    setWaterNorm(water)
                }
            }
        }
    }

    private fun subscribeMonth() {
        setVisible(true)
        viewModel.observeNutritionDataMonth().observe(viewLifecycleOwner) { nutrition ->
            if (nutrition != null) {
                setNutritionFullWeek(nutrition)
            } else {
                viewModel.observeNutrition().observe(viewLifecycleOwner) { nutrition ->
                    setNutritionNorm(nutrition)
                }
                viewModel.observeWater().observe(viewLifecycleOwner) { water ->
                    setWaterNorm(water)
                }
            }
        }
    }

    private fun setNutritionFullWeek(nutritionFullWeek: List<NutritionFull>) {

        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val todayNutrition = nutritionFullWeek.find {
            it.nutrition.date == today.format(formatter)
        }

        val filledDays = nutritionFullWeek.filter { it.nutrition.calories > 0 }

        val count = filledDays.size.coerceAtLeast(1)
        val sumProteins = filledDays.sumOf { it.nutrition.proteins.toDouble() }
        val sumFats = filledDays.sumOf { it.nutrition.fats.toDouble() }
        val sumCarbs = filledDays.sumOf { it.nutrition.carbs.toDouble() }

        val avgProteins = sumProteins / count
        val avgFats = sumFats / count
        val avgCarbs = sumCarbs / count

        val totalAvgGrams = avgProteins + avgFats + avgCarbs

        val proteinsProc =
            if (totalAvgGrams.toFloat() == 0f) 0f else (avgProteins / totalAvgGrams).toFloat()
        val fatsProc =
            if (totalAvgGrams.toFloat() == 0f) 0f else (avgFats / totalAvgGrams).toFloat()
        val carbsProc =
            if (totalAvgGrams.toFloat() == 0f) 0f else (avgCarbs / totalAvgGrams).toFloat()

        if (totalAvgGrams.toFloat() == 0f) {
            binding.staticNutrientDonut.updatePercents(listOf(0f, 0f, 0f))
        } else {
            binding.staticNutrientDonut.updatePercents(listOf(fatsProc, proteinsProc, carbsProc))
        }

        fun safePercent(value: Float): Int =
            if (value.isNaN()) 0 else (value * 100).roundToInt()

        binding.nutrientProteins.text = "${safePercent(proteinsProc)}%"
        binding.nutrientProteinsTotal.text = "/ ${avgProteins.toInt()} г"
        binding.nutrientFats.text = "${safePercent(fatsProc)}%"
        binding.nutrientFatsTotal.text = "/ ${avgFats.toInt()} г"
        binding.nutrientCarbs.text = "${safePercent(carbsProc)}%"
        binding.nutrientCarbsTotal.text = "/ ${avgCarbs.toInt()} г"

        val breakfasts =
            nutritionFullWeek.mapNotNull { it.meals.find { meal -> meal.mealType == MealType.BREAKFAST && meal.calories > 0 }?.calories?.toFloat() }
        val lunches =
            nutritionFullWeek.mapNotNull { it.meals.find { meal -> meal.mealType == MealType.LUNCH && meal.calories > 0 }?.calories?.toFloat() }
        val afternoonTeas =
            nutritionFullWeek.mapNotNull { it.meals.find { meal -> meal.mealType == MealType.AFTERNOON_TEA && meal.calories > 0 }?.calories?.toFloat() }
        val dinners =
            nutritionFullWeek.mapNotNull { it.meals.find { meal -> meal.mealType == MealType.DINNER && meal.calories > 0 }?.calories?.toFloat() }

        val avgBreakfast = if (breakfasts.isNotEmpty()) breakfasts.average().toFloat() else 0f
        val avgLunch = if (lunches.isNotEmpty()) lunches.average().toFloat() else 0f
        val avgAfternoonTea =
            if (afternoonTeas.isNotEmpty()) afternoonTeas.average().toFloat() else 0f
        val avgDinner = if (dinners.isNotEmpty()) dinners.average().toFloat() else 0f

        val totalAvgMeals = avgBreakfast + avgLunch + avgAfternoonTea + avgDinner
        val breakfastPercent = if (totalAvgMeals == 0f) 0f else avgBreakfast / totalAvgMeals
        val lunchPercent = if (totalAvgMeals == 0f) 0f else avgLunch / totalAvgMeals
        val afternoonTeaPercent = if (totalAvgMeals == 0f) 0f else avgAfternoonTea / totalAvgMeals
        val dinnerPercent = if (totalAvgMeals == 0f) 0f else avgDinner / totalAvgMeals

        binding.staticCaloriesByDayDonut.updatePercent(
            if (totalAvgMeals == 0f) listOf(0f, 0f, 0f, 0f)
            else listOf(breakfastPercent, dinnerPercent, afternoonTeaPercent, lunchPercent)
        )

        fun percent(part: Float, total: Float): Int =
            if (total == 0f) 0 else ((part / total) * 100).roundToInt()

        binding.nutrientBreakfast.text = "${percent(avgBreakfast, totalAvgMeals)}%"
        binding.nutrientBreakfastTotal.text = "/ ${avgBreakfast.roundToInt()} ккал"
        binding.nutrientLunch.text = "${percent(avgLunch, totalAvgMeals)}%"
        binding.nutrientLunchTotal.text = "/ ${avgLunch.roundToInt()} ккал"
        binding.nutrientAfternoonTea.text = "${percent(avgAfternoonTea, totalAvgMeals)}%"
        binding.nutrientAfternoonTeaTotal.text = "/ ${avgAfternoonTea.roundToInt()} ккал"
        binding.nutrientDinner.text = "${percent(avgDinner, totalAvgMeals)}%"
        binding.nutrientDinnerTotal.text = "/ ${avgDinner.roundToInt()} ккал"

        val filledWater = nutritionFullWeek.filter { it.water.waterMl > 0 }
        val countWater = filledWater.size.coerceAtLeast(1)
        val sumWater = filledWater.sumOf { it.water.waterMl }
        val avgWater = sumWater / countWater
        val targetWater = todayNutrition?.water?.recommendedWaterMl ?: 0
        binding.titleAverageWater.text = requireContext().getString(R.string.average_value)
        binding.averageWater.text = "${avgWater} мл"
        binding.targetWater.text = "${targetWater} мл"

        val sumCalories = filledDays.sumOf { it.nutrition.calories.toDouble() }
        val avgCalories = sumCalories / count
        val targetCalories = todayNutrition?.nutrition?.recommendedCalories

        binding.averageCalories.text = "${avgCalories.toInt()} ккал"
        binding.targetCalories.text = "${targetCalories} ккал"

        binding.caloriesValue.text = "${avgCalories.toInt()} ккал"
        binding.proteinValue.text = "${avgProteins.toInt()} г"
        binding.fatValue.text = "${avgFats.toInt()} г"
        binding.carbohydratesValue.text = "${avgCarbs.toInt()} г"

        showCaloriesWeekChart(binding.barChartCalories, nutritionFullWeek) { requireContext() }
        showWaterWeekChart(binding.barChartWater, nutritionFullWeek) { requireContext() }
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

    private fun setVisible(isVisible: Boolean) {
        binding.statisticView.statisticView.isVisible = !isVisible
        binding.buttonBackDay.isVisible = !isVisible
        binding.buttonForwardDay.isVisible = !isVisible
        binding.calendarDay.isVisible = !isVisible
        binding.buttonCalendar.isVisible = !isVisible
        binding.titleCaloriesIntake.isVisible = isVisible
        binding.titleAverageCalories.isVisible = isVisible
        binding.averageCalories.isVisible = isVisible
        binding.targetCalories.isVisible = isVisible
        binding.titleTargetCalories.isVisible = isVisible
        binding.barChartCalories.isVisible = isVisible
        binding.barChartWater.isVisible = isVisible
    }

    private fun setWaterNorm(water: Water) {
        binding.titleAverageWater.text = "Выпито"
        binding.averageWater.text = "0 мл"
        binding.targetWater.text = "${water.waterMl} мл"
    }

    private fun setNutritionFull(nutritionFull: NutritionFull) {
        binding.statisticView.apply {
            calories.text = "${nutritionFull.nutrition.calories} ккал"
            val calDiff =
                nutritionFull.nutrition.recommendedCalories - nutritionFull.nutrition.calories
            when {
                calDiff > 0 -> {
                    remainingCalories.text = "$calDiff ккал осталось"
                    remainingCalories.setTextColor(
                        MaterialColors.getColor(
                            requireContext(),
                            com.google.android.material.R.attr.colorOnSecondaryFixed,
                            Color.BLACK
                        )
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
            val protDiff =
                nutritionFull.nutrition.recommendedProteins.toInt() - nutritionFull.nutrition.proteins.toInt()
            when {
                protDiff > 0 -> {
                    remainingProtein.text = "$protDiff г осталось"
                    remainingProtein.setTextColor(
                        MaterialColors.getColor(
                            requireContext(),
                            com.google.android.material.R.attr.colorOnSecondaryFixed,
                            Color.BLACK
                        )
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
            val fatDiff =
                nutritionFull.nutrition.recommendedFats.toInt() - nutritionFull.nutrition.fats.toInt()
            when {
                fatDiff > 0 -> {
                    remainingFat.text = "$fatDiff г осталось"
                    remainingFat.setTextColor(
                        MaterialColors.getColor(
                            requireContext(),
                            com.google.android.material.R.attr.colorOnSecondaryFixed,
                            Color.BLACK
                        )
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
            val carbDiff =
                nutritionFull.nutrition.recommendedCarbs.toInt() - nutritionFull.nutrition.carbs.toInt()
            when {
                carbDiff > 0 -> {
                    remainingCarbohydrates.text = "$carbDiff г осталось"
                    remainingCarbohydrates.setTextColor(
                        MaterialColors.getColor(
                            requireContext(),
                            com.google.android.material.R.attr.colorOnSecondaryFixed,
                            Color.BLACK
                        )
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
            staticDonut.updateNutrients(
                listOf(
                    NutrientStat(
                        nutritionFull.nutrition.fats,
                        nutritionFull.nutrition.recommendedFats
                    ),
                    NutrientStat(
                        nutritionFull.nutrition.proteins,
                        nutritionFull.nutrition.recommendedProteins
                    ),
                    NutrientStat(
                        nutritionFull.nutrition.carbs,
                        nutritionFull.nutrition.recommendedCarbs
                    )
                )
            )

            staticDonut.updateCalories(
                nutritionFull.nutrition.calories.toFloat(),
                nutritionFull.nutrition.recommendedCalories.toFloat()
            )
        }
        val totalGrams =
            nutritionFull.nutrition.proteins + nutritionFull.nutrition.fats + nutritionFull.nutrition.carbs
        val proteins = nutritionFull.nutrition.proteins / totalGrams
        val fats = nutritionFull.nutrition.fats / totalGrams
        val carbs = nutritionFull.nutrition.carbs / totalGrams

        if (totalGrams == 0f) {
            binding.staticNutrientDonut.updatePercents(listOf(0f, 0f, 0f))
        } else {
            binding.staticNutrientDonut.updatePercents(listOf(fats, proteins, carbs))
        }

        var totalMeal = nutritionFull.nutrition.calories.toFloat()
        var breakfast = 0f
        var lunch = 0f
        var afternoonTea = 0f
        var dinner = 0f
        nutritionFull.meals.forEach { meal ->
            when (meal.mealType) {
                MealType.BREAKFAST -> breakfast = meal.calories.toFloat()
                MealType.LUNCH -> lunch = meal.calories.toFloat()
                MealType.AFTERNOON_TEA -> afternoonTea = meal.calories.toFloat()
                MealType.DINNER -> dinner = meal.calories.toFloat()
            }
        }

        fun percent(part: Float, total: Float): Int =
            if (total == 0f) 0 else ((part / total) * 100).roundToInt()

        binding.staticCaloriesByDayDonut.updatePercent(
            if (totalMeal == 0f) listOf(0f, 0f, 0f, 0f)
            else listOf(
                breakfast / totalMeal,
                dinner / totalMeal,
                afternoonTea / totalMeal,
                lunch / totalMeal
            )
        )

        binding.nutrientBreakfast.text = "${percent(breakfast, totalMeal)}%"
        binding.nutrientBreakfastTotal.text = "/ ${breakfast.roundToInt()} ккал"
        binding.nutrientLunch.text = "${percent(lunch, totalMeal)}%"
        binding.nutrientLunchTotal.text = "/ ${lunch.roundToInt()} ккал"
        binding.nutrientAfternoonTea.text = "${percent(afternoonTea, totalMeal)}%"
        binding.nutrientAfternoonTeaTotal.text = "/ ${afternoonTea.roundToInt()} ккал"
        binding.nutrientDinner.text = "${percent(dinner, totalMeal)}%"
        binding.nutrientDinnerTotal.text = "/ ${dinner.roundToInt()} ккал"

        fun safePercent(value: Float): Int =
            if (value.isNaN()) 0 else (value * 100).roundToInt()

        binding.nutrientProteins.text = "${safePercent(proteins)}%"
        binding.nutrientProteinsTotal.text = "/ ${nutritionFull.nutrition.proteins.toInt()} г"
        binding.nutrientFats.text = "${safePercent(fats)}%"
        binding.nutrientFatsTotal.text = "/ ${nutritionFull.nutrition.fats.toInt()} г"
        binding.nutrientCarbs.text = "${safePercent(carbs)}%"
        binding.nutrientCarbsTotal.text = "/ ${nutritionFull.nutrition.carbs.toInt()} г"

        binding.titleAverageWater.text = "Выпито"
        binding.averageWater.text = "${nutritionFull.water.waterMl} мл"
        binding.targetWater.text = "${nutritionFull.water.recommendedWaterMl} мл"

        binding.caloriesValue.text = "${nutritionFull.nutrition.calories} ккал"
        binding.proteinValue.text = "${nutritionFull.nutrition.proteins.toInt()} г"
        binding.fatValue.text = "${nutritionFull.nutrition.fats.toInt()} г"
        binding.carbohydratesValue.text = "${nutritionFull.nutrition.carbs.toInt()} г"
    }

    private fun setNutritionNorm(nutrition: Nutrition) {
        binding.statisticView.apply {
            calories.text = "0 ккал"
            remainingCalories.text = "${nutrition.calories} ккал осталось"
            remainingCalories.setTextColor(
                MaterialColors.getColor(
                    requireContext(),
                    com.google.android.material.R.attr.colorOnSecondaryFixed,
                    Color.BLACK
                )
            )
            protein.text = "0 г"
            remainingProtein.text = "${nutrition.proteins.toInt()} г осталось"
            remainingProtein.setTextColor(
                MaterialColors.getColor(
                    requireContext(),
                    com.google.android.material.R.attr.colorOnSecondaryFixed,
                    Color.BLACK
                )
            )
            fat.text = "0 г"
            remainingFat.text = "${nutrition.fats.toInt()} г осталось"
            remainingFat.setTextColor(
                MaterialColors.getColor(
                    requireContext(),
                    com.google.android.material.R.attr.colorOnSecondaryFixed,
                    Color.BLACK
                )
            )
            carbohydrates.text = "0 г"
            remainingCarbohydrates.text = "${nutrition.carbs.toInt()} г осталось"
            remainingCarbohydrates.setTextColor(
                MaterialColors.getColor(
                    requireContext(),
                    com.google.android.material.R.attr.colorOnSecondaryFixed,
                    Color.BLACK
                )
            )
            staticDonut.updateNutrients(
                listOf(
                    NutrientStat(0f, nutrition.fats),
                    NutrientStat(0f, nutrition.proteins),
                    NutrientStat(0f, nutrition.carbs)
                )
            )
        }

        binding.staticNutrientDonut.updatePercents(listOf(0f, 0f, 0f))

        binding.staticCaloriesByDayDonut.updatePercent(listOf(0f, 0f, 0f, 0f))

        binding.nutrientBreakfast.text = "0%"
        binding.nutrientBreakfastTotal.text = "/ 0 ккал"
        binding.nutrientLunch.text = "0%"
        binding.nutrientLunchTotal.text = "/ 0 ккал"
        binding.nutrientAfternoonTea.text = "0%"
        binding.nutrientAfternoonTeaTotal.text = "/ 0 ккал"
        binding.nutrientDinner.text = "0%"
        binding.nutrientDinnerTotal.text = "/ 0 ккал"

        binding.nutrientProteins.text = "0%"
        binding.nutrientProteinsTotal.text = "/ 0 г"
        binding.nutrientFats.text = "0%"
        binding.nutrientFatsTotal.text = "/ 0 г"
        binding.nutrientCarbs.text = "0%"
        binding.nutrientCarbsTotal.text = "/ 0 г"

        binding.caloriesValue.text = "0 ккал"
        binding.proteinValue.text = "0 г"
        binding.fatValue.text = "0 г"
        binding.carbohydratesValue.text = "0 г"
    }

    private fun showCaloriesWeekChart(
        barChart: BarChart,
        nutritionFullWeek: List<NutritionFull>,
        requireContext: () -> android.content.Context
    ) {
        val hasAnyData = nutritionFullWeek.any { it.nutrition.calories > 0 }

        if (nutritionFullWeek.isEmpty() || !hasAnyData) {
            barChart.clear()
            barChart.setNoDataText("Нет данных")
            barChart.setNoDataTextColor(
                MaterialColors.getColor(
                    requireContext(),
                    com.google.android.material.R.attr.colorOnSecondaryFixed,
                    Color.BLACK
                )
            )
            barChart.invalidate()
            return
        }

        val formatter = DateTimeFormatter.ofPattern("dd.MM")
        val parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val sorted = nutritionFullWeek.sortedBy { it.nutrition.date }
        val labels = sorted.map {
            LocalDate.parse(it.nutrition.date, parseFormatter).format(formatter)
        }
        val caloriesList = sorted.map { it.nutrition.calories }

        val entries = caloriesList.mapIndexed { idx, value ->
            BarEntry(idx.toFloat(), value.toFloat())
        }

        val dataSet = BarDataSet(entries, "Калории").apply {
            valueTextColor = MaterialColors.getColor(
                requireContext(),
                com.google.android.material.R.attr.colorOnSecondaryFixed,
                Color.BLACK
            )
            valueTextSize = 12f
            setDrawValues(true)
            color = ContextCompat.getColor(requireContext(), R.color.see_foam_greene)
            axisDependency = YAxis.AxisDependency.RIGHT
        }

        val barData = BarData(dataSet).apply {
            barWidth = 0.5f
        }
        barChart.data = barData
        barChart.setFitBars(true)

        barChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            granularity = 1f
            setDrawLabels(true)
            setDrawGridLines(false)
            textColor = MaterialColors.getColor(
                requireContext(),
                com.google.android.material.R.attr.colorOnSecondaryFixed,
                Color.BLACK
            )
            textSize = 12f
            position = XAxis.XAxisPosition.BOTTOM
        }

        barChart.axisLeft.isEnabled = false

        barChart.axisRight.apply {
            isEnabled = true
            setDrawGridLines(true)
            textColor = MaterialColors.getColor(
                requireContext(),
                com.google.android.material.R.attr.colorOnSecondaryFixed,
                Color.BLACK
            )
            textSize = 12f
            axisMinimum = 0f

            val today = LocalDate.now().format(parseFormatter)
            val todayNutrition = sorted.find { it.nutrition.date == today }
            val target = todayNutrition?.nutrition?.recommendedCalories
                ?: sorted.map { it.nutrition.recommendedCalories }.maxOrNull()
                ?: 2000

            val limitLine = LimitLine(target.toFloat()).apply {
                lineWidth = 2f
                enableDashedLine(16f, 12f, 0f)
                lineColor = ContextCompat.getColor(requireContext(), R.color.see_foam_greene)
                textColor = ContextCompat.getColor(requireContext(), R.color.see_foam_greene)
                textSize = 12f
                labelPosition = LimitLine.LimitLabelPosition.LEFT_TOP
            }

            removeAllLimitLines()
            addLimitLine(limitLine)
        }

        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setScaleEnabled(false)
        barChart.setPinchZoom(false)
        barChart.isDoubleTapToZoomEnabled = false

        barChart.setDragEnabled(false)
        barChart.isHighlightPerDragEnabled = false
        barChart.isHighlightPerTapEnabled = false

        barChart.invalidate()
    }

    private fun showWaterWeekChart(
        barChart: BarChart,
        nutritionFullWeek: List<NutritionFull>,
        requireContext: () -> android.content.Context
    ) {
        val hasAnyData = nutritionFullWeek.any { it.water.waterMl > 0 }

        if (nutritionFullWeek.isEmpty() || !hasAnyData) {
            barChart.clear()
            barChart.setNoDataText("Нет данных")
            barChart.setNoDataTextColor(
                MaterialColors.getColor(
                    requireContext(),
                    com.google.android.material.R.attr.colorOnSecondaryFixed,
                    Color.BLACK
                )
            )
            barChart.invalidate()
            return
        }

        val formatter = DateTimeFormatter.ofPattern("dd.MM")
        val parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val sorted = nutritionFullWeek.sortedBy { it.nutrition.date }
        val labels = sorted.map {
            LocalDate.parse(it.nutrition.date, parseFormatter).format(formatter)
        }
        val waterList = sorted.map { it.water.waterMl }

        val entries = waterList.mapIndexed { idx, value ->
            BarEntry(idx.toFloat(), value.toFloat())
        }

        val dataSet = BarDataSet(entries, "Вода").apply {
            valueTextColor = MaterialColors.getColor(
                requireContext(),
                com.google.android.material.R.attr.colorOnSecondaryFixed,
                Color.BLACK
            )
            valueTextSize = 12f
            setDrawValues(true)
            color = ContextCompat.getColor(requireContext(), R.color.blue)
            axisDependency = YAxis.AxisDependency.RIGHT
        }

        val barData = BarData(dataSet).apply {
            barWidth = 0.5f
        }
        barChart.data = barData
        barChart.setFitBars(true)

        barChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            granularity = 1f
            setDrawLabels(true)
            setDrawGridLines(false)
            textColor = MaterialColors.getColor(
                requireContext(),
                com.google.android.material.R.attr.colorOnSecondaryFixed,
                Color.BLACK
            )
            textSize = 12f
            position = XAxis.XAxisPosition.BOTTOM
        }

        barChart.axisLeft.isEnabled = false

        barChart.axisRight.apply {
            isEnabled = true
            setDrawGridLines(true)
            textColor = MaterialColors.getColor(
                requireContext(),
                com.google.android.material.R.attr.colorOnSecondaryFixed,
                Color.BLACK
            )
            textSize = 12f
            axisMinimum = 0f

            val today = LocalDate.now().format(parseFormatter)
            val todayNutrition = sorted.find { it.nutrition.date == today }
            val target = todayNutrition?.water?.recommendedWaterMl
                ?: sorted.map { it.water.recommendedWaterMl }.maxOrNull()
                ?: 2000

            val limitLine = LimitLine(target.toFloat()).apply {
                lineWidth = 2f
                enableDashedLine(16f, 12f, 0f)
                lineColor = ContextCompat.getColor(requireContext(), R.color.blue)
                textColor = ContextCompat.getColor(requireContext(), R.color.blue)
                textSize = 12f
                labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
            }

            removeAllLimitLines()
            addLimitLine(limitLine)
        }

        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setScaleEnabled(false)
        barChart.setPinchZoom(false)
        barChart.isDoubleTapToZoomEnabled = false

        barChart.setDragEnabled(false)
        barChart.isHighlightPerDragEnabled = false
        barChart.isHighlightPerTapEnabled = false

        barChart.invalidate()
    }
}
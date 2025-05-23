package com.example.supporthealth.activity.statistic.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.supporthealth.R
import com.example.supporthealth.databinding.FragmentStatisticActivityBinding
import com.example.supporthealth.main.domain.models.StepEntity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.tabs.TabLayout
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StatisticActivityFragment : Fragment() {

    companion object {
        fun newInstance() = StatisticActivityFragment()
    }

    private val viewModel: StatisticActivityViewModel by viewModel()
    private lateinit var binding: FragmentStatisticActivityBinding

    private val today = LocalDate.now()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val todayStr = today.format(formatter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.observeStepDate(todayStr).observe(viewLifecycleOwner) { step ->
            binding.steps.text = step.steps.toString()
            binding.targetSteps.text = step.target.toString()
        }

        viewModel.observeSteps().observe(viewLifecycleOwner) { stepsList ->
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE
            val today = LocalDate.now()
            val monday = today.with(DayOfWeek.MONDAY)
            val weekDates = (0..6).map { monday.plusDays(it.toLong()) }

            val stepsPerDay = mutableMapOf<DayOfWeek, Int>()
            val targetsPerDay = mutableMapOf<DayOfWeek, Int>()
            val closedPerDay = mutableMapOf<DayOfWeek, Boolean>()

            for (date in weekDates) {
                val entity = stepsList.find { LocalDate.parse(it.date, formatter) == date }
                val steps = entity?.steps ?: 0
                val target = entity?.target ?: 10000
                stepsPerDay[date.dayOfWeek] = steps
                targetsPerDay[date.dayOfWeek] = target
                closedPerDay[date.dayOfWeek] = steps >= target && target != 0
            }
            binding.statisticWeek.donutProgressMonday.progressMax =
                targetsPerDay[DayOfWeek.MONDAY]?.toFloat() ?: 10000f
            binding.statisticWeek.donutProgressMonday.progress =
                stepsPerDay[DayOfWeek.MONDAY]?.toFloat() ?: 0f
            binding.statisticWeek.iconCompleteMonday.visibility =
                if (closedPerDay[DayOfWeek.MONDAY] == true) View.VISIBLE else View.GONE

            binding.statisticWeek.donutProgressTuesday.progressMax =
                targetsPerDay[DayOfWeek.TUESDAY]?.toFloat() ?: 10000f
            binding.statisticWeek.donutProgressTuesday.progress =
                stepsPerDay[DayOfWeek.TUESDAY]?.toFloat() ?: 0f
            binding.statisticWeek.iconCompleteTuesday.visibility =
                if (closedPerDay[DayOfWeek.TUESDAY] == true) View.VISIBLE else View.GONE

            binding.statisticWeek.donutProgressWednesday.progressMax =
                targetsPerDay[DayOfWeek.WEDNESDAY]?.toFloat() ?: 10000f
            binding.statisticWeek.donutProgressWednesday.progress =
                stepsPerDay[DayOfWeek.WEDNESDAY]?.toFloat() ?: 0f
            binding.statisticWeek.iconCompleteWednesday.visibility =
                if (closedPerDay[DayOfWeek.WEDNESDAY] == true) View.VISIBLE else View.GONE

            binding.statisticWeek.donutProgressThursday.progressMax =
                targetsPerDay[DayOfWeek.THURSDAY]?.toFloat() ?: 10000f
            binding.statisticWeek.donutProgressThursday.progress =
                stepsPerDay[DayOfWeek.THURSDAY]?.toFloat() ?: 0f
            binding.statisticWeek.iconCompleteThursday.visibility =
                if (closedPerDay[DayOfWeek.THURSDAY] == true) View.VISIBLE else View.GONE

            binding.statisticWeek.donutProgressFriday.progressMax =
                targetsPerDay[DayOfWeek.FRIDAY]?.toFloat() ?: 10000f
            binding.statisticWeek.donutProgressFriday.progress =
                stepsPerDay[DayOfWeek.FRIDAY]?.toFloat() ?: 0f
            binding.statisticWeek.iconCompleteFriday.visibility =
                if (closedPerDay[DayOfWeek.FRIDAY] == true) View.VISIBLE else View.GONE

            binding.statisticWeek.donutProgressSaturday.progressMax =
                targetsPerDay[DayOfWeek.SATURDAY]?.toFloat() ?: 10000f
            binding.statisticWeek.donutProgressSaturday.progress =
                stepsPerDay[DayOfWeek.SATURDAY]?.toFloat() ?: 0f
            binding.statisticWeek.iconCompleteSaturday.visibility =
                if (closedPerDay[DayOfWeek.SATURDAY] == true) View.VISIBLE else View.GONE

            binding.statisticWeek.donutProgressSunday.progressMax =
                targetsPerDay[DayOfWeek.SUNDAY]?.toFloat() ?: 10000f
            binding.statisticWeek.donutProgressSunday.progress =
                stepsPerDay[DayOfWeek.SUNDAY]?.toFloat() ?: 0f
            binding.statisticWeek.iconCompleteSunday.visibility =
                if (closedPerDay[DayOfWeek.SUNDAY] == true) View.VISIBLE else View.GONE

            binding.dynamics.tableLayout.apply {
                addTab(newTab().setText("Неделя"))
                addTab(newTab().setText("Месяц"))
                addTab(newTab().setText("Год"))
            }

            val (steps, labels) = getStepsPerWeek(stepsList)
            showPeriodChart(binding.dynamics.barChart, steps, labels, "Шаги за неделю")
            val calories = getCaloriesPerWeekList(stepsList)
            val distances = getDistancePerWeekList(stepsList)
            updateStatsForPeriod(steps, calories, distances, "неделю")

            binding.dynamics.tableLayout.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            val (steps, labels) = getStepsPerWeek(stepsList)
                            showPeriodChart(
                                binding.dynamics.barChart,
                                steps,
                                labels,
                                "Шаги за неделю"
                            )
                            val calories = getCaloriesPerWeekList(stepsList)
                            val distances = getDistancePerWeekList(stepsList)
                            updateStatsForPeriod(steps, calories, distances, "неделю")
                        }

                        1 -> {
                            val (steps, labels) = getStepsPerMonth(stepsList)
                            showPeriodChart(binding.dynamics.barChart, steps, labels, "Шаги за месяц")
                            val calories = getCaloriesPerMonthList(stepsList)
                            val distances = getDistancePerMonthList(stepsList)
                            updateStatsForPeriod(steps, calories, distances, "месяц")
                        }

                        2 -> {
                            val (steps, labels) = getStepsPerYear(stepsList)
                            showPeriodChart(binding.dynamics.barChart, steps, labels, "Шаги за год")
                            val calories = getCaloriesPerYearList(stepsList)
                            val distances = getDistancePerYearList(stepsList)
                            updateStatsForPeriod(steps, calories, distances, "год")
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun showPeriodChart(
        barChart: BarChart,
        stepsPerPeriod: List<Int>,
        labels: List<String>,
        description: String = "Динамика"
    ) {
        val entries = stepsPerPeriod.mapIndexedNotNull { idx, value ->
            if (value > 0) BarEntry(idx.toFloat(), value.toFloat()) else null
        }
        val dataSet = BarDataSet(entries, description).apply {
            valueTextColor = ContextCompat.getColor(requireContext(), R.color.gray_night)
            valueTextSize = 12f
            setDrawValues(true)
            color = Color.WHITE
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
            textColor = ContextCompat.getColor(requireContext(), R.color.gray_night)
            textSize = 12f
            position = XAxis.XAxisPosition.BOTTOM
        }

        barChart.axisLeft.isEnabled = false

        barChart.axisRight.apply {
            isEnabled = true
            setDrawGridLines(true)
            textColor = ContextCompat.getColor(requireContext(), R.color.gray_night)
            textSize = 12f
            axisMinimum = 0f
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

    fun getStepsPerWeek(stepsList: List<StepEntity>): Pair<List<Int>, List<String>> {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val today = LocalDate.now()
        val monday = today.with(DayOfWeek.MONDAY)
        val weekDates = (0..6).map { monday.plusDays(it.toLong()) }

        val stepsPerDay = MutableList(7) { 0 }
        val labels = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")

        weekDates.forEachIndexed { idx, date ->
            val entity = stepsList.find { LocalDate.parse(it.date, formatter) == date }
            stepsPerDay[idx] = entity?.steps ?: 0
        }

        return stepsPerDay to labels
    }

    fun getCaloriesPerWeekList(
        stepsList: List<StepEntity>
    ): List<Int> {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val today = LocalDate.now()
        val monday = today.with(DayOfWeek.MONDAY)
        val weekDates = (0..6).map { monday.plusDays(it.toLong()) }

        return weekDates.map { date ->
            val entity = stepsList.find { LocalDate.parse(it.date, formatter) == date }
            entity?.calories ?: 0
        }
    }

    fun getDistancePerWeekList(
        stepsList: List<StepEntity>
    ): List<Float> {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val today = LocalDate.now()
        val monday = today.with(DayOfWeek.MONDAY)
        val weekDates = (0..6).map { monday.plusDays(it.toLong()) }

        return weekDates.map { date ->
            val entity = stepsList.find { LocalDate.parse(it.date, formatter) == date }
            entity?.distant ?: 0f
        }
    }

    fun getStepsPerMonth(stepsList: List<StepEntity>): Pair<List<Int>, List<String>> {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val today = LocalDate.now()
        val startOfMonth = today.withDayOfMonth(1)
        val daysInMonth = today.lengthOfMonth()
        val monthDates = (0 until daysInMonth).map { startOfMonth.plusDays(it.toLong()) }
        val stepsPerDay = MutableList(daysInMonth) { 0 }

        val formatterLabel = DateTimeFormatter.ofPattern("dd.MM")
        val labels = monthDates.map { it.format(formatterLabel) }

        monthDates.forEachIndexed { idx, date ->
            val entity = stepsList.find { LocalDate.parse(it.date, formatter) == date }
            stepsPerDay[idx] = entity?.steps ?: 0
        }

        return stepsPerDay to labels
    }

    fun getCaloriesPerMonthList(
        stepsList: List<StepEntity>
    ): List<Int> {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val today = LocalDate.now()
        val startOfMonth = today.withDayOfMonth(1)
        val daysInMonth = today.lengthOfMonth()
        val monthDates = (0 until daysInMonth).map { startOfMonth.plusDays(it.toLong()) }

        return monthDates.map { date ->
            val entity = stepsList.find { LocalDate.parse(it.date, formatter) == date }
            entity?.calories ?: 0
        }
    }

    fun getDistancePerMonthList(
        stepsList: List<StepEntity>
    ): List<Float> {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val today = LocalDate.now()
        val startOfMonth = today.withDayOfMonth(1)
        val daysInMonth = today.lengthOfMonth()
        val monthDates = (0 until daysInMonth).map { startOfMonth.plusDays(it.toLong()) }

        return monthDates.map { date ->
            val entity = stepsList.find { LocalDate.parse(it.date, formatter) == date }
            entity?.distant ?: 0f
        }
    }

    fun getStepsPerYear(stepsList: List<StepEntity>): Pair<List<Int>, List<String>> {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val today = LocalDate.now()
        val currentYear = today.year
        val stepsPerMonth = MutableList(12) { 0 }
        val labels = listOf(
            "Янв",
            "Фев",
            "Мар",
            "Апр",
            "Май",
            "Июн",
            "Июл",
            "Авг",
            "Сен",
            "Окт",
            "Ноя",
            "Дек"
        )

        for (month in 1..12) {
            val monthSteps = stepsList
                .filter {
                    val date = LocalDate.parse(it.date, formatter)
                    date.year == currentYear && date.monthValue == month
                }
                .sumOf { it.steps }
            stepsPerMonth[month - 1] = monthSteps
        }

        return stepsPerMonth to labels
    }

    fun getDistancePerYearList(
        stepsList: List<StepEntity>
    ): List<Float> {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val today = LocalDate.now()
        val currentYear = today.year

        return (1..12).map { month ->
            stepsList
                .filter {
                    val date = LocalDate.parse(it.date, formatter)
                    date.year == currentYear && date.monthValue == month
                }
                .sumOf { it.distant.toDouble() }.toFloat()
        }
    }

    fun getCaloriesPerYearList(
        stepsList: List<StepEntity>
    ): List<Int> {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val today = LocalDate.now()
        val currentYear = today.year

        return (1..12).map { month ->
            stepsList
                .filter {
                    val date = LocalDate.parse(it.date, formatter)
                    date.year == currentYear && date.monthValue == month
                }
                .sumOf { it.calories }
        }
    }

    fun updateStatsForPeriod(
        steps: List<Int>,
        calories: List<Int>,
        distances: List<Float>,
        periodText: String
    ) {
        val validSteps = steps.filter { it > 0 }
        val avgSteps = if (validSteps.isNotEmpty()) validSteps.average().toInt() else 0

        val totalDistance = distances.sum()
        val validCalories = calories.filter { it > 0 }
        val avgCalories = if (validCalories.isNotEmpty()) validCalories.average().toInt() else 0

        binding.dynamics.steps.text = pluralizeSteps(avgSteps)
        binding.dynamics.stepsInfo.text = "в среднем"

        binding.dynamics.distant.text = formatDistance(totalDistance)
        binding.dynamics.distantInfo.text = "расстояние за $periodText"

        binding.dynamics.target.text = "${avgCalories} ккал"
        binding.dynamics.targetInfo.text = "в среднем"
    }

    private fun formatDistance(distanceMeters: Float): String {
        return if (distanceMeters < 1000f) {
            "${distanceMeters.toInt()} м"
        } else {
            val distanceKm = distanceMeters / 1000f
            val whole = distanceKm.toInt()
            if (distanceKm % 1f == 0f) {
                "$whole км"
            } else {
                "%.1f км".format(distanceKm)
            }
        }
    }

    private fun pluralizeSteps(count: Int): String {
        val n = count % 100
        val n1 = count % 10
        return when {
            n in 11..14 -> "$count шагов"
            n1 == 1 -> "$count шаг"
            n1 in 2..4 -> "$count шага"
            else -> "$count шагов"
        }
    }
}
package com.example.supporthealth.stress.statistic.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.supporthealth.R
import com.example.supporthealth.databinding.FragmentStatisticMoodBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatisticMoodFragment : Fragment() {

    private val viewModel: StatisticMoodViewModel by viewModel()
    private lateinit var binding: FragmentStatisticMoodBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticMoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // Обработка табов: Настроение / Энергия
        binding.tabType.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showMoodStats()
                    1 -> showEnergyStats()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.tabType.addTab(binding.tabType.newTab().setText("Настроение"))
        binding.tabType.addTab(binding.tabType.newTab().setText("Энергия"))
        binding.tabType.getTabAt(0)?.select()

        showMoodStats()
    }

    private fun showMoodStats() {
        binding.pieChartTitle.text = "Ваше настроение"
        setupLineChart(listOf(2f, 3f, 4f, 3.5f, 5f))
        viewModel.moodCounts.value?.let { setupMoodChart(it) }
        viewModel.moodCounts.value?.let { setupBars(it) }
        viewModel.dayPartMoods.value?.let { setupHistory(it) }
    }

    private fun showEnergyStats() {
        binding.pieChartTitle.text = "Ваша энергия"
        val energyCounts = listOf(0, 1, 3, 2, 1, 0, 0)
        val energyTimes = listOf(2, 3, 1, 1)
        setupLineChart(listOf(1f, 2f, 1.5f, 2.5f, 3f))
        setupMoodChart(energyCounts)
        setupBars(energyCounts)
        setupHistory(energyTimes)
    }

    private fun setupLineChart(values: List<Float>) {
        val entries = values.mapIndexed { index, value -> Entry(index.toFloat(), value) }

        val dataSet = LineDataSet(entries, "").apply {
            color = ContextCompat.getColor(requireContext(), R.color.purple_500)
            lineWidth = 2f
            setDrawCircles(true)
            setDrawValues(false)
        }

        binding.lineChart.data = LineData(dataSet)
        binding.lineChart.description.isEnabled = false
        binding.lineChart.axisRight.isEnabled = false
        binding.lineChart.invalidate()
    }

    private fun setupMoodChart(counts: List<Int>) {
        val moodData = counts.mapIndexed { index, count ->
            count.toFloat() to ContextCompat.getColor(requireContext(), moodColors[index])
        }

        val topIndex = counts.withIndex().maxByOrNull { it.value }?.index ?: 3
        val centerLabel = moodDescriptions[topIndex]
        binding.pieChartView.setChartData(moodData, centerLabel)
    }

    private fun setupBars(counts: List<Int>) {
        val barContainer = binding.root.findViewById<LinearLayout>(R.id.barStatsContainer)
        barContainer.removeAllViews()

        for (i in moodDescriptions.indices) {
            val label = moodDescriptions[i]
            val count = counts.getOrElse(i) { 0 }
            val color = ContextCompat.getColor(requireContext(), moodColors[i])
            val barView = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_bar_stat, barContainer, false)

            val labelView = barView.findViewById<TextView>(R.id.barLabel)
            val progressBar = barView.findViewById<ProgressBar>(R.id.barProgress)

            labelView.text = label
            progressBar.progress = count
            progressBar.progressTintList = ColorStateList.valueOf(color)

            barContainer.addView(barView)
        }
    }

    private fun setupHistory(moodsPerTime: List<Int>) {
        val historyContainer = binding.root.findViewById<LinearLayout>(R.id.historyBlock)
        historyContainer.removeAllViews()

        val timesOfDay = listOf("Утро", "День", "Вечер", "Ночь")
        val bestMoodIndex = moodsPerTime.withIndex().maxByOrNull { it.value }?.index ?: 0

        for ((i, time) in timesOfDay.withIndex()) {
            val isBest = (i == bestMoodIndex)
            val moodIndex = moodsPerTime.getOrElse(i) { 3 }

            val view = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_mood_time_block, historyContainer, false)

            view.findViewById<TextView>(R.id.timeLabel).text = time
            view.findViewById<TextView>(R.id.moodLabel).text = moodDescriptions[moodIndex]
            val color = ContextCompat.getColor(requireContext(), moodColors[moodIndex])
            val circle = view.findViewById<View>(R.id.colorCircle)

            circle.backgroundTintList = ColorStateList.valueOf(color)

            if (isBest) {
                circle.layoutParams.width = 28
                circle.layoutParams.height = 28
                circle.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bg_mood_circle_highlight
                )
            }

            historyContainer.addView(view)
        }
    }

    companion object {
        val moodDescriptions = listOf(
            "Ужасное", "Плохое", "Не очень",
            "Нормальное", "Хорошее", "Отличное", "Восхитительное"
        )

        val moodColors = listOf(
            R.color.mood_terrible, R.color.mood_bad, R.color.mood_okay,
            R.color.mood_normal, R.color.mood_good, R.color.mood_great, R.color.mood_greatest
        )
    }
}

package com.example.supporthealth.stress.statistic.ui

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.supporthealth.R
import com.example.supporthealth.databinding.FragmentStatisticMoodBinding
import com.example.supporthealth.main.domain.models.MoodEntity
import com.example.supporthealth.stress.dialog.domain.DayPart
import com.example.supporthealth.stress.statistic.ui.custom.GradientLineChartRenderer
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.color.MaterialColors
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.roundToInt

class StatisticMoodFragment : Fragment() {

    private val viewModel: StatisticMoodViewModel by viewModel()
    private lateinit var binding: FragmentStatisticMoodBinding

    private val today = LocalDate.now()
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val todayStr = today.format(formatter)

    private var currentPeriod: String = "Неделя"
    private var currentType: String = "настроение"

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

        binding.tableLayout.apply {
            addTab(newTab().setText("Неделя"))
            addTab(newTab().setText("Месяц"))
            addTab(newTab().setText("Все время"))
        }

        binding.tableLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentPeriod = tab?.text?.toString() ?: "Неделя"
                loadAndShowData()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        binding.toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                currentType = when (checkedId) {
                    R.id.stress -> "настроение"
                    R.id.energy -> "энергия"
                    else -> "настроение"
                }
                loadAndShowData()
            }
        }

        binding.tableLayout.getTabAt(0)?.select()
        binding.toggleGroup.check(R.id.stress)
    }

    private fun loadAndShowData() {
        val endDate = today.format(formatter)
        val startDate = when (currentPeriod) {
            "Неделя" -> today.minusDays(6)
            "Месяц" -> today.minusDays(29)
            "Все время" -> LocalDate.of(2000, 1, 1)
            else -> today.minusDays(6)
        }.format(formatter)

        viewModel.observeMoodData(startDate, endDate).observe(viewLifecycleOwner) { data ->
            updateChart(data, currentPeriod.lowercase(), currentType)
            updateDynamics(data, currentType)
            updateDailyPeriodBlocks(data, currentType)
        }
    }

    private fun updateChart(moodData: List<MoodEntity>, period: String, type: String) {
        val entries = mutableListOf<Entry>()

        moodData.forEachIndexed { index, mood ->
            val value = when (type) {
                "энергия" -> mood.energyLevel.toFloat()
                else -> mood.moodLevel.toFloat()
            }
            entries.add(Entry(index.toFloat(), value))
        }

        val label = if (type == "энергия") "Энергия" else "Настроение"

        val dataSet = LineDataSet(entries, label).apply {
            setDrawValues(false)
            lineWidth = 4f
            setDrawCircles(false)
            mode = LineDataSet.Mode.LINEAR
            setDrawFilled(false)
        }

        val lineData = LineData(dataSet)
        binding.lineChart.data = lineData

        binding.lineChart.apply {
            isDragEnabled = true
            setScaleEnabled(false)
            setTouchEnabled(true)
            setVisibleXRangeMaximum(7f)
            moveViewToX((moodData.size - 7).coerceAtLeast(0).toFloat())
        }

        binding.lineChart.xAxis.apply {
            granularity = 1f
            isGranularityEnabled = true
            setLabelCount(7, true)
            setAvoidFirstLastClipping(true)

            textSize = 12f
            textColor = MaterialColors.getColor(
                requireContext(),
                com.google.android.material.R.attr.colorOnSecondaryFixed,
                Color.BLACK
            )
            setDrawGridLines(false)
            gridColor = MaterialColors.getColor(
                requireContext(),
                com.google.android.material.R.attr.colorOnSecondaryFixed,
                Color.BLACK
            )
            position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM

            val dayMonthFormatter = DateTimeFormatter.ofPattern("dd'\u000A'MM", Locale("ru"))

            val firstDate = if (moodData.isNotEmpty()) LocalDate.parse(moodData.first().date, formatter) else null

            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val index = value.roundToInt()
                    if (index < 0 || index >= moodData.size || firstDate == null) return ""

                    val currentDate = LocalDate.parse(moodData[index].date, formatter)

                    val daysSinceFirst = ChronoUnit.DAYS.between(firstDate, currentDate).toInt()

                    return if (period == "месяц" || period == "все время") {
                        if (daysSinceFirst % 4 == 0) currentDate.format(dayMonthFormatter) else ""
                    } else {
                        if (index == 0) currentDate.format(dayMonthFormatter) else {
                            val prevDate = LocalDate.parse(moodData[index - 1].date, formatter)
                            if (currentDate != prevDate) currentDate.format(dayMonthFormatter) else ""
                        }
                    }
                }
            }
        }

        binding.lineChart.axisLeft.apply {
            isEnabled = true
            setDrawGridLines(true)
            setDrawAxisLine(false)
            gridColor = MaterialColors.getColor(
                requireContext(),
                com.google.android.material.R.attr.colorOnSecondaryFixed,
                Color.BLACK
            )
            axisMinimum = -1f
            axisMaximum = 6f
            setDrawLabels(false)
        }

        binding.lineChart.axisRight.isEnabled = false
        binding.lineChart.description.isEnabled = false
        binding.lineChart.legend.isEnabled = false

        binding.lineChart.post {
            val colors = if (type == "энергия") {
                moodData.map {
                    ContextCompat.getColor(requireContext(), viewModel.getEnergyColorResId(it.energyLevel))
                }.toIntArray()
            } else {
                moodData.map {
                    ContextCompat.getColor(requireContext(), viewModel.getMoodColorResId(it.moodLevel))
                }.toIntArray()
            }

            binding.lineChart.renderer = GradientLineChartRenderer(
                binding.lineChart,
                binding.lineChart.animator,
                binding.lineChart.viewPortHandler,
                colors
            )
            binding.lineChart.invalidate()
        }
    }

    private fun updateDynamics(moodData: List<MoodEntity>, type: String) {
        if (moodData.isEmpty()) {
            binding.dynamicsCircle.updatePercents(List(7) { 0f })
            binding.dynamicsList.removeAllViews()
            for (level in 0..6) {
                addDynamicsItem(level, 0f, 0, type)
            }
            return
        }

        val counts = IntArray(7) { 0 }
        val totalCount = moodData.size

        moodData.forEach { mood ->
            val level = when (type) {
                "энергия" -> mood.energyLevel.coerceIn(0, 6)
                else -> mood.moodLevel.coerceIn(0, 6)
            }
            counts[level] += 1
        }

        val percents = counts.map { it.toFloat() / totalCount }

        binding.dynamicsCircle.updatePercents(percents)

        binding.dynamicsList.removeAllViews()

        for (level in 0..6) {
            addDynamicsItem(level, percents[level], counts[level], type)
        }
    }

    private fun addDynamicsItem(level: Int, percent: Float, count: Int, type: String) {
        val context = requireContext()

        val container = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8)
            }
            gravity = Gravity.CENTER_VERTICAL
        }

        val countTextView = TextView(context).apply {
            text = count.toString()
            setTextColor(Color.BLACK)
            textSize = 14f
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = 16
                width = 48
            }
            gravity = Gravity.END
        }

        val barView = View(context).apply {
            setBackgroundColor(
                when (type) {
                    "энергия" -> viewModel.getEnergyColorResId(level)
                    else -> viewModel.getMoodColorResId(level)
                }.let { ContextCompat.getColor(context, it) }
            )
            layoutParams = LinearLayout.LayoutParams(
                0,
                24
            ).apply {
                weight = percent
                width = (percent * 200).toInt().coerceAtLeast(2)
            }
        }

        container.addView(countTextView)
        container.addView(barView)

        binding.dynamicsList.addView(container)
    }

    private fun updateDailyPeriodBlocks(moodData: List<MoodEntity>, type: String) {
        val grouped = moodData.groupBy { it.dayPart }

        fun updateBlock(
            blockId: Int,
            iconId: Int,
            descriptionId: Int,
            percentId: Int,
            periodKey: DayPart
        ) {
            val block = binding.root.findViewById<ConstraintLayout>(blockId)
            val iconView = binding.root.findViewById<ImageView>(iconId)
            val descriptionView = binding.root.findViewById<TextView>(descriptionId)
            val percentView = binding.root.findViewById<TextView>(percentId)

            val data = grouped[periodKey]

            if (data.isNullOrEmpty()) {
                block.visibility = View.GONE
                return
            } else {
                block.visibility = View.VISIBLE
            }

            val avgLevel = data.map {
                if (type == "энергия") it.energyLevel else it.moodLevel
            }.average().toInt().coerceIn(0, 6)

            val descriptionText = if (type == "энергия") viewModel.getEnergyDescription(avgLevel) else viewModel.getMoodDescription(avgLevel)
            val emojiRes = if (type == "энергия") viewModel.getMoodEmojiResId(avgLevel) else viewModel.getMoodEmojiResId(avgLevel)

            val percent = ((data.size.toDouble() / moodData.size) * 100).toInt()

            iconView.setImageResource(emojiRes)
            descriptionView.text = descriptionText
            percentView.text = "$percent%"
        }

        updateBlock(R.id.night, R.id.icon_night, R.id.description_night, R.id.percent_night, DayPart.NIGHT)
        updateBlock(R.id.morning, R.id.icon_morning, R.id.description_morning, R.id.percent_morning, DayPart.MORNING)
        updateBlock(R.id.day, R.id.icon_day, R.id.description_day, R.id.percent_day, DayPart.DAY)
        updateBlock(R.id.evening, R.id.icon_evening, R.id.description_evening, R.id.percent_evening, DayPart.EVENING)
    }
}

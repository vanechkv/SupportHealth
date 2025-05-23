package com.example.supporthealth.nutrition.main.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.supporthealth.R
import com.example.supporthealth.nutrition.main.domain.models.NutrientStat

class MultiDonutView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val centralCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.see_foam_greene_50)
        style = Paint.Style.STROKE
        strokeWidth = 42f
        strokeCap = Paint.Cap.ROUND
    }

    private val centralProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.see_foam_greene)
        style = Paint.Style.STROKE
        strokeWidth = 42f
        strokeCap = Paint.Cap.ROUND
    }

    private val arcPaints = arrayOf(
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.yellow_50)
            strokeWidth = 32f
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        },
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.iced_pink_rose_50)
            strokeWidth = 32f
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        },
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.blue_50)
            strokeWidth = 32f
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
    )

    private val progressPaints = arrayOf(
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.yellow)
            strokeWidth = 32f
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        },
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.iced_pink_rose)
            strokeWidth = 32f
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        },
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.blue)
            strokeWidth = 32f
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
    )

    private val overProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.red)
        strokeWidth = 32f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private var nutrients: List<NutrientStat> = listOf(
        NutrientStat(100f, 200f),
        NutrientStat(200f, 400f),
        NutrientStat(50f, 100f)
    )

    fun updateNutrients(newNutrients: List<NutrientStat>) {
        nutrients = newNutrients
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerPadding = 82f
        val centerRect = RectF(
            centerPadding,
            centerPadding,
            width - centerPadding,
            height - centerPadding
        )
        canvas.drawArc(centerRect, 0f, 360f, false, centralCirclePaint)

        val gap = 22f
        val count = nutrients.size
        val totalArc = 360f
        val totalGap = gap * count
        val arcsLength = totalArc - totalGap

        val recommendedSum = nutrients.sumOf { it.recommended.toDouble() }.toFloat()

        val outerPadding = 22f
        val rect = RectF(
            outerPadding,
            outerPadding,
            width - outerPadding,
            height - outerPadding
        )

        var startAngle = -90f

        for (i in 0 until count) {
            val recommended = nutrients[i].recommended
            val consumed = nutrients[i].value

            val sweep = if (recommendedSum > 0f) arcsLength * (recommended / recommendedSum) else 0f

            canvas.drawArc(rect, startAngle, sweep, false, arcPaints[i])

            val progressSweep = sweep * (consumed / recommended).coerceAtMost(1f)
            canvas.drawArc(rect, startAngle, progressSweep, false, progressPaints[i])

            if (consumed > recommended) {
                val overFraction = ((consumed - recommended) / recommended).coerceAtMost(1f)
                val overSweep = sweep * overFraction
                canvas.drawArc(rect, startAngle, overSweep, false, overProgressPaint)
            }

            startAngle += sweep + gap
        }
    }
}

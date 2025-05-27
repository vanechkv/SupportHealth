package com.example.supporthealth.nutrition.statistic.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.supporthealth.R
import com.google.android.material.color.MaterialColors

class DonutNutrientView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val arcPaints = arrayOf(
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.yellow)
            strokeWidth = 72f
            style = Paint.Style.STROKE
        },
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.iced_pink_rose)
            strokeWidth = 72f
            style = Paint.Style.STROKE
        },
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.blue)
            strokeWidth = 72f
            style = Paint.Style.STROKE
        }
    )

    private val grayPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = MaterialColors.getColor(
            context,
            com.google.android.material.R.attr.colorOnSecondaryFixed,
            Color.BLACK
        )
        strokeWidth = 72f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private var percents: List<Float> = listOf(0.2f, 0.3f, 0.5f)

    fun updatePercents(newPercents: List<Float>) {
        percents = newPercents
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val gap = 6f
        val count = percents.size
        val totalArc = 360f
        val totalGap = gap * count
        val arcsLength = totalArc - totalGap

        val outerPadding = 56f
        val rect = RectF(
            outerPadding,
            outerPadding,
            width - outerPadding,
            height - outerPadding
        )

        if (percents.all { it == 0f }) {
            canvas.drawArc(rect, 0f, 360f, false, grayPaint)
            return
        }

        var startAngle = -90f

        for (i in 0 until count) {
            val percent = percents[i].coerceAtLeast(0f)
            val sweep = arcsLength * percent
            canvas.drawArc(rect, startAngle, sweep, false, arcPaints[i % arcPaints.size])
            startAngle += sweep + gap
        }
    }
}
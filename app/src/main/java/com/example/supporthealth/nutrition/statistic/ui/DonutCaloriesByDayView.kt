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

class DonutCaloriesByDayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val arcPaints = arrayOf(
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.yellow)
            strokeWidth = 142f
            style = Paint.Style.STROKE
        },
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.iced_pink_rose)
            strokeWidth = 142f
            style = Paint.Style.STROKE
        },
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.blue)
            strokeWidth = 142f
            style = Paint.Style.STROKE
        },
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.see_foam_greene)
            strokeWidth = 142f
            style = Paint.Style.STROKE
        }
    )

    private val grayPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = MaterialColors.getColor(
            context,
            com.google.android.material.R.attr.colorOnSecondaryFixed,
            Color.BLACK
        )
        strokeWidth = 142f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private var percents: List<Float> = listOf(0.6f, 0.2f, 0.1f, 0.1f)

    fun updatePercent(newPercent: List<Float>) {
        percents = newPercent
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val width = height / 2
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val stroke = arcPaints[0].strokeWidth
        val padding = stroke / 2
        val radius = height / 2f - padding
        val centerX = 0f
        val centerY = height / 2f

        val rect = RectF(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        val gapAngle = 4f
        val totalGaps = gapAngle * (percents.size - 1)
        val availableSweep = 180f - totalGaps

        if (percents.all { it == 0f }) {
            canvas.drawArc(rect, 270f, 180f, false, grayPaint)
            return
        }

        val percSum = percents.sum().takeIf { it > 0f } ?: 1f
        var startAngle = 270f

        for (i in percents.indices) {
            val sweep = availableSweep * (percents[i] / percSum)
            if (percents[i] > 0f) {
                canvas.drawArc(rect, startAngle, sweep, false, arcPaints[i % arcPaints.size])
            }
            startAngle += sweep + gapAngle
        }
    }
}

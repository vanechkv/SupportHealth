package com.example.supporthealth.stress.statistic.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.supporthealth.R

class CircleChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var data: List<Pair<Float, Int>> = emptyList() // value to color
    private var centerLabel: String = ""
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = RectF()

    /**
     * Устанавливает данные и текст в центре круга
     */
    fun setChartData(data: List<Pair<Float, Int>>, label: String = "") {
        this.data = data
        this.centerLabel = label
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (data.isEmpty()) return

        val total = data.sumOf { it.first.toDouble() }.toFloat()
        var startAngle = -90f
        val size = width.coerceAtMost(height).toFloat()
        val padding = 16f
        rect.set(padding, padding, size - padding, size - padding)

        for ((value, color) in data) {
            val sweepAngle = value / total * 360f
            paint.color = color
            canvas.drawArc(rect, startAngle, sweepAngle, true, paint)
            startAngle += sweepAngle
        }

        // Внутренний круг (фон)
        paint.color = ContextCompat.getColor(context, R.color.bg_layout)
        canvas.drawCircle(width / 2f, height / 2f, size / 4f, paint)

        // Текст в центре (настроение)
        paint.color = ContextCompat.getColor(context, R.color.white)
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 32f
        canvas.drawText(centerLabel, width / 2f, height / 2f + 12f, paint)
    }
}

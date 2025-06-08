package com.example.supporthealth.habits.main.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.supporthealth.R
import kotlin.math.min

class DonutHabitView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    var strokeWidth: Float = 72f
        set(value) {
            field = value
            centralCirclePaint.strokeWidth = value
            centralProgressPaint.strokeWidth = value
            invalidate()
        }

    private val centralCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.see_foam_greene_50)
        style = Paint.Style.STROKE
        strokeWidth = this@DonutHabitView.strokeWidth
        strokeCap = Paint.Cap.ROUND
    }

    private val centralProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.see_foam_greene)
        style = Paint.Style.STROKE
        strokeWidth = this@DonutHabitView.strokeWidth
        strokeCap = Paint.Cap.ROUND
    }


    private var time: Float = 0f
    private var target: Float = 1f

    fun updateSteps(newTime: Float, newTarget: Float) {
        time = newTime
        target = newTarget
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width
        val height = height
        val size = min(width, height)
        val halfStroke = centralCirclePaint.strokeWidth / 2f
        val oval = RectF(
            halfStroke + paddingLeft,
            halfStroke + paddingTop,
            size - halfStroke - paddingRight,
            size - halfStroke - paddingBottom
        )

        canvas.drawArc(oval, 0f, 360f, false, centralCirclePaint)

        if (target > 0f) {
            val progress = time / target
            val angle = (progress.coerceIn(0f, 1f)) * 360f
            canvas.drawArc(oval, -90f, angle, false, centralProgressPaint)
        }
    }

    override fun onAttachedToWindow() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        super.onAttachedToWindow()
    }
}
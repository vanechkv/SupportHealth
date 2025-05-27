package com.example.supporthealth.activity.main.ui

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

class DonutActivityView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val centralCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.see_foam_greene_50)
        style = Paint.Style.STROKE
        strokeWidth = 72f
        strokeCap = Paint.Cap.ROUND
    }

    private val centralProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.see_foam_greene)
        style = Paint.Style.STROKE
        strokeWidth = 72f
        strokeCap = Paint.Cap.ROUND
    }


    private var steps: Int = 0
    private var target: Int = 0

    fun updateSteps(newSteps: Int, newTarget: Int) {
        steps = newSteps
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

        if (target > 0) {
            val progress = steps / target.toFloat()
            val angle = (progress.coerceAtMost(1f)) * 360f

            canvas.drawArc(oval, -90f, angle, false, centralProgressPaint)
        }
    }

    override fun onAttachedToWindow() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        super.onAttachedToWindow()
    }
}
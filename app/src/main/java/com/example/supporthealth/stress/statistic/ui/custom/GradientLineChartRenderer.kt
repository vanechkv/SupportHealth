package com.example.supporthealth.stress.statistic.ui.custom

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class GradientLineChartRenderer(
    chart: LineChart,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler,
    private val colors: IntArray
) : LineChartRenderer(chart, animator, viewPortHandler) {

    override fun drawLinear(c: Canvas, dataSet: ILineDataSet) {
        val entries = (0 until dataSet.entryCount).map { dataSet.getEntryForIndex(it) }
        if (entries.size < 2) return

        val trans = mChart.getTransformer(dataSet.axisDependency)
        val path = Path()

        path.reset()
        for (i in 0 until entries.size) {
            val e = entries[i]
            val pt = floatArrayOf(e.x, e.y)
            trans.pointValuesToPixel(pt)
            if (i == 0) {
                path.moveTo(pt[0], pt[1])
            } else {
                val prev = entries[i - 1]
                val next = if (i + 1 < entries.size) entries[i + 1] else e
                val prevPt = floatArrayOf(prev.x, prev.y)
                val nextPt = floatArrayOf(next.x, next.y)
                trans.pointValuesToPixel(prevPt)
                trans.pointValuesToPixel(nextPt)

                val intensity = 0.2f
                val control1X = prevPt[0] + (pt[0] - prevPt[0]) * intensity
                val control1Y = prevPt[1] + (pt[1] - prevPt[1]) * intensity
                val control2X = pt[0] - (nextPt[0] - prevPt[0]) * intensity
                val control2Y = pt[1] - (nextPt[1] - prevPt[1]) * intensity

                path.cubicTo(control1X, control1Y, control2X, control2Y, pt[0], pt[1])
            }
        }

        val pm = PathMeasure(path, false)
        val length = pm.length
        val segment = 4f

        val pos = FloatArray(2)
        val prevPos = FloatArray(2)
        var distance = 0f

        while (distance < length) {
            if (pm.getPosTan(distance, pos, null)) {
                if (distance > 0) {
                    val t = distance / length
                    val color = interpolateColor(colors, t)
                    mRenderPaint.color = color
                    mRenderPaint.strokeWidth = dataSet.lineWidth
                    mRenderPaint.style = Paint.Style.STROKE
                    c.drawLine(prevPos[0], prevPos[1], pos[0], pos[1], mRenderPaint)
                }
                prevPos[0] = pos[0]
                prevPos[1] = pos[1]
            }
            distance += segment
        }
    }

    private fun interpolateColor(colors: IntArray, t: Float): Int {
        if (colors.size == 1) return colors[0]
        val scaledT = t * (colors.size - 1)
        val idx = scaledT.toInt().coerceIn(0, colors.size - 2)
        val subT = scaledT - idx
        val c0 = colors[idx]
        val c1 = colors[idx + 1]
        return blendColors(c0, c1, subT)
    }

    private fun blendColors(color1: Int, color2: Int, ratio: Float): Int {
        val inverseR = 1f - ratio
        val a = (android.graphics.Color.alpha(color1) * inverseR + android.graphics.Color.alpha(color2) * ratio).toInt()
        val r = (android.graphics.Color.red(color1) * inverseR + android.graphics.Color.red(color2) * ratio).toInt()
        val g = (android.graphics.Color.green(color1) * inverseR + android.graphics.Color.green(color2) * ratio).toInt()
        val b = (android.graphics.Color.blue(color1) * inverseR + android.graphics.Color.blue(color2) * ratio).toInt()
        return android.graphics.Color.argb(a, r, g, b)
    }
}

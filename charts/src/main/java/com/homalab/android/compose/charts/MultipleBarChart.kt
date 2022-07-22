package com.homalab.android.compose.charts

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import com.homalab.android.compose.charts.components.*
import com.homalab.android.compose.charts.entities.BarEntity

@Composable
fun BarChart2(
    modifier: Modifier = Modifier,
    chartData: List<BarChartData2>,
    verticalAxisValues: MutableList<Float> = mutableListOf(),
    verticalAxisLabelTransform: (Float) -> String,
    horizontalAxisOptions: ChartDefaults.AxisOptions = ChartDefaults.defaultAxisOptions(),
    verticalAxisOptions: ChartDefaults.AxisOptions = ChartDefaults.defaultAxisOptions(),
    horizontalLineOptions: ChartDefaults.HorizontalLineOptions = ChartDefaults.defaultHorizontalLineOptions(),
    animationOptions: ChartDefaults.AnimationOptions = ChartDefaults.defaultAnimationOptions(),
    barWidthRatio: Float = DefaultBarWidthRatio,
    contentPadding: Dp = DefaultContentPadding
) {
    if (verticalAxisValues.isEmpty()) verticalAxisValues.addAll(generateVerticaAxisValues(chartData))

    val horizontalAxisThicknessPx = horizontalAxisOptions.axisThickness.toPx()
    val verticalAxisThicknessPx = verticalAxisOptions.axisThickness.toPx()
    val contentPaddingPx = contentPadding.toPx()

    val visibleChartHeight =
        horizontalLineOptions.horizontalLineSpacing * (verticalAxisValues.size - 1)
    val horizontalAxisLabelHeight = contentPadding + horizontalAxisOptions.axisLabelFontSize.toDp()

    val chartHeight = visibleChartHeight + horizontalAxisLabelHeight

    val leftAreaWidth = calculateTextWidth(
        verticalAxisLabelTransform(verticalAxisValues.last()),
        verticalAxisOptions.axisLabelFontSize.toPx()
    ) + contentPaddingPx

    var animatedBars by remember {
        mutableStateOf(listOf<BarEntity>())
    }

    Canvas(modifier = modifier.height(chartHeight)) {
        val verticalAxisLength = visibleChartHeight.toPx()
        val horizontalAxisLength = size.width - leftAreaWidth

        val distanceBetweenVerticalAxisValues = (verticalAxisLength / (verticalAxisValues.size - 1))

        //draw horizontal axis
        drawRect(
            color = horizontalAxisOptions.axisColor,
            topLeft = Offset(leftAreaWidth, verticalAxisLength),
            size = Size(horizontalAxisLength, horizontalAxisThicknessPx)
        )

        //draw vertical axis
        drawRect(
            color = verticalAxisOptions.axisColor,
            topLeft = Offset(leftAreaWidth, 0.0f),
            size = Size(verticalAxisThicknessPx, verticalAxisLength)
        )

        //draw horizontal lines & labels
        val verticalValuesTextPaint = Paint().apply {
            textSize = verticalAxisOptions.axisLabelFontSize.toPx()
            color = verticalAxisOptions.axisLabelColor.toArgb()
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        val horizontalValuesTextPaint = Paint().apply {
            textSize = horizontalAxisOptions.axisLabelFontSize.toPx()
            color = horizontalAxisOptions.axisLabelColor.toArgb()
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        verticalAxisValues.forEachIndexed { index, fl ->
            val x = leftAreaWidth / 2.toFloat()
            val y = verticalAxisLength - (distanceBetweenVerticalAxisValues).times(index)

            drawText(
                verticalAxisLabelTransform(fl),
                x,
                y + verticalAxisOptions.axisLabelFontSize.toPx() / 2,
                verticalValuesTextPaint
            )

            if (horizontalLineOptions.showHorizontalLines && index != 0)
                drawLine(
                    start = Offset(leftAreaWidth, y),
                    end = Offset(leftAreaWidth + horizontalAxisLength, y),
                    color = horizontalLineOptions.horizontalLineColor,
                    strokeWidth = horizontalLineOptions.horizontalLineThickness.toPx(),
                    pathEffect = if (horizontalLineOptions.horizontalLineStyle == HorizontalLineStyle.DASH) PathEffect.dashPathEffect(
                        floatArrayOf(10f, 10f), 5f
                    ) else null
                )
        }

        val minValue = verticalAxisValues.minOf { it }
        val deltaRange = verticalAxisValues.maxOf { it } - minValue

        val rectFs = mutableListOf<BarEntity>()

        var largestLength = 0
        chartData.forEach {
            if (it.values.size > largestLength) largestLength = it.values.size
        }

        val areaWidth = horizontalAxisLength / largestLength

        val calculatedOneAreaWidth = (areaWidth * barWidthRatio)

        for (index in 0 until largestLength) {
            val values = chartData.map { it.values[index] }
            var start = areaWidth * index
            start += leftAreaWidth

            val barWidth = calculatedOneAreaWidth / values.size

            val center = start + areaWidth / 2
            start = center - calculatedOneAreaWidth / 2

            values.forEachIndexed { i, barValue ->

                val startX = start + barWidth * i

                val rect = calculateRect(
                    left = startX,
                    right = startX + barWidth,
                    value = barValue.value,
                    minValue = minValue,
                    deltaRange = deltaRange,
                    verticalAxisLength = verticalAxisLength
                )

                if (animationOptions.isEnabled) {
                    rectFs.add(
                        BarEntity(
                            chartData[i].barColor,
                            Rect(rect.left, rect.top, rect.right, rect.bottom)
                        )
                    )
                } else drawRect(
                    color = chartData[i].barColor,
                    topLeft = rect.topLeft,
                    size = rect.bottomRight.toSize(rect.topLeft)
                )

                drawText(
                    chartData[i].values[index].valueLabel,
                    center,
                    verticalAxisLength + horizontalAxisLabelHeight.toPx(),
                    horizontalValuesTextPaint
                )
            }
        }
        animatedBars = rectFs
    }

    animatedBars.filter { it.rect.size.height > 0 }.forEachIndexed { index, bar ->
        AnimatedBar(
            modifier = modifier.height(chartHeight),
            durationMillis = animationOptions.durationMillis,
            delayMillis = index * animationOptions.durationMillis.toLong(),
            barEntity = bar
        )
    }
}

private fun generateVerticaAxisValues(chartData: List<BarChartData2>): List<Float> {
    val values = mutableListOf<Float>().apply {
        chartData.forEach {
            addAll(it.values.map { barValue ->
                barValue.value
            })
        }
    }
    return generateVerticalValues(values.minOf { it }, values.maxOf { it })
}

private fun calculateRect(
    left: Float,
    right: Float,
    value: Float,
    minValue: Float,
    deltaRange: Float,
    verticalAxisLength: Float
): Rect {
    val deltaValue = value - minValue
    val valuePercent = deltaValue / deltaRange
    val barHeightInPixel = valuePercent * verticalAxisLength
    val top = verticalAxisLength - barHeightInPixel

    return Rect(topLeft = Offset(left, top), Offset(right, verticalAxisLength))
}

data class BarChartData2(
    val values: List<BarValue>,
    val barColor: Color,
    val label: String
)

data class BarValue(
    val value: Float,
    val valueLabel: String,
)
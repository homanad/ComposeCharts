package com.homalab.android.compose.charts

import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import com.homalab.android.compose.charts.components.*
import com.homalab.android.compose.charts.entities.CircleEntity
import com.homalab.android.compose.charts.entities.LineEntity
import com.homalab.android.compose.charts.entities.TextEntity
import kotlinx.coroutines.delay
import kotlin.math.ceil

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    chartData: List<MultipleChartData>,
    verticalAxisValues: MutableList<Float> = mutableListOf(),
    verticalAxisLabelTransform: (Float) -> String,
    horizontalAxisOptions: ChartDefaults.AxisOptions = ChartDefaults.defaultAxisOptions(),
    verticalAxisOptions: ChartDefaults.AxisOptions = ChartDefaults.defaultAxisOptions(),
    horizontalLineOptions: ChartDefaults.HorizontalLineOptions = ChartDefaults.defaultHorizontalLineOptions(),
    circlePointOptions: ChartDefaults.CirclePointOptions = ChartDefaults.defaultCirclePointOptions(),
    animationOptions: ChartDefaults.AnimationOptions = ChartDefaults.defaultAnimationOptions(),
    lineWidth: Dp = DefaultLineWidth,
    contentPadding: Dp = DefaultContentPadding,
) {
    if (verticalAxisValues.isEmpty()) verticalAxisValues.addAll(generateVerticaAxisValues(chartData))

    val visibleChartHeight =
        horizontalLineOptions.horizontalLineSpacing * (verticalAxisValues.size - 1)
    val horizontalAxisLabelHeight = contentPadding + horizontalAxisOptions.axisLabelFontSize.toDp()

    val chartLabelLineHeight = contentPadding + horizontalAxisOptions.axisLabelFontSize.toDp()
    val totalChartLabelHeight =
        chartLabelLineHeight * ceil(chartData.size.toFloat() / MaxChartLabelInOneLine)

    val chartHeight = visibleChartHeight + horizontalAxisLabelHeight + totalChartLabelHeight

    val horizontalAxisLabelFontSizePx = horizontalAxisOptions.axisLabelFontSize.toPx()
    val verticalAxisLabelFontSizePx = verticalAxisOptions.axisLabelFontSize.toPx()

    val contentPaddingPx = contentPadding.toPx()
    val horizontalAxisThicknessPx = horizontalAxisOptions.axisThickness.toPx()
    val verticalAxisThicknessPx = verticalAxisOptions.axisThickness.toPx()

    val horizontalLabelAreaY = (visibleChartHeight + horizontalAxisLabelHeight).toPx()
    val chartLabelAreaBaseY = (visibleChartHeight + horizontalAxisLabelHeight).toPx()

    val leftAreaWidth = calculateTextWidth(
        verticalAxisLabelTransform(verticalAxisValues.last()),
        verticalAxisLabelFontSizePx
    ) + contentPaddingPx

    val chartAnimatable = remember {
        if (animationOptions.isEnabled) Animatable(0f) else Animatable(1f)
    }

    LaunchedEffect(key1 = chartAnimatable, block = {
        if (!animationOptions.isEnabled) return@LaunchedEffect

        delay(animationOptions.delayMillis.toLong())
        chartAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = animationOptions.componentDurationMillis)
        )
    })

    val verticalValuesPaint = Paint().apply {
        textSize = verticalAxisLabelFontSizePx * chartAnimatable.value
        color = verticalAxisOptions.axisLabelColor.toArgb()
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    val horizontalValuesPaint = Paint().apply {
        textSize = horizontalAxisLabelFontSizePx * chartAnimatable.value
        color = horizontalAxisOptions.axisLabelColor.toArgb()
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    var animatedLineEntities by remember {
        mutableStateOf(listOf<LineEntity>())
    }
    var animatedCircles by remember {
        mutableStateOf(listOf<CircleEntity>())
    }

    Canvas(modifier = modifier.height(chartHeight)) {
        val verticalAxisHeight = visibleChartHeight.toPx()
        val horizontalAxisWidth = size.width - leftAreaWidth
        val verticalAxisValuesDistance = (verticalAxisHeight / (verticalAxisValues.size - 1))

        //draw horizontal axis
        drawRect(
            color = horizontalAxisOptions.axisColor,
            topLeft = Offset(leftAreaWidth, verticalAxisHeight),
            size = Size(horizontalAxisWidth * chartAnimatable.value, horizontalAxisThicknessPx)
        )

        //draw vertical axis
        drawRect(
            color = verticalAxisOptions.axisColor,
            topLeft = Offset(
                leftAreaWidth,
                verticalAxisHeight - verticalAxisHeight * chartAnimatable.value
            ),
            size = Size(verticalAxisThicknessPx, verticalAxisHeight * chartAnimatable.value)
        )

        //draw horizontal lines & labels
        verticalAxisValues.forEachIndexed { index, value ->
            val x = leftAreaWidth / 2.toFloat()
            val y = verticalAxisHeight - (verticalAxisValuesDistance).times(index)

            drawText(
                verticalAxisLabelTransform(value),
                x,
                y + verticalAxisLabelFontSizePx / 2,
                verticalValuesPaint
            )

            //don't draw min line
            if (horizontalLineOptions.showHorizontalLines && index != 0)
                drawLine(
                    start = Offset(leftAreaWidth, y),
                    end = Offset(leftAreaWidth + horizontalAxisWidth * chartAnimatable.value, y),
                    color = horizontalLineOptions.horizontalLineColor,
                    strokeWidth = horizontalLineOptions.horizontalLineThickness.toPx(),
                    pathEffect = if (horizontalLineOptions.horizontalLineStyle == HorizontalLineStyle.DASH)
                        PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 5f) else null
                )
        }

        //draw line values
        val minValue = verticalAxisValues.minOf { it }
        val deltaRange = verticalAxisValues.maxOf { it } - minValue

        val circleEntities = mutableListOf<CircleEntity>()
        val textOffsets = mutableListOf<TextEntity>()

        val lineEntities = mutableListOf<LineEntity>()

        chartData.forEachIndexed { i, multipleChartData ->
            var previousOffset: Offset? = null

            val barWidth = horizontalAxisWidth / multipleChartData.values.size

            multipleChartData.values.forEachIndexed { index, multipleChartValue ->
                var x = barWidth * index
                x += leftAreaWidth

                val currentOffset = calculateOffset(
                    x,
                    multipleChartValue.value,
                    minValue,
                    deltaRange,
                    verticalAxisHeight
                )

                val endOffset = Offset((currentOffset.x + barWidth.div(2)), currentOffset.y)

                circleEntities.add(
                    CircleEntity(
                        multipleChartData.dotColor,
                        endOffset,
                        if (circlePointOptions.showCirclePoint) {
                            if (circlePointOptions.upscaleBackCircle)
                                circlePointOptions.baseRatio + (circlePointOptions.upscaleRatioStep * (chartData.size - 1 - i))
                            else circlePointOptions.baseRatio
                        } else 0.5f
                    )
                )

                val newTextEntity = TextEntity(
                    multipleChartValue.label,
                    currentOffset.copy(x = x + barWidth.div(2))
                )
                if (!textOffsets.contains(newTextEntity)) textOffsets.add(newTextEntity)

                previousOffset?.let {
                    val start = Offset(it.x + barWidth.div(2), it.y)

                    if (animationOptions.isEnabled) lineEntities.add(
                        LineEntity(
                            start = start,
                            end = endOffset,
                            color = multipleChartData.lineColor,
                            strokeWidth = lineWidth
                        )
                    ) else drawLine(
                        start = start,
                        end = endOffset,
                        color = multipleChartData.lineColor,
                        strokeWidth = lineWidth.toPx()
                    )
                }
                previousOffset = currentOffset
            }

            val width =
                if (chartData.size >= MaxChartLabelInOneLine) horizontalAxisWidth / MaxChartLabelInOneLine
                else horizontalAxisWidth / chartData.size
            var x = width * (i % MaxChartLabelInOneLine)
            x += leftAreaWidth

            val y = chartLabelAreaBaseY + chartLabelLineHeight.toPx() *
                    ceil((i + 1).toFloat() / MaxChartLabelInOneLine)

            val startRect = x + contentPaddingPx
            val endRect = startRect + width / 4

            val rectTopLeft = Offset(startRect, y - horizontalAxisLabelFontSizePx)
            drawRect(
                color = multipleChartData.lineColor,
                topLeft = rectTopLeft,
                size = Offset(
                    startRect + (endRect - startRect) * chartAnimatable.value,
                    y + horizontalAxisLabelFontSizePx / 2
                ).toSize(rectTopLeft)
            )

            val textWidth =
                calculateTextWidth(multipleChartData.label, verticalAxisLabelFontSizePx)

            drawText(
                multipleChartData.label,
                (endRect + contentPaddingPx + textWidth / 2),
                y,
                horizontalValuesPaint
            )
        }

        if (animationOptions.isEnabled) {
            animatedLineEntities = lineEntities
            animatedCircles = circleEntities
        }

        textOffsets.forEach {
            drawText(it.text, it.offset.x, horizontalLabelAreaY, horizontalValuesPaint)
        }

        if (!animationOptions.isEnabled) circleEntities.forEach {
            drawCircle(
                color = it.color,
                center = it.offset,
                radius = lineWidth.times(it.ratio).toPx()
            )
        }
    }

    animatedLineEntities.forEachIndexed { index, line ->
        AnimatedLine(
            modifier = modifier.height(chartHeight),
            durationMillis = animationOptions.durationMillis,
            delayMillis = (index + 1) * animationOptions.durationMillis + animationOptions.delayMillis,
            lineEntity = line
        )
    }

    animatedCircles.forEachIndexed { index, circleEntity ->
        AnimatedCircle(
            modifier = modifier.height(chartHeight),
            durationMillis = animationOptions.durationMillis,
            delayMillis = index * animationOptions.durationMillis + animationOptions.delayMillis,
            strokeWidth = lineWidth,
            circleEntity = circleEntity
        )
    }
}

private fun generateVerticaAxisValues(chartData: List<MultipleChartData>): List<Float> {
    val values = mutableListOf<Float>().apply {
        chartData.forEach {
            addAll(it.values.map { v -> v.value })
        }
    }
    return generateVerticalValues(values.minOf { it }, values.maxOf { it })
}

private fun calculateOffset(
    x: Float,
    value: Float,
    minValue: Float,
    deltaRange: Float,
    verticalAxisLength: Float
): Offset {
    val deltaValue = value - minValue
    val valuePercent = deltaValue / deltaRange

    val barHeightInPixel = valuePercent * verticalAxisLength
    val y = verticalAxisLength - barHeightInPixel
    return Offset(x, y)
}

data class MultipleChartData(
    val dotColor: Color,
    val lineColor: Color,
    val values: List<MultipleChartValue>,
    val label: String
)

data class MultipleChartValue(
    val label: String,
    val value: Float
)

enum class HorizontalLineStyle {
    DASH, STROKE
}
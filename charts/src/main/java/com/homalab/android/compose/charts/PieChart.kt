package com.homalab.android.compose.charts

import android.text.TextPaint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.homalab.android.compose.charts.components.*
import kotlinx.coroutines.delay
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    chartData: List<PieChartData>,
    pieAnimationOptions: ChartDefaults.PieAnimationOptions = ChartDefaults.defaultPieAnimationOptions(),
    drawStyle: DrawStyle = Fill,
    percentTextOptions: ChartDefaults.PercentTextOptions = ChartDefaults.defaultPercentTextOptions(),
    labelTextFontSize: TextUnit = DefaultAxisLabelFontSize,
    contentPadding: Dp = DefaultContentPadding
) {
    val labelLineHeight = contentPadding * 2 + labelTextFontSize.toDp()
    val totalChartLabelHeight =
        labelLineHeight * ceil(chartData.size.toFloat() / MaxChartLabelInOneLine)

    val angleAnimatable = if (pieAnimationOptions.isEnabled) remember {
        Animatable(0f)
    } else remember {
        Animatable(360f)
    }

    val shiftAnimatable = remember {
        Animatable(0f)
    }

    val textAnimatable = remember {
        Animatable(0f)
    }

    LaunchedEffect(angleAnimatable, block = {
        if (!pieAnimationOptions.isEnabled) return@LaunchedEffect

        delay(pieAnimationOptions.drawDelayMillis.toLong())

        angleAnimatable.animateTo(
            targetValue = 360f,
            animationSpec = tween(
                durationMillis = pieAnimationOptions.angleDurationMillis,
                delayMillis = pieAnimationOptions.angleDelayMillis,
                easing = LinearOutSlowInEasing
            )
        )
    })

    LaunchedEffect(key1 = shiftAnimatable, block = {
        if (!pieAnimationOptions.isEnabled) return@LaunchedEffect

        delay(pieAnimationOptions.drawDelayMillis.toLong())

        shiftAnimatable.animateTo(
            targetValue = pieAnimationOptions.shiftAngle,
            animationSpec = tween(
                durationMillis = pieAnimationOptions.shiftDurationMillis,
                delayMillis = pieAnimationOptions.shiftDelayMillis,
                easing = CubicBezierEasing(0f, 0.75f, 0.35f, 0.85f)
            )
        )
    })

    LaunchedEffect(textAnimatable, block = {
        if (!pieAnimationOptions.isEnabled) return@LaunchedEffect

        delay(pieAnimationOptions.drawDelayMillis.toLong())

        textAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = pieAnimationOptions.angleDurationMillis,
                delayMillis = pieAnimationOptions.angleDelayMillis,
                easing = LinearOutSlowInEasing
            )
        )
    })

    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .padding(bottom = totalChartLabelHeight)
    ) {
        val innerRadius = size.minDimension / 2

        val halfSize = size / 2.0f
        val topLeft = Offset(
            halfSize.width - innerRadius,
            halfSize.height - innerRadius
        )
        val size = Size(innerRadius * 2, innerRadius * 2)
        var startAngle = shiftAnimatable.value - 90f

        val paint = TextPaint().apply {
            isAntiAlias = true
            color = percentTextOptions.textColor.toArgb()
            textSize = textAnimatable.value * percentTextOptions.fontSize.toPx()
        }

        val radius = size.minDimension / 4f

        val total = chartData.sumOf { it.value.toDouble() }

        val centerPoint = Offset(topLeft.x + size.center.x, topLeft.y + size.center.y)

        chartData.forEachIndexed { index, data ->
            val proportion = data.value / total
            val sweep = (proportion * angleAnimatable.value).toFloat()
            drawArc(
                color = data.color,
                startAngle = startAngle,
                sweepAngle = sweep,
                topLeft = topLeft,
                size = size,
                useCenter = true,
                style = drawStyle
            )
            drawContext.canvas.nativeCanvas.run {
                val degree = 360 + startAngle + sweep / 2

                val centerArcX = cos(Math.toRadians(degree.toDouble())) * radius + centerPoint.x
                val centerArcY = sin(Math.toRadians(degree.toDouble())) * radius + centerPoint.y

                val text = String.format("%.2f", proportion * 100) + "%"
                val textWidth = calculateTextWidth(text, percentTextOptions.fontSize.toPx())

                drawText(text, centerArcX.toFloat() - textWidth / 2, centerArcY.toFloat(), paint)
            }
            startAngle += sweep
        }
    }
}

private const val DividerLengthInDegrees = 1.8f

data class PieChartData(
    val label: String,
    val value: Float,
    val color: Color
)
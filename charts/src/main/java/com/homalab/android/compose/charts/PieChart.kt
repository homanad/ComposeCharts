package com.homalab.android.compose.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import kotlinx.coroutines.delay

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    chartData: List<PieChartData>,
    drawStyle: DrawStyle
) {

    val angleAnimatable = remember {
        Animatable(0f)
    }

    val shiftAnimatable = remember {
        Animatable(0f)
    }

    LaunchedEffect(angleAnimatable, block = {
        delay(100)
        angleAnimatable.animateTo(
            targetValue = 360f,
            animationSpec = tween(
                durationMillis = 1000,
                delayMillis = 300,
                easing = LinearOutSlowInEasing
            )
        )
    })

    LaunchedEffect(key1 = shiftAnimatable, block = {
        delay(100)
        shiftAnimatable.animateTo(
            targetValue = 30f,
            animationSpec = tween(
                durationMillis = 1000,
                delayMillis = 300,
                easing = CubicBezierEasing(0f, 0f, 0f, 0f)
            )
        )
    })

    val total = chartData.sumOf { it.value.toDouble() }
    val proportions = chartData.map {
        it.value / total
    }

    Canvas(modifier = modifier.aspectRatio(1f)) {
        val innerRadius = size.minDimension / 2

        val halfSize = size / 2.0f
        val topLeft = Offset(
            halfSize.width - innerRadius,
            halfSize.height - innerRadius
        )
        val size = Size(innerRadius * 2, innerRadius * 2)
        var startAngle = shiftAnimatable.value - 90f

        proportions.forEachIndexed { index, d ->
            val sweep = (d * angleAnimatable.value).toFloat()
            drawArc(
                color = chartData[index].color,
                startAngle = startAngle,
                sweepAngle = sweep,
                topLeft = topLeft,
                size = size,
                useCenter = true,
                style = drawStyle
            )
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
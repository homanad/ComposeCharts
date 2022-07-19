package com.homalab.android.compose.samplecharts.charts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.homalab.android.compose.charts.ChartDefaults
import com.homalab.android.compose.charts.MultipleChartData
import com.homalab.android.compose.charts.MultipleChartValue
import com.homalab.android.compose.charts.MultipleLinesChart
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun SampleLinesChart(modifier: Modifier = Modifier) {
    MultipleLinesChart(
        modifier = modifier,
        chartData = getTestData(),
        verticalAxisValues = generateMinMaxRange(6f, 30f),
        verticalAxisLabelTransform = { it.toString() },
        animationOptions = ChartDefaults.defaultAnimationOptions().copy(true, 600)
    )
}

fun generateMinMaxRange(min: Float, max: Float): List<Float> {
    val minValue = floor(min).toInt()
    val maxValue = ceil(max).toInt()

    val delta = maxValue - minValue
    val step = ceil(delta.toFloat() / MAX_HORIZONTAL_LINE).toInt()

    val list = mutableListOf<Float>()
    var value = minValue - step
    while (value < maxValue) {
        value += step
        list.add(value.toFloat())
    }
    return list
}

const val MAX_HORIZONTAL_LINE = 5

fun getTestData(): List<MultipleChartData> {
    return listOf(
        MultipleChartData(
            dotColor = Color.Green,
            dotRatio = 1.5f,
            label = "line 1",
            lineColor = Color.Green,
            values = listOf(
                MultipleChartValue("label1", 10f),
                MultipleChartValue("label2", 12f),
                MultipleChartValue("label3", 13f),
                MultipleChartValue("label4", 18f),
                MultipleChartValue("label5", 9f),
            )
        ),
        MultipleChartData(
            dotColor = Color.Gray,
            dotRatio = 1.5f,
            label = "line 2",
            lineColor = Color.Gray,
            values = listOf(
                MultipleChartValue("label1", 15f),
                MultipleChartValue("label2", 20f),
                MultipleChartValue("label3", 8f),
                MultipleChartValue("label4", 9f),
                MultipleChartValue("label5", 6f),
            )
        ),
        MultipleChartData(
            dotColor = Color.Blue,
            dotRatio = 1.5f,
            label = "line 3",
            lineColor = Color.Blue,
            values = listOf(
                MultipleChartValue("label1", 30f),
                MultipleChartValue("label2", 28f),
                MultipleChartValue("label3", 24f),
                MultipleChartValue("label4", 20f),
                MultipleChartValue("label5", 25f),
            )
        )
    )
}
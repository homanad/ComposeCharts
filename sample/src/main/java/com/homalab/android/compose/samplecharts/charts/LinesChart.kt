package com.homalab.android.compose.samplecharts.charts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.homalab.android.compose.charts.LineChart
import com.homalab.android.compose.charts.MultipleChartData
import com.homalab.android.compose.charts.MultipleChartValue
import com.homalab.android.compose.charts.components.ChartDefaults
import com.homalab.android.compose.charts.components.generateVerticalValues

@Composable
fun SampleLinesChart(modifier: Modifier = Modifier) {
    LineChart(
        modifier = modifier,
        chartData = getTestData(),
        verticalAxisValues = generateVerticalValues(6f, 30f).toMutableList(),
        verticalAxisLabelTransform = { it.toString() },
        animationOptions = ChartDefaults.defaultAnimationOptions().copy(true, 200)
    )
}

fun getTestData(): List<MultipleChartData> {
    return listOf(
        MultipleChartData(
            dotColor = Color.Green,
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
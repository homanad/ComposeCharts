package com.homalab.android.compose.samplecharts.charts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.homalab.android.compose.charts.BarChart
import com.homalab.android.compose.charts.BarChartData
import com.homalab.android.compose.charts.components.ChartDefaults

@Composable
fun SampleBarChart(modifier: Modifier = Modifier) {
    BarChart(
        modifier = modifier,
        chartData = getChartData(),
        verticalAxisValues = generateMinMaxRange(0f, 25f),
        verticalAxisLabelTransform = { it.toString() },
        animationOptions = ChartDefaults.defaultAnimationOptions().copy(true)
    )
}

fun getChartData(): List<BarChartData> {
    return listOf(
        BarChartData(
            barColor = Color.Blue,
            barValue = 12f,
            label = "label1"
        ),
        BarChartData(
            barColor = Color.Blue,
            barValue = 15f,
            label = "label2"
        ),
        BarChartData(
            barColor = Color.Blue,
            barValue = 13f,
            label = "label3"
        ),
        BarChartData(
            barColor = Color.Blue,
            barValue = 25f,
            label = "label4"
        )
    )
}
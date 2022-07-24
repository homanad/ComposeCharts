package com.homalab.android.compose.samplecharts.charts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.homalab.android.compose.charts.BarChart
import com.homalab.android.compose.charts.BarChartData
import com.homalab.android.compose.charts.BarValue
import com.homalab.android.compose.charts.components.ChartDefaults
import com.homalab.android.compose.charts.components.generateVerticalValues

@Composable
fun SampleBarChart(modifier: Modifier = Modifier) {
    BarChart(
        modifier = modifier,
        chartData = getBarsData(),
        verticalAxisValues = generateVerticalValues(0f, 25f).toMutableList(),
        verticalAxisLabelTransform = { "${it.toInt()} b$" },
        animationOptions = ChartDefaults.defaultAnimationOptions().copy(true, durationMillis = 200)
    )
}

private fun getBarsData(): List<BarChartData> {
    val barData1 = BarChartData(
        values = listOf(
            BarValue(10f, "Jan"),
            BarValue(20f, "Feb"),
            BarValue(15f, "Mar"),
            BarValue(13f, "Apr"),
            BarValue(14f, "May"),
        ),
        barColor = Color.Blue,
        label = "Data 1"
    )
    val barData2 = BarChartData(
        values = listOf(
            BarValue(16f, "Jan"),
            BarValue(12f, "Feb"),
            BarValue(10f, "Mar"),
            BarValue(14f, "Apr"),
            BarValue(11f, "May"),
            BarValue(13f, "Jun"),
        ),
        barColor = Color.Gray,
        label = "Data 2"
    )
    val barData3 = BarChartData(
        values = listOf(
            BarValue(12f, "Jan"),
            BarValue(23f, "Feb"),
            BarValue(11f, "Mar"),
            BarValue(15f, "Apr"),
            BarValue(11f, "May"),
        ),
        barColor = Color.Green,
        label = "Data 3"
    )
    val barData4 = BarChartData(
        values = listOf(
            BarValue(15f, "Jan"),
            BarValue(25f, "Feb"),
            BarValue(17f, "Mar"),
            BarValue(14f, "Apr"),
            BarValue(19f, "May"),
            BarValue(20f, "Jun"),
        ),
        barColor = Color.Magenta,
        label = "Data 4"
    )

    return listOf(barData1, barData2, barData3)
}
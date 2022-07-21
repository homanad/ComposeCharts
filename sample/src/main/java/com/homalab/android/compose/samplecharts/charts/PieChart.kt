package com.homalab.android.compose.samplecharts.charts

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp
import com.homalab.android.compose.charts.PieChart
import com.homalab.android.compose.charts.PieChartData

@Composable
fun SamplePieChart(modifier: Modifier = Modifier) {
    val chartData = listOf(
        PieChartData("label 1", 10f, Color.Green),
        PieChartData("label 2", 20f, Color.Gray),
        PieChartData("label 3", 15f, Color.Magenta),
        PieChartData("label 4", 19f, Color.Yellow),
        PieChartData("label 5", 11f, Color.Cyan),
    )

    PieChart(modifier = modifier.padding(16.dp), chartData = chartData, drawStyle = Fill)
}
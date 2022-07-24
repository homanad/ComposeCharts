package com.homalab.android.compose.samplecharts.charts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.homalab.android.compose.charts.BarChart2
import com.homalab.android.compose.charts.BarChartData2
import com.homalab.android.compose.charts.BarValue
import com.homalab.android.compose.charts.components.ChartDefaults
import com.homalab.android.compose.charts.components.generateVerticalValues

@Composable
fun SampleBarsChart(modifier: Modifier = Modifier) {
    BarChart2(
        modifier = modifier,
        chartData = getBarsData(),
        verticalAxisValues = generateVerticalValues(0f, 25f).toMutableList(),
        verticalAxisLabelTransform = { it.toString() },
        animationOptions = ChartDefaults.defaultAnimationOptions().copy(true, durationMillis = 600)
    )
}

private fun getBarsData(): List<BarChartData2> {
    val barData1 = BarChartData2(
        values = listOf(
            BarValue(10f, "2022/01"),
            BarValue(20f, "2022/02"),
            BarValue(15f, "2022/03"),
            BarValue(13f, "2022/04"),
            BarValue(14f, "2022/05"),
        ),
        barColor = Color.Blue,
        label = "Chart1"
    )
    val barData2 = BarChartData2(
        values = listOf(
            BarValue(16f, "2022/01"),
            BarValue(12f, "2022/02"),
            BarValue(10f, "2022/03"),
            BarValue(14f, "2022/04"),
            BarValue(11f, "2022/05"),
            BarValue(13f, "2022/06"),
        ),
        barColor = Color.Gray,
        label = "Chart2"
    )
    val barData3 = BarChartData2(
        values = listOf(
            BarValue(12f, "2022/01"),
            BarValue(23f, "2022/02"),
            BarValue(11f, "2022/03"),
            BarValue(15f, "2022/04"),
            BarValue(11f, "2022/05"),
        ),
        barColor = Color.Green,
        label = "Chart3"
    )

    return listOf(barData1, barData2, barData3)
}
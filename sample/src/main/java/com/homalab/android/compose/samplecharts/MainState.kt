package com.homalab.android.compose.samplecharts

import androidx.compose.runtime.*

@Stable
class MainState(
    chartDisplay: ChartDisplay,
) {
    var chartDisplay by mutableStateOf(chartDisplay)
}

enum class ChartDisplay {
    LINE, BAR, PIE, BARS
}

@Composable
fun rememberMainState(chartDisplay: ChartDisplay = ChartDisplay.LINE) =
    remember { MainState(chartDisplay) }
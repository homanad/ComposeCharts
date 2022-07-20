package com.homalab.android.compose.samplecharts

import androidx.compose.runtime.*

@Stable
class MainState(
    chartDisplay: ChartDisplay,
) {
    var chartDisplay by mutableStateOf(chartDisplay)
}

enum class ChartDisplay {
    MULTIPLE_LINES, BAR, PIE
}

@Composable
fun rememberMainState(chartDisplay: ChartDisplay = ChartDisplay.MULTIPLE_LINES) =
    remember { MainState(chartDisplay) }
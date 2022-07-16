package com.homalab.android.compose.samplecharts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.homalab.android.compose.samplecharts.charts.LinesChart
import com.homalab.android.compose.samplecharts.ui.theme.SampleChartsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleChartsTheme {
                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                }
                SampleApp()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleApp(mainState: MainState = rememberMainState()) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MainTabs(
                items = ChartDisplay.values().toList(),
                selectedTab = mainState.chartDisplay,
                onTabSelected = { mainState.chartDisplay = it }
            )
        }
    ) { paddingValues ->
        when (mainState.chartDisplay) {
            ChartDisplay.MULTIPLE_LINES -> {
                LinesChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = paddingValues.calculateTopPadding() + 16.dp)
                )
            }
            ChartDisplay.BAR -> {

            }
        }
    }
}
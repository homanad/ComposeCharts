package com.homalab.android.compose.samplecharts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MainTabs(
    modifier: Modifier = Modifier,
    items: List<ChartDisplay>,
    selectedTab: ChartDisplay,
    onTabSelected: (ChartDisplay) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        modifier = modifier,
        contentColor = MaterialTheme.colorScheme.onSurface,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectedTab.ordinal])
                    .fillMaxSize()
                    .padding(4.dp)
                    .border(
                        BorderStroke(
                            4.dp,
                            MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        RoundedCornerShape(16.dp)
                    )
            )
        },
        divider = {}
    ) {
        items.forEachIndexed { index, item ->
            val selected = index == selectedTab.ordinal

            Tab(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(16.dp)),
                selected = selected,
                onClick = { onTabSelected(items[index]) }
            ) {
                Text(
                    text = item.name,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
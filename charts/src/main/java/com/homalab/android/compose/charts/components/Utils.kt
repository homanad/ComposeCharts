package com.homalab.android.compose.charts.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun Dp.toPx() = LocalDensity.current.run { this@toPx.toPx() }

@Composable
fun TextUnit.toPx() = LocalDensity.current.run { this@toPx.toPx() }

@Composable
fun TextUnit.toDp() = LocalDensity.current.run { this@toDp.toDp() }

fun generateVerticalValues(min: Float, max: Float): List<Float> {
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

fun calculateTextWidth(text: String, fontSize: Float): Float {
    return (text.length * fontSize).div(1.75).toFloat()
}

fun Offset.toSize(topLeftOffset: Offset): Size {
    return Size(x - topLeftOffset.x, y - topLeftOffset.y)
}

fun DrawScope.drawText(text: String, x: Float, y: Float, paint: NativePaint) {
    drawContext.canvas.nativeCanvas.drawText(text, x, y, paint)
}

private const val MAX_HORIZONTAL_LINE = 5
package com.homalab.android.compose.charts.components

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.homalab.android.compose.charts.HorizontalLineStyle

val DefaultLineWidth = 4.dp
val DefaultAxisLabelColor = Color(0xFF3D3D3D)
val DefaultAxisColor = Color(0xFF3D3D3D)
val DefaultAxisLabelFontSize = 13.sp
val DefaultAxisThickness = 1.dp
val DefaultContentPadding = 8.dp
val HorizontalLineSpacing = 30.dp
const val DefaultCirclePointRatio = 1.5f
const val DefaultUpscaleRatioStep = 0.5f
const val MaxChartLabelInOneLine = 3
const val DEFAULT_DURATION = 600
const val DEFAULT_DELAY_DURATION = 200
const val DefaultBarWidthRatio = 0.8f

object ChartDefaults {

    fun defaultAnimationOptions() =
        AnimationOptions(
            isEnabled = false,
            durationMillis = DEFAULT_DURATION,
            delayMillis = DEFAULT_DELAY_DURATION,
            componentDurationMillis = DEFAULT_DURATION
        )

    @Immutable
    class AnimationOptions(
        val isEnabled: Boolean,
        val durationMillis: Int,
        val delayMillis: Int,
        val componentDurationMillis: Int
    ) {
        @Stable
        fun copy(
            isEnabled: Boolean = this.isEnabled,
            durationMillis: Int = this.durationMillis,
            delayMillis: Int = this.delayMillis,
            componentDurationMillis: Int = this.componentDurationMillis
        ) = AnimationOptions(isEnabled, durationMillis, delayMillis, componentDurationMillis)
    }

    fun defaultAxisOptions() = AxisOptions(
        axisColor = DefaultAxisColor,
        axisThickness = DefaultAxisThickness,
        axisLabelColor = DefaultAxisLabelColor,
        axisLabelFontSize = DefaultAxisLabelFontSize
    )

    @Immutable
    class AxisOptions(
        val axisColor: Color,
        val axisThickness: Dp,
        val axisLabelColor: Color,
        val axisLabelFontSize: TextUnit
    ) {
        @Stable
        fun copy(
            axisColor: Color = this.axisColor,
            axisThickness: Dp = this.axisThickness,
            axisLabelColor: Color = this.axisLabelColor,
            axisLabelFontSize: TextUnit = this.axisLabelFontSize
        ) = AxisOptions(axisColor, axisThickness, axisLabelColor, axisLabelFontSize)
    }

    fun defaultHorizontalLineOptions() = HorizontalLineOptions(
        showHorizontalLines = true,
        horizontalLineColor = DefaultAxisColor,
        horizontalLineThickness = DefaultAxisThickness,
        horizontalLineSpacing = HorizontalLineSpacing,
        horizontalLineStyle = HorizontalLineStyle.DASH
    )

    @Immutable
    class HorizontalLineOptions(
        val showHorizontalLines: Boolean,
        val horizontalLineColor: Color,
        val horizontalLineThickness: Dp,
        val horizontalLineSpacing: Dp,
        val horizontalLineStyle: HorizontalLineStyle,
    ) {
        @Stable
        fun copy(
            showHorizontalLines: Boolean = this.showHorizontalLines,
            horizontalLineColor: Color = this.horizontalLineColor,
            horizontalLineThickness: Dp = this.horizontalLineThickness,
            horizontalLineSpacing: Dp = this.horizontalLineSpacing,
            horizontalLineStyle: HorizontalLineStyle = this.horizontalLineStyle
        ) = HorizontalLineOptions(
            showHorizontalLines,
            horizontalLineColor,
            horizontalLineThickness,
            horizontalLineSpacing,
            horizontalLineStyle
        )
    }

    fun defaultCirclePointOptions() = CirclePointOptions(
        baseRatio = DefaultCirclePointRatio,
        upscaleBackCircle = false,
        upscaleRatioStep = DefaultUpscaleRatioStep,
        showCirclePoint = true
    )

    @Immutable
    class CirclePointOptions(
        val baseRatio: Float,
        val upscaleBackCircle: Boolean,
        val upscaleRatioStep: Float,
        val showCirclePoint: Boolean
    ) {
        @Stable
        fun copy(
            baseRatio: Float = this.baseRatio,
            upscaleBackCircle: Boolean = this.upscaleBackCircle,
            upscaleRatioStep: Float = this.upscaleRatioStep,
            showCirclePoint: Boolean = this.showCirclePoint
        ) = CirclePointOptions(baseRatio, upscaleBackCircle, upscaleRatioStep, showCirclePoint)
    }

    fun defaultPieAnimationOptions() = PieAnimationOptions(
        isEnabled = true,
        angleDurationMillis = 1000,
        angleDelayMillis = 300,
        isShiftEnabled = true,
        shiftAngle = 90f,
        shiftDurationMillis = 1000,
        shiftDelayMillis = 300,
        drawDelayMillis = 100
    )

    @Immutable
    class PieAnimationOptions(
        val isEnabled: Boolean,
        val angleDurationMillis: Int,
        val angleDelayMillis: Int,
        val isShiftEnabled: Boolean,
        val shiftAngle: Float,
        val shiftDurationMillis: Int,
        val shiftDelayMillis: Int,
        val drawDelayMillis: Int
    ) {
        @Stable
        fun copy(
            isEnabled: Boolean = this.isEnabled,
            angleDurationMillis: Int = this.angleDurationMillis,
            angleDelayMillis: Int = this.angleDelayMillis,
            isShiftEnabled: Boolean = this.isShiftEnabled,
            shiftAngle: Float = this.shiftAngle,
            shiftDurationMillis: Int = this.shiftDurationMillis,
            shiftDelayMillis: Int = this.shiftDelayMillis,
            drawDelayMillis: Int = this.drawDelayMillis
        ) = PieAnimationOptions(
            isEnabled,
            angleDurationMillis,
            angleDelayMillis,
            isShiftEnabled,
            shiftAngle,
            shiftDurationMillis,
            shiftDelayMillis,
            drawDelayMillis
        )
    }

    fun defaultTextOptions() = TextOptions(
        fontSize = DefaultAxisLabelFontSize,
        textColor = DefaultAxisLabelColor
    )

    @Immutable
    class TextOptions(
        val fontSize: TextUnit,
        val textColor: Color
    ) {
        @Stable
        fun copy(fontSize: TextUnit = this.fontSize, textColor: Color = this.textColor) =
            TextOptions(fontSize, textColor)
    }
}
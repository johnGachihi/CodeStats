package me.johngachihi.codestats.mobile.android.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import me.johngachihi.codestats.mobile.android.ui.AppTheme
import kotlin.math.ceil
import kotlin.math.sin
import kotlin.random.Random

private val xAxisLabels = listOf("0", "4", "8", "12", "16", "20", "24")
private const val numOfBars = 48

private const val dataBarDropAnimDuration = 350

typealias YValues = List<Int>

@OptIn(ExperimentalTextApi::class)
@Composable
fun HoursOfDayBarChart(
    yValues: YValues = List(numOfBars) { 0 },
    barColor: Color = MaterialTheme.colors.primary,
    axesColor: Color = Color.Gray,
    axesLabelStyle: TextStyle = TextStyle(color = axesColor, fontSize = 12.sp),
    loading: Boolean = false
) {
    val (internalYValues, dataBarHeightPercentage) =
        rememberChartUpdateAnimation(yValues, dropAnimDuration = dataBarDropAnimDuration)

    val maxYValue = remember(internalYValues) {
        // TODO: Why don't I use derivedState or should I?
        (ceil(internalYValues.max() / 100f) * 100).toInt()
    }

    val textMeasurer = rememberTextMeasurer()

    val (loadingBarHeightPercentage, loadingBarSineWavePhase) =
        rememberLoadingAnimation(loading = loading, riseDelay = dataBarDropAnimDuration)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val xAxisYOffset = 24.dp.toPx()

        val longestYAxisLabel = textMeasurer.measure(
            text = AnnotatedString("100000"),
            style = axesLabelStyle,
            softWrap = false,
            overflow = TextOverflow.Clip
        )
        val yAxisLabelRect = Rect(
            topLeft = Offset(x = canvasWidth - longestYAxisLabel.size.width, y = 0f),
            bottomRight = Offset(x = canvasWidth, y = canvasHeight - xAxisYOffset)
        )
        val xAxisLabelRect = Rect(
            topLeft = Offset(x = 0f, y = canvasHeight - xAxisYOffset),
            bottomRight = Offset(x = canvasWidth - yAxisLabelRect.width, y = 0f)
        )
        val graphRect = Rect(
            topLeft = Offset.Zero,
            bottomRight = xAxisLabelRect.topRight
        )

        val xAxis = Line(start = graphRect.bottomLeft, end = graphRect.bottomRight)

        // x axis line
        drawLine(
            start = xAxis.start,
            end = xAxis.end,
            color = axesColor.copy(alpha = 0.5f),
            strokeWidth = 2f
        )

        // x axis labelling
        val spaceBetweenLabels = graphRect.width / (xAxisLabels.size - 1)
        xAxisLabels.forEachIndexed { idx, label ->
            val labelLayoutResult = textMeasurer.measure(
                text = AnnotatedString(label),
                style = axesLabelStyle,
                softWrap = false,
                overflow = TextOverflow.Clip
            )
            val topLeft = Offset(
                x = (xAxis.start.x + idx * spaceBetweenLabels) - labelLayoutResult.size.width / 2,
                y = xAxis.start.y + 8f
            )
            drawText(textLayoutResult = labelLayoutResult, topLeft = topLeft)
        }

        // y-axis labelling
        val yAxisLabelLayoutResult = textMeasurer.measure(
            text = AnnotatedString(maxYValue.toString()),
            style = TextStyle(color = axesColor, fontSize = 12.sp),
        )
        drawText(
            textLayoutResult = yAxisLabelLayoutResult,
            topLeft = Offset(x = yAxisLabelRect.left + 4.dp.toPx(), y = 0f)
        )

        // Bars
        val barSpacing = 2.dp.toPx()
        val barWidth = (graphRect.width - (barSpacing * (numOfBars - 1))) / (numOfBars)

        internalYValues.forEachIndexed { idx, yValue ->
            val xOffset = idx * (barWidth + barSpacing) + barWidth / 2
            val barHeight =
                dataBarHeightPercentage.value * ((yValue * graphRect.height) / maxYValue)
            val barTop = graphRect.height - barHeight

            drawLine(
                strokeWidth = barWidth,
                start = Offset(x = xOffset, y = graphRect.bottom),
                end = Offset(x = xOffset, y = barTop),
                color = barColor
            )
        }

        // Loading Bars
        repeat(numOfBars) {
            val xOffset = it * (barWidth + barSpacing) + barWidth / 2
            val amplitude = 0.2f
            val angularFreq = 0.1f
            val verticalOffset = 1f
            val scale = graphRect.height / 4
            val barHeight =
                ((amplitude * sin(angularFreq * it + loadingBarSineWavePhase.value) + verticalOffset) * scale * loadingBarHeightPercentage.value)
            val barTop = (graphRect.height - barHeight)

            drawLine(
                strokeWidth = barWidth,
                start = Offset(x = xOffset, y = graphRect.bottom),
                end = Offset(x = xOffset, y = barTop),
                color = axesColor.copy(alpha = 0.2f)
            )
        }

        // top x-axis line
        drawLine(
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = graphRect.right, y = 0f),
            color = axesColor.copy(alpha = 0.2f),
            strokeWidth = 2f
        )

        // mid x-axis line
        drawLine(
            start = Offset(x = 0f, y = graphRect.height / 2),
            end = Offset(x = graphRect.right, y = graphRect.height / 2),
            color = axesColor.copy(alpha = 0.2f),
            strokeWidth = 2f
        )

        ////////// For Debugging
        /*val pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f))

        // Canvas left end line
        drawLine(
            start = Offset(x = 0f + 1, y = 0f),
            end = Offset(x = 0f + 1, y = canvasHeight),
            color = axesColor,
            pathEffect = pathEffect
        )

        // Canvas right end line
        drawLine(
            start = Offset(x = canvasWidth - 1, y = 0f),
            end = Offset(x = canvasWidth - 1, y = canvasHeight),
            color = axesColor,
            pathEffect = pathEffect
        )

        // Canvas bottom end line
        drawLine(
            start = Offset(x = 0f, y = canvasHeight),
            end = Offset(x = canvasWidth, y = canvasHeight),
            color = axesColor,
            pathEffect = pathEffect
        )

        // x-axis midpoint
        drawLine(
            start = Offset(x = xAxis.midpoint.x, y = 0f),
            end = Offset(x = xAxis.midpoint.x, y = canvasHeight),
            color = Color.White,
            pathEffect = pathEffect
        )

        // x-axis right end
        drawLine(
            start = Offset(x = xAxis.end.x, y = 0f),
            end = Offset(x = xAxis.end.x, y = canvasHeight),
            color = Color.White,
            pathEffect = pathEffect
        )*/
        //////// End Remove
    }
}

// TODO: Stop returning Pair. User of this composable may confuse
//       between the two values. Use something with "useful" type-checking

data class ChartUpdateAnimationState(
    val internalYValues: List<Int>,
    val dataBarHeightPercentage: Animatable<Float, AnimationVector1D>
)

@Composable
private fun rememberChartUpdateAnimation(
    yValues: YValues,
    dropAnimDuration: Int
): ChartUpdateAnimationState {
    val (internalYValues, setInternalYValues) = remember { mutableStateOf(yValues) }
    val dataBarHeightPercentage = remember { Animatable(0f) }

    LaunchedEffect(yValues) {
        dataBarHeightPercentage.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = dropAnimDuration,
                easing = LinearOutSlowInEasing
            )
        )
        setInternalYValues(yValues)
        dataBarHeightPercentage.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 350,
                easing = LinearOutSlowInEasing,
            )
        )
    }

    return ChartUpdateAnimationState(internalYValues, dataBarHeightPercentage)
}

data class LoadingAnimationState(
    val loadingBarHeightPercentage: Animatable<Float, AnimationVector1D>,
    val loadingBarSineWavePhase: Animatable<Float, AnimationVector1D>
)

@Composable
private fun rememberLoadingAnimation(loading: Boolean, riseDelay: Int): LoadingAnimationState {
    val loadingBarSineWavePhase = remember { Animatable(0f) }
    val loadingBarHeightPercentage = remember { Animatable(1f) }

    LaunchedEffect(loading) {
        if (loading) {
            launch {
                loadingBarHeightPercentage.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        delayMillis = riseDelay,
                        durationMillis = 350,
                        easing = LinearOutSlowInEasing
                    )
                )
            }

            loadingBarSineWavePhase.animateTo(
                targetValue = -100f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 55000,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            loadingBarHeightPercentage.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 350,
                    easing = LinearOutSlowInEasing
                ),
            )
            // TODO: Check why wave snaps backward before it lowers
            loadingBarSineWavePhase.snapTo(0f)
        }
    }

    return LoadingAnimationState(loadingBarHeightPercentage, loadingBarSineWavePhase)
}

class Line(val start: Offset, val end: Offset) {
    val midpoint: Offset = Offset(x = (end.x - start.x) / 2, y = (end.y - start.y) / 2)
}

@Preview
@Composable
fun PreviewHoursOfDayBarChart() {
    AppTheme {
        Card {
            HoursOfDayBarChart(
                yValues = List(48) {
                    Random.nextInt(0, 100_000)
                }
            )
        }
    }
}

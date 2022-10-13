package me.johngachihi.codestats.mobile.android.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.johngachihi.codestats.mobile.android.AnimatedBarsProgress
import me.johngachihi.codestats.mobile.android.AppTheme
import me.johngachihi.codestats.mobile.android.Line
import kotlin.math.ceil
import kotlin.random.Random

private val xAxisLabels = listOf("0", "4", "8", "12", "16", "20", "24")
private const val numOfBars = 48

@OptIn(ExperimentalTextApi::class)
@Composable
fun HoursOfDayBarChart(
    yValues: List<Int> = List(48) { 0 },
    barColor: Color = MaterialTheme.colors.primary,
    axesColor: Color = Color.Gray,
    axesLabelStyle: TextStyle = TextStyle(color = axesColor, fontSize = 12.sp),
) {
    val maxYValue = remember(yValues) {
        (ceil(yValues.max() / 100f) * 100).toInt()
    }

    val textMeasurer = rememberTextMeasurer()

    val currentState = remember {
        MutableTransitionState(AnimatedBarsProgress.START).apply {
            targetState = AnimatedBarsProgress.END
        }
    }
    val transition = updateTransition(currentState, label = "Appear Anim")
    val sizePercentage by transition.animateFloat(
        label = "Size Anim",
        transitionSpec = {
            tween(
                delayMillis = 500,
                durationMillis = 350,
                easing = LinearOutSlowInEasing
            )
        }
    ) { progress ->
        if (progress == AnimatedBarsProgress.START) {
            0f
        } else {
            1f
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val xAxisYOffset = 24.dp.toPx()

        val yAxisLabelRect = Rect(
            topLeft = Offset(x = canvasWidth - 56.dp.toPx(), y = 0f),
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
            color = axesColor,
            strokeWidth = 4f
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

        yValues.forEachIndexed { idx, yValue ->
            val xOffset = idx * (barWidth + barSpacing) + barWidth / 2
            val barHeight = sizePercentage * ((yValue * graphRect.height) / maxYValue)
            val barTop = graphRect.height - barHeight

            drawLine(
                strokeWidth = barWidth,
                start = Offset(x = xOffset, y = graphRect.bottom),
                end = Offset(x = xOffset, y = barTop),
                color = barColor
            )
        }

        ////////// REMOVE
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f))

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
        )
        //////// End Remove
    }
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

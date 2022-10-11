package me.johngachihi.codestats.mobile.android

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import kotlin.math.sin

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Card(modifier = Modifier.fillMaxWidth(), elevation = 4.dp) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Type count",
                    style = MaterialTheme.typography.subtitle1,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "109,897",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.primary
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Card(modifier = Modifier.fillMaxWidth(), elevation = 4.dp) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Type count",
                    style = MaterialTheme.typography.subtitle1
                )

                Spacer(Modifier.height(8.dp))

                HoursOfDayBarChart()
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun HoursOfDayBarChart(
    barColor: Color = MaterialTheme.colors.primary,
    axesColor: Color = Color.Gray
) {
    val textMeasurer = rememberTextMeasurer()

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

        val axesLabelStyle = TextStyle(color = axesColor, fontSize = 12.sp)

        val xAxis = Line(start = graphRect.bottomLeft, end = graphRect.bottomRight)

        // x axis line
        drawLine(
            start = xAxis.start,
            end = xAxis.end,
            color = axesColor,
            strokeWidth = 4f
        )

        // x axis labels
        val labels = listOf("0", "4", "8", "12", "16", "20", "24")
        val spaceBetweenLabels = graphRect.width / (labels.size - 1)

        labels.forEachIndexed { idx, label ->
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

        // y-axis label
        val yAxisLabelLayoutResult = textMeasurer.measure(
            text = AnnotatedString("90,000"),
            style = TextStyle(color = axesColor, fontSize = 12.sp),
        )

        drawText(
            textLayoutResult = yAxisLabelLayoutResult,
            topLeft = Offset(x = yAxisLabelRect.left + 4.dp.toPx(), y = 0f)
        )

        // Bars
        val numOfBars = 48
        val barSpacing = 2.dp.toPx()
        val barWidth = (graphRect.width - (barSpacing * (numOfBars - 1))) / (numOfBars)

        for (i in 0 until numOfBars) {
            val xOffset = i * (barWidth + barSpacing) + barWidth / 2
            val tempYTop =
                graphRect.height / 2 - (graphRect.height / 2) * sin(i.toFloat() / 3)
            drawLine(
                strokeWidth = barWidth,
                start = Offset(x = xOffset, y = graphRect.bottom),
                end = Offset(x = xOffset, y = tempYTop),
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

class Line(val start: Offset, val end: Offset) {
    val midpoint: Offset = Offset(x = (end.x - start.x) / 2, y = (end.y - start.y) / 2)
}

@Preview
@Composable
fun PreviewHomeScreen() {
    AppTheme {
        HomeScreen()
    }
}
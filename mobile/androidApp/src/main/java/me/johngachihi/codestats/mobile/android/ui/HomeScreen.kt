package me.johngachihi.codestats.mobile.android.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.johngachihi.codestats.mobile.android.ui.components.HoursOfDayBarChart
import kotlin.random.Random

@Composable
fun HomeScreen() {
    val chartData = remember {
        List(48) {
            Random.nextInt(0, 100_000)
        }
    }

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

                HoursOfDayBarChart(yValues = chartData)
            }
        }
    }
}


enum class AnimatedBarsProgress { START, END }

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
package me.johngachihi.codestats.mobile.android

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()) {
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
                Canvas(modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                ) {
                    drawCircle(
                        color = Color.Blue,
                        center = Offset(x = 0f, y = 0f),
                        radius = size.minDimension / 4
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    AppTheme {
        HomeScreen()
    }
}
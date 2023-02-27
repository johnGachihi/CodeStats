package me.johngachihi.codestats.mobile.android.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.johngachihi.codestats.mobile.android.ui.AppTheme
import me.johngachihi.codestats.mobile.android.ui.UiState
import me.johngachihi.codestats.mobile.android.ui.components.HoursOfDayBarChart
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    setAppBarActions: (@Composable (RowScope.() -> Unit)) -> Unit = { },
    homeViewModel: HomeViewModel = koinViewModel()
) {
    val typingStats by remember { homeViewModel.typingStats }
    val day by remember { homeViewModel.day }

    val formattedDay by remember(day) {
        derivedStateOf { formatDate(day) }
    }

    setAppBarActions {
        IconButton(onClick = { homeViewModel.refresh() }) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh"
            )
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { homeViewModel.decDay() }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Previous"
                )
            }
            Text(formattedDay, style = MaterialTheme.typography.body2)
            IconButton(onClick = { homeViewModel.incDay() }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Next"
                )
            }
        }

        Card(modifier = Modifier.fillMaxWidth(), elevation = 4.dp) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Type count",
                    style = MaterialTheme.typography.subtitle1,
                )
                Spacer(Modifier.height(8.dp))
                when (typingStats) {
                    is UiState.Loading -> Text(
                        "...",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onBackground
                    )
                    is UiState.Error -> Text(
                        "Error",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.error
                    )
                    else -> Text(
                        (typingStats as UiState.Success).data.count.toString(),
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Card(modifier = Modifier.fillMaxWidth(), elevation = 4.dp) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Typing distribution",
                    style = MaterialTheme.typography.subtitle1
                )

                Spacer(Modifier.height(16.dp))

                /*TODO: Simplify
                Eg.
                HoursOfDayBarChart(
                    yValues = if (typingStats is UiState.Success) typingStats.data.rate else null,
                    loading = typingStats is UiState.Loading,
                    error = if (typingStats is UiState.Error) typingStats.error else null
                )*/
                when (typingStats) {
                    is UiState.Success -> HoursOfDayBarChart(
                        yValues = (typingStats as UiState.Success<UiTypingStats>).data.rate,
                    )
                    is UiState.Loading -> HoursOfDayBarChart(loading = true)
                    // TODO: Add error state
                    is UiState.Error -> HoursOfDayBarChart()
                }
            }
        }
    }
}

fun formatDate(day: LocalDate): String {
    return if (day.compareTo(LocalDate.now()) == 0)
        "Today"
    else
        day.format(
            DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
        )
}

@Preview
@Composable
fun PreviewHomeScreen() {
    AppTheme {
        HomeScreen()
    }
}
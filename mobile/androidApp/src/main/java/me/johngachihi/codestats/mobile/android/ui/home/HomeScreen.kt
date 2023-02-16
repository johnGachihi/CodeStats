package me.johngachihi.codestats.mobile.android.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.johngachihi.codestats.mobile.android.ui.AppTheme
import me.johngachihi.codestats.mobile.android.ui.components.HoursOfDayBarChart
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    setAppBarActions: (@Composable (RowScope.() -> Unit)) -> Unit = { },
    homeViewModel: HomeViewModel = viewModel()
) {
    val (typingStats, setTypingStats) = remember { homeViewModel.typingStats }
    val (day, setDay) = remember { homeViewModel.day }

    val formattedDay by remember(day) {
        derivedStateOf {
            if (day.compareTo(LocalDate.now()) == 0) "Today" else day.format(
                DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")
            )
        }
    }

    LaunchedEffect(day) {
        setTypingStats(HomeViewModel.TypingStatsDataResult.Loading)
        homeViewModel.refresh()
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
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = {
                setDay(day.minusDays(1))
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Previous"
                )
            }
            Text(formattedDay, style = MaterialTheme.typography.body2)
            IconButton(onClick = {
                setDay(day.plusDays(1))
            }) {
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
                    is HomeViewModel.TypingStatsDataResult.Loading -> Text(
                        "...",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onBackground
                    )
                    is HomeViewModel.TypingStatsDataResult.Error -> Text(
                        "Error",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.error
                    )
                    else -> Text(
                        (typingStats as HomeViewModel.TypingStatsDataResult.Success).typingStats.count.toString(),
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

                HoursOfDayBarChart(
                    yValues = if (typingStats is HomeViewModel.TypingStatsDataResult.Success) {
                        val a: MutableList<Int> = MutableList(48) { 0 }
                        val rate = typingStats.typingStats.rate
                        for (sample in rate) {
                            val index =
                                sample.lowerLimit.hour * 2 + if (sample.lowerLimit.minute == 0) 0 else 1
                            a[index] = sample.count
                        }
                        a
                    } else {
                        MutableList(48) { 0 }
                    },
                    loading = typingStats is HomeViewModel.TypingStatsDataResult.Loading
                )
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
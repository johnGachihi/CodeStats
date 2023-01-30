package me.johngachihi.codestats.mobile.android.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.engine.android.*
import kotlinx.coroutines.launch
import me.johngachihi.codestats.core.Period
import me.johngachihi.codestats.core.TypingStats
import me.johngachihi.codestats.mobile.android.data.TypingStatsClient
import me.johngachihi.codestats.mobile.android.data.createHttpClient
import java.time.LocalDate

class HomeViewModel : ViewModel() {
    sealed class TypingStatsDataResult {
        object Loading : TypingStatsDataResult()
        data class Success(val typingStats: TypingStats) : TypingStatsDataResult()
        data class Error(val error: Throwable) : TypingStatsDataResult()
    }

    var day = mutableStateOf(LocalDate.now())
    var typingStats = mutableStateOf<TypingStatsDataResult>(TypingStatsDataResult.Loading)

    init {
        viewModelScope.launch {
            fetchTypingStats(day.value, Period.Day)
        }
    }

    fun refresh() {
        fetchTypingStats(day.value, Period.Day)
    }

    private fun fetchTypingStats(day: LocalDate, period: Period) {
        viewModelScope.launch {
            typingStats.value = try {
                TypingStatsDataResult.Success(
                    TypingStatsClient(createHttpClient(Android.create())).fetchTypingStats(
                        day = day,
                        period = period
                    )
                )
            } catch (e: Exception) {
                TypingStatsDataResult.Error(e)
            }
        }
    }
}
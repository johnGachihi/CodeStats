package me.johngachihi.codestats.mobile.android.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.johngachihi.codestats.core.Period
import me.johngachihi.codestats.core.TypingRateSample
import me.johngachihi.codestats.core.TypingStats
import me.johngachihi.codestats.mobile.android.data.datastore.UsernamePref
import me.johngachihi.codestats.mobile.android.data.net.fetchTypingStats
import me.johngachihi.codestats.mobile.android.ui.UiState
import java.time.LocalDate

class HomeViewModel(usernamePref: UsernamePref) : ViewModel() {
    private val _day = mutableStateOf(LocalDate.now())
    private val dayStateFlow = snapshotFlow { _day.value }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _day.value
        )
    private val _typingStats = mutableStateOf<UiState<UiTypingStats>>(UiState.Loading)
    private val username = usernamePref.username
        .mapLatest { UiState.Success(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UiState.Loading
        )

    val day: State<LocalDate> = _day
    val typingStats: State<UiState<UiTypingStats>> = _typingStats

    init {
        viewModelScope.launch {
            dayStateFlow.combine(username) { day, username -> day to username }
                .collectLatest {
                    if (it.second is UiState.Success)
                        loadTypingStats()
                    else if (it.second is UiState.Loading)
                        _typingStats.value = UiState.Loading
                }
        }
    }

    fun refresh() = viewModelScope.launch {
        loadTypingStats()
    }

    fun incDay() {
        _day.value = _day.value.plusDays(1)
    }

    fun decDay() {
        _day.value = _day.value.minusDays(1)
    }

    private suspend fun loadTypingStats() {
        assert(username.value is UiState.Success) {
            "Loading data before username is retrieved"
        }

        _typingStats.value = UiState.Loading
        _typingStats.value = try {
            UiState.Success(
                formatTypingStats(
                    fetchTypingStats(
                        _day.value,
                        Period.Day,
                        (username.value as UiState.Success).data
                    )
                )
            )
        } catch (e: Exception) {
            UiState.Error(e)
        }
    }

    private fun formatTypingStats(typingStats: TypingStats): UiTypingStats {
        fun formatTypingRate(typingRate: List<TypingRateSample>): List<Int> {
            val buf: MutableList<Int> = MutableList(48) { 0 }
            for (sample in typingRate) {
                val index =
                    sample.lowerLimit.hour * 2 + if (sample.lowerLimit.minute == 0) 0 else 1
                buf[index] = sample.count
            }
            return buf
        }

        return UiTypingStats(
            count = typingStats.count,
            rate = formatTypingRate(typingStats.rate)
        )
    }
}
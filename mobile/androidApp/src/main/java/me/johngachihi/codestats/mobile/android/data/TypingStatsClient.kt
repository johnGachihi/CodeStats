package me.johngachihi.codestats.mobile.android.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import me.johngachihi.codestats.core.Period
import me.johngachihi.codestats.core.TypingStats
import java.time.LocalDate

class TypingStatsClient(private val httpClient: HttpClient) {

    suspend fun fetchTypingStats(day: LocalDate, period: Period): TypingStats {
        return httpClient.get("${Constants.BaseUrl}/activity/typing") {
            url {
                appendPathSegments(day.toString(), period.toString())
            }
        }.body()
    }
}
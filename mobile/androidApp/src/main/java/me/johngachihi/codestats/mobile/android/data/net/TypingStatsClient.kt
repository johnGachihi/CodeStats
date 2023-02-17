package me.johngachihi.codestats.mobile.android.data.net

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import me.johngachihi.codestats.core.Period
import me.johngachihi.codestats.core.TypingStats
import java.time.LocalDate

suspend fun fetchTypingStats(day: LocalDate, period: Period, httpClient: HttpClient = client): TypingStats {
    return httpClient.get("${Constants.BaseUrl}/activity/typing") {
        url {
            appendPathSegments(day.toString(), period.toString())
        }
    }.body()
}
package me.johngachihi.codestats.mobile.android.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import me.johngachihi.codestats.core.TypingStats

class TypingStatsClient(private val httpClient: HttpClient) {

    suspend fun fetchTypingStats(): TypingStats {
        return httpClient.get("${Constants.BaseUrl}/activity/typing").body()
    }
}
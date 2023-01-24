package me.johngachihi.codestats.mobile.android.data

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.johngachihi.codestats.core.TypingRateSample
import me.johngachihi.codestats.core.TypingStats
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

internal class TypingStatsClientTest {
    @Test
    fun `sends correct http request`() = runTest {
        val mockServer = MockEngine {
            respond(
                content = Json.encodeToString(TypingStats(12345, listOf(
                    TypingRateSample(1, LocalDateTime.now())
                ))),
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val httpClient = createHttpClient(mockServer)

        TypingStatsClient(httpClient).fetchTypingStats()

        mockServer.requestHistory.first().run {
            assertEquals("${Constants.BaseUrl}/activity/typing", url.toString())
            assertEquals(HttpMethod.Get, method)
        }
    }

    @Test
    fun `when successful returns TypingStats`() = runTest {
        val mockServer = MockEngine {
            respond(
                content = Json.encodeToString(TypingStats(12345, emptyList())),
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val httpClient = createHttpClient(mockServer)

        TypingStatsClient(httpClient).fetchTypingStats().run {
            assertEquals(12345, count)
            assertEquals(rate, emptyList<TypingRateSample>())
        }
    }
}
package me.johngachihi.codestats.mobile.android.data.net

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import me.johngachihi.codestats.core.Period
import me.johngachihi.codestats.core.TypingRateSample
import me.johngachihi.codestats.core.TypingStats
import org.junit.Assert
import org.junit.Test
import respondJson
import java.time.LocalDate

internal class TypingStatsTest {
    @Test
    fun `sends correct http request`() = runTest {
        val mockServer = MockEngine {
            respondJson(TypingStats(12345, emptyList()))
        }
        val httpClient = createHttpClient(mockServer)

        fetchTypingStats(
            day = LocalDate.of(2021, 1, 1),
            period = Period.Week,
            httpClient = httpClient
        )

        mockServer.requestHistory.first().run {
            Assert.assertEquals(
                "${Constants.BaseUrl}/activity/typing/2021-01-01/Week",
                url.toString()
            )
            Assert.assertEquals(HttpMethod.Get, method)
        }
    }

    @Test
    fun `when successful returns TypingStats`() = runTest {
        val dayToFetch = LocalDate.of(2021, 1, 1)

        val expectedTypingStats = TypingStats(
            count = 12345,
            rate = listOf(
                TypingRateSample(1, dayToFetch.atTime(12, 0))
            )
        )
        val mockServer = MockEngine { respondJson(expectedTypingStats) }
        val httpClient = createHttpClient(mockServer)

        val actualTypingStats = fetchTypingStats(
            day = LocalDate.of(2021, 1, 1),
            period = Period.Day,
            httpClient = httpClient
        )

        Assert.assertEquals(expectedTypingStats, actualTypingStats)
    }
}
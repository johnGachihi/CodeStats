package me.johngachihi.codestats.intellijplugin

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import me.johngachihi.codestats.core.CodingEvent
import me.johngachihi.codestats.core.CodingEventType
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Instant

class MockApiClient : ApiClient {
    data class Request(val body: String)

    val requests = mutableListOf<Request>()

    override suspend fun sendRequest(path: String, method: String, jsonBody: String): String {
        requests.add(Request(jsonBody))
        return ""
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
internal class RemoteLoggerTest {

    @Test
    fun `makes request when burst size is reached, with appropriate request body`() = runTest {
        val mockApiClient = MockApiClient()
        val remoteLogger = RemoteLogger(mockApiClient, burstSize = 3)

        val burstOneEvents = listOf(
            createCharTypedEvent('a'),
            createCharTypedEvent('b'),
            createCharTypedEvent('c')
        )

        remoteLogger.logAsync(burstOneEvents[0]).await()
        assertThat(mockApiClient.requests).hasSize(0)

        remoteLogger.logAsync(burstOneEvents[1]).await()
        assertThat(mockApiClient.requests).hasSize(0)

        remoteLogger.logAsync(burstOneEvents[2]).await()
        assertThat(mockApiClient.requests).hasSize(1)
        assertThat(mockApiClient.requests.first().body).isEqualTo(
            Json.encodeToString(
                ListSerializer(CodingEventJsonSerializer),
                burstOneEvents
            )
        )

        val burstTwoEvents = listOf(
            createCharTypedEvent('d'),
            createCharTypedEvent('e'),
            createCharTypedEvent('f')
        )

        remoteLogger.logAsync(burstTwoEvents[0]).await()
        assertThat(mockApiClient.requests).hasSize(1)

        remoteLogger.logAsync(burstTwoEvents[1]).await()
        assertThat(mockApiClient.requests).hasSize(1)

        remoteLogger.logAsync(burstTwoEvents[2]).await()
        assertThat(mockApiClient.requests).hasSize(2)
        assertThat(mockApiClient.requests[1].body).isEqualTo(
            Json.encodeToString(
                ListSerializer(CodingEventJsonSerializer),
                burstTwoEvents
            )
        )
    }

}

private fun createCharTypedEvent(ch: Char) = CodingEvent(
    CodingEventType.CHAR_TYPED, ch.toString(), Instant.now()
)
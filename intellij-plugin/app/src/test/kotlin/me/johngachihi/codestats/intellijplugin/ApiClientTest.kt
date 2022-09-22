package me.johngachihi.codestats.intellijplugin

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ApiClientTest {

    @Test
    fun `when request fails with response code is 4xx, throws ClientException`() = runTest {
        val server = MockWebServer().apply {
            enqueue(MockResponse().setResponseCode(400))
            start()
        }

        try {
            DefaultApiClient(server.url("").toString()).sendRequest("/")
            failBecauseExceptionWasNotThrown(ClientException::class.java)
        } catch (_: ClientException) {}

        server.shutdown()
    }

    @Test
    fun `when request fails with response code is 5xx, throws ServerException`() = runTest {
        val server = MockWebServer().apply {
            enqueue(MockResponse().setResponseCode(500))
            start()
        }

        try {
            DefaultApiClient(server.url("").toString()).sendRequest("/")
            failBecauseExceptionWasNotThrown(ServerException::class.java)
        } catch (_: ServerException) {}

        server.shutdown()
    }

    @Test
    fun `returns response body`() = runTest {
        val server = MockWebServer().apply {
            enqueue(MockResponse().setBody("{}"))
            start()
        }

        val response = DefaultApiClient(server.url("").toString())
            .sendRequest("/")

        assertThat(response).isEqualTo("{}")

        server.shutdown()
    }
}
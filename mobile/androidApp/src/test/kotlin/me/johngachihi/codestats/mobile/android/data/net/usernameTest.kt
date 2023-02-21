package me.johngachihi.codestats.mobile.android.data.net

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test


internal class IsUsernameAvailableTest {
    @Test
    fun `sends correct http request`() = runTest {
        val mockServer = MockEngine { respondOk() }
        val httpClient = createHttpClient(mockServer)

        val username = "a-good-username"
        isUsernameAvailable(username = username, httpClient = httpClient)

        mockServer.requestHistory.first().run {
            assertEquals("${Constants.BaseUrl}/username/$username/exists", url.toString())
            assertEquals(HttpMethod.Get, method)
        }
    }

    @Test
    fun `when response is a 404 returns true`() = runTest {
        val mockServer = MockEngine {
            respondError(status = HttpStatusCode.NotFound, content = "")
        }
        val httpClient = createHttpClient(mockServer)

        val response = isUsernameAvailable(username = "a-good-username", httpClient = httpClient)

        assertEquals(true, response)
    }

    @Test
    fun `when response is a 2xx returns false`() = runTest {
        val mockServer = MockEngine {
            respondOk()
        }
        val httpClient = createHttpClient(mockServer)

        val response = isUsernameAvailable(username = "a-good-username", httpClient = httpClient)

        assertEquals(false, response)
    }
}
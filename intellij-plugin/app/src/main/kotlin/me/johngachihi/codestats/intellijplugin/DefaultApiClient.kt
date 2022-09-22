package me.johngachihi.codestats.intellijplugin

import kotlinx.coroutines.future.await
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class ClientException(statusCode: Int) :
    Exception("Request failed with status code: $statusCode")

class ServerException(statusCode: Int) :
    Exception("Request failed with status code: $statusCode")

interface ApiClient {
    suspend fun sendRequest(
        path: String,
        method: String = "POST",
        jsonBody: String = "{}"
    ): String
}

class DefaultApiClient(baseUrl: String? = null) : ApiClient {
    private val baseUrl = URI.create(baseUrl ?: "http://localhost:8080")
    private val client = HttpClient.newHttpClient()

    override suspend fun sendRequest(path: String, method: String, jsonBody: String): String {
        val request = HttpRequest.newBuilder()
            .uri(baseUrl.resolve(path))
            .method(method, HttpRequest.BodyPublishers.ofString(jsonBody))
            .setHeader("content-type", "application/json")
            .build()

        val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .await()

        when (response.statusCode()) {
            in 400..499 -> throw ClientException(statusCode = response.statusCode())
            in 500..599 -> throw ServerException(statusCode = response.statusCode())
        }

        return response.body()
    }
}
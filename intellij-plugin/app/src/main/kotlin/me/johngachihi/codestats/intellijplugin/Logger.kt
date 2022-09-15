package me.johngachihi.codestats.intellijplugin

import com.intellij.openapi.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import me.johngachihi.codestats.core.CodingEvent
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

interface Logger : Disposable {
    fun log(codingEvent: CodingEvent)
}

class RemoteLogger : Logger {
    // Inject in constructor?
    private val client = HttpClient.newHttpClient()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val eventsBuffer: Nothing = TODO()

    override fun log(codingEvent: CodingEvent) {
        coroutineScope.launch {
            val request = HttpRequest.newBuilder()
                .uri(URI.create("https://johngachihi.me/adlfj"))
                .build()

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).await()
        }
    }

    override fun dispose() {
        coroutineScope.cancel()
    }
}
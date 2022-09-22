package me.johngachihi.codestats.intellijplugin

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import kotlinx.coroutines.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import me.johngachihi.codestats.core.CodingEvent
import java.util.concurrent.ConcurrentLinkedDeque

interface Logger : Disposable {
    fun logAsync(codingEvent: CodingEvent): Deferred<Unit>
}

@Service
class RemoteLogger(
    private val apiClient: ApiClient = DefaultApiClient(
        // TODO: Externalize
        "https://little-silence-2856.fly.dev"
    ),
    private val burstSize: Int = 10
) : Logger {
    // Look into using SupervisorJob
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val eventsBuffer = ConcurrentLinkedDeque<CodingEvent>()

    override fun logAsync(codingEvent: CodingEvent): Deferred<Unit> {
        eventsBuffer.add(codingEvent)

        if (eventsBuffer.size < burstSize)
            return CompletableDeferred<Unit>().apply { complete(Unit) }

        return coroutineScope.async {
            val serializedCodingEvent = Json.encodeToString(
                ListSerializer(CodingEventJsonSerializer),
                eventsBuffer.toList()
            )
            eventsBuffer.clear()

            apiClient.sendRequest(path = "/coding-event", jsonBody = serializedCodingEvent)
        }
    }

    override fun dispose() {
        coroutineScope.cancel()
    }
}
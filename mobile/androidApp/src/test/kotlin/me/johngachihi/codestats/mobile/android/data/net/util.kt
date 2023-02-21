import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> MockRequestHandleScope.respondJson(payload: T) = respond(
    content = Json.encodeToString(payload),
    headers = headersOf(HttpHeaders.ContentType, "application/json")
)
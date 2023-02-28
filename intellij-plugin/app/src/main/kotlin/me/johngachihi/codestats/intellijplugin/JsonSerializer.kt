package me.johngachihi.codestats.intellijplugin

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import me.johngachihi.codestats.core.CodingEvent
import me.johngachihi.codestats.core.CodingEventType
import java.time.Instant
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException

@Serializable
private data class CodingEventSurrogate(
    val type: CodingEventType,
    val payload: String,
    @Serializable(with = InstantSerializer::class)
    val firedAt: Instant,
    val username: String?,
    val language: String?,
)

private fun CodingEvent.toCodingEventSurrogate() =
    CodingEventSurrogate(type, payload, firedAt, username, language)

private fun CodingEventSurrogate.toCodingEvent() =
    CodingEvent(type, payload, firedAt, username, language)

object CodingEventJsonSerializer : KSerializer<CodingEvent> {
    override val descriptor: SerialDescriptor = CodingEventSurrogate.serializer().descriptor

    override fun serialize(encoder: Encoder, value: CodingEvent) {
        encoder.encodeSerializableValue(CodingEventSurrogate.serializer(), value.toCodingEventSurrogate())
    }

    override fun deserialize(decoder: Decoder): CodingEvent {
        return decoder.decodeSerializableValue(CodingEventSurrogate.serializer())
            .toCodingEvent()
    }
}

object InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Instant {
        val instantString = decoder.decodeString()
        return try {
            Instant.parse(instantString)
        } catch (e: DateTimeParseException) {
            OffsetDateTime.parse(instantString).toInstant()
        }
    }
}

package me.johngachihi.codestats.core

import java.time.Instant

data class CodingEvent<T>(
    val payload: CodingEventPayload<T>,
    val firedAt: Instant
)

sealed class CodingEventPayload<T>(val type: String, val data: T) {
    data class CharType(val char: Char) : CodingEventPayload<Char>("Type", char)
    data class Paste(val pastedText: String) : CodingEventPayload<String>("Paste", pastedText)
}

package me.johngachihi.codestats.core

import java.time.Instant

open class CodingEvent(
    val type: CodingEventType,
    val payload: String,
    val firedAt: Instant,
    val username: String? = null,
    val language: String? = null,
)

enum class CodingEventType(val label: String) {
    CHAR_TYPED("CharTyped"),
    PASTE("Paste")
}
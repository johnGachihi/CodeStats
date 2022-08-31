package me.johngachihi.codestats.core

import java.time.Instant

open class CodingEvent(
    val type: CodingEventType,
    val payload: String,
    val firedAt: Instant,
)

class CharTypedCodingEvent(private val char: Char, firedAt: Instant) :
    CodingEvent(CodingEventType.CHAR_TYPED, char.toString(), firedAt)

class PasteCodingEvent(private val pastedText: String, firedAt: Instant) :
    CodingEvent(CodingEventType.PASTE, pastedText, firedAt)

enum class CodingEventType(val label: String) {
    CHAR_TYPED("CharTyped"),
    PASTE("Paste")
}

/*sealed class CodingEventType(val label: String) {
    class CHAR_TYPED : CodingEventType("CharTyped")
}*/

/*data class CharTypedCodingEvent(val typedChar: Char, override val firedAt: Instant) : CodingEvent {
    override val type = "CharTyped"
    override val payload = typedChar
}

data class PasteCodingEvent(val pastedText: String, override val firedAt: Instant) : CodingEvent {
    override val type = "Paste"
    override val payload = pastedText
}*/
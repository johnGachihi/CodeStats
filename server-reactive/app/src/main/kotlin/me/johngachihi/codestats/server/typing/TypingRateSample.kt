package me.johngachihi.codestats.server.typing

import java.time.LocalDateTime

data class TypingRateSample(
    val count: Int,
    val lowerLimit: LocalDateTime
)
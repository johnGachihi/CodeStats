package me.johngachihi.codestats.core

import java.time.LocalDateTime

data class TypingRateSample(
    val count: Int,
    val lowerLimit: LocalDateTime
)
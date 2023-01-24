package me.johngachihi.codestats.core

import kotlinx.serialization.Serializable

@Serializable
data class TypingStats(
    val count: Int,
    val rate: List<TypingRateSample>,
)
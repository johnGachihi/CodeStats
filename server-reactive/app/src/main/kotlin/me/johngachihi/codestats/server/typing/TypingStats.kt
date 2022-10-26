package me.johngachihi.codestats.server.typing

data class TypingStats(
    val count: Int,
    val rate: List<TypingRateSample>,
)
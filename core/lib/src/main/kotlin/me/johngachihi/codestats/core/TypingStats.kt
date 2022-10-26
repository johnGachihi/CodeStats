package me.johngachihi.codestats.core

data class TypingStats(
    val count: Int,
    val rate: List<TypingRateSample>,
)
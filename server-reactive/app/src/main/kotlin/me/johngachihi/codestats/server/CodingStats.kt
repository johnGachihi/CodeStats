package me.johngachihi.codestats.server

data class CodingStats(
    val typedCharsToday: Int,
    val typedCharsDistributionToday: List<DistributionEntry>
)
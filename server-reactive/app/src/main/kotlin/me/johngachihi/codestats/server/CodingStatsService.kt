package me.johngachihi.codestats.server

import me.johngachihi.codestats.core.CodingEventType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class CodingStatsService {
    @Autowired
    private lateinit var codingEventRepo: CodingEventRepository

    val startOfToday: Instant
        get() = Instant.now().truncatedTo(ChronoUnit.DAYS)

    val startOfTomorrow: Instant
        get() = startOfToday.plus(1, ChronoUnit.DAYS)

    suspend fun computeStats() = CodingStats(
        typedCharsToday = fetchTypedCharCountToday(),
        typedCharsDistributionToday = fetchTypedCharEventDistributionToday()
    )

    private suspend fun fetchTypedCharCountToday(): Int =
        codingEventRepo.countEventsFiredBetween(
            type = CodingEventType.CHAR_TYPED,
            from = startOfToday,
            toExclusive = startOfTomorrow
        )

    private suspend fun fetchTypedCharEventDistributionToday() =
        codingEventRepo.getEventDistributionBetween(
            type = CodingEventType.CHAR_TYPED,
            from = startOfToday,
            toExclusive = startOfTomorrow
        )
}
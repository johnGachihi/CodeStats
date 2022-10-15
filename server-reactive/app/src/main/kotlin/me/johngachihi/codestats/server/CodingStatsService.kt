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

    suspend fun computeStats() = CodingStats(
        typedCharsToday = getCountOfCharsTypedToday()
    )

    private suspend fun getCountOfCharsTypedToday(): Int {
        val startOfToday = Instant.now().truncatedTo(ChronoUnit.DAYS)
        val startOfTomorrow = startOfToday.plus(1, ChronoUnit.DAYS)

        return codingEventRepo.countEventsFiredBetween(
            type = CodingEventType.CHAR_TYPED,
            from = startOfToday,
            toExclusive = startOfTomorrow
        )
    }
}
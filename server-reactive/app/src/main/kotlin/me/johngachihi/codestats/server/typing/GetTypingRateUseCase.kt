package me.johngachihi.codestats.server.typing

import me.johngachihi.codestats.core.TypingRateSample
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjuster

interface GetTypingRateUseCase {
    suspend operator fun invoke(forDay: LocalDate, period: Period): List<TypingRateSample>
}

@Service
class DefaultGetTypingRateUseCase(
    @Autowired
    private val getTypingActivity: GetTypingActivityUseCase
) : GetTypingRateUseCase {
    override suspend operator fun invoke(forDay: LocalDate, period: Period): List<TypingRateSample> {
        val typingActivity = getTypingActivity(forDay, period)

        val typingRateBuf = mutableMapOf<LocalDateTime, Int>()
        typingActivity.collect {
            val firedAt = LocalDateTime.ofInstant(it.firedAt, ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.MINUTES)
            val timeBucketLowerLimit = firedAt.with(roundByHalfHour())

            typingRateBuf.merge(timeBucketLowerLimit, 1) { old, v -> old + v }
        }

        return typingRateBuf.map {
            TypingRateSample(lowerLimit = it.key, count = it.value)
        }
    }
}

private fun roundByHalfHour(): TemporalAdjuster = TemporalAdjuster { temporal ->
    if (temporal.get(ChronoField.MINUTE_OF_HOUR) < 30)
        temporal.with(ChronoField.MINUTE_OF_HOUR, 0L)
    else
        temporal.with(ChronoField.MINUTE_OF_HOUR, 30)
}
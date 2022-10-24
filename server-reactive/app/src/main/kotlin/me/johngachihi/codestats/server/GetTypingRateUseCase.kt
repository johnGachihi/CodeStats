package me.johngachihi.codestats.server

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjuster

@Service
class GetTypingRateUseCase(
    @Autowired
    private val getTypingActivity: GetTypingActivityUseCase
) {
    suspend operator fun invoke(forDay: LocalDate, period: Period): List<TypingRateSample> {
        val typingActivity = getTypingActivity(forDay, period)

        val typingRateBuf = mutableMapOf<LocalDateTime, TypingRateSample>()
        typingActivity.collect {
            val firedAt = LocalDateTime.ofInstant(it.firedAt, ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.MINUTES)
            val timeBucketLowerLimit = firedAt.with(roundByHalfHour())

            typingRateBuf.compute(timeBucketLowerLimit) { _, v ->
                if (v == null) {
                    TypingRateSample(count = 1, lowerLimit = timeBucketLowerLimit)
                } else {
                    TypingRateSample(count = v.count + 1, lowerLimit = v.lowerLimit)
                }
            }
        }

        return typingRateBuf.values.toList()
    }
}

data class TypingRateSample(
    val count: Long,
    val lowerLimit: LocalDateTime
)

private fun roundByHalfHour(): TemporalAdjuster = TemporalAdjuster { temporal ->
    if (temporal.get(ChronoField.MINUTE_OF_HOUR) < 30)
        temporal.with(ChronoField.MINUTE_OF_HOUR, 0L)
    else
        temporal.with(ChronoField.MINUTE_OF_HOUR, 30)
}
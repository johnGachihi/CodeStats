package me.johngachihi.codestats.server.typing

import kotlinx.coroutines.test.runTest
import me.johngachihi.codestats.core.TypingRateSample
import me.johngachihi.codestats.server.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


internal class DefaultGetTypingRateUseCaseTest {
    @Test
    fun `calls GetTypingActivityUseCase appropriately`() = runTest {
        val aDay = LocalDate.now()
        val mockGetTypingActivity = MockGetTypingActivityUseCase()

        val getTypingRate = DefaultGetTypingRateUseCase(mockGetTypingActivity)
        getTypingRate(forDay = aDay, period = Period.Day)
        getTypingRate(forDay = aDay.plusDays(1), period = Period.Week)
        getTypingRate(forDay = aDay.plusDays(2), period = Period.Month)

        assertThat(mockGetTypingActivity.calls).containsExactlyInAnyOrder(
            MockGetTypingActivityUseCase.Params(aDay, Period.Day),
            MockGetTypingActivityUseCase.Params(aDay.plusDays(1), Period.Week),
            MockGetTypingActivityUseCase.Params(aDay.plusDays(2), Period.Month)
        )
    }

    @Test
    fun `returns series of typing activity counts for thirty-minute buckets making up period starting from first day`() =
        runTest {
            val aDay = LocalDate.now()
            val mockGetTypingActivity = MockGetTypingActivityUseCase(
                makeCharTypedEvent(firedAt = aDay.startOfDay),
                makeCharTypedEvent(firedAt = aDay.startOfDay + 15.min),
                makeCharTypedEvent(firedAt = aDay.startOfDay + 30.min - 1.nanoSecs),
                //
                makeCharTypedEvent(firedAt = aDay.startOfDay + 30.min),
                makeCharTypedEvent(firedAt = aDay.startOfDay + 1.hours - 1.nanoSecs),
                //
                makeCharTypedEvent(firedAt = aDay.startOfDay + 1.hours),
                //
                makeCharTypedEvent(firedAt = (aDay.plusDays(1)).startOfDay),
                makeCharTypedEvent(firedAt = (aDay.plusDays(1)).startOfDay + 30.min - 1.nanoSecs)
            )
            val whateverPeriod = Period.Day

            val typingRate = DefaultGetTypingRateUseCase(mockGetTypingActivity)
                .invoke(forDay = aDay, period = whateverPeriod) // Params don't matter

            assertThat(typingRate).containsExactly(
                TypingRateSample(count = 3, lowerLimit = LocalDateTime.of(aDay, LocalTime.parse("00:00"))),
                TypingRateSample(count = 2, lowerLimit = LocalDateTime.of(aDay, LocalTime.parse("00:30"))),
                TypingRateSample(count = 1, lowerLimit = LocalDateTime.of(aDay, LocalTime.parse("01:00"))),
                TypingRateSample(count = 2, lowerLimit = LocalDateTime.of(aDay.plusDays(1), LocalTime.parse("00:00")))
            )
        }
}


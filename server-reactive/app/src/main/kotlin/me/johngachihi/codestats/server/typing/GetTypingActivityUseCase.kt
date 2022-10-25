package me.johngachihi.codestats.server.typing

import kotlinx.coroutines.flow.Flow
import me.johngachihi.codestats.core.CodingEventType
import me.johngachihi.codestats.server.CodingActivityRepository
import me.johngachihi.codestats.server.CodingEventDataModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.DayOfWeek.MONDAY
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters.*

enum class Period { Day, Week, Month }

interface GetTypingActivityUseCase {
    operator fun invoke(day: LocalDate, period: Period): Flow<CodingEventDataModel>
}

@Service
class DefaultGetTypingActivityUseCase(
    @Autowired val codingActivityRepository: CodingActivityRepository
) : GetTypingActivityUseCase {
    override operator fun invoke(day: LocalDate, period: Period): Flow<CodingEventDataModel> {
        val from = when (period) {
            Period.Day -> day
            Period.Week -> day.with(previousOrSame(MONDAY))
            Period.Month -> day.with(firstDayOfMonth())
        }

        val toExclusive = when (period) {
            Period.Day -> day.plusDays(1)
            Period.Week -> day.with(next(MONDAY))
            Period.Month -> day.with(firstDayOfNextMonth())
        }

        return codingActivityRepository.findAllRecordedBetween(
            type = CodingEventType.CHAR_TYPED,
            from = from.atStartOfDay().toInstant(ZoneOffset.UTC),
            toExclusive = toExclusive.atStartOfDay().toInstant(ZoneOffset.UTC)
        )
    }
}